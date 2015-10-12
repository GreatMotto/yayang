package com.edenred.android.apps.avenesg.home;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.login.BarcodeAdapter;
import com.edenred.android.apps.avenesg.login.UniqueCodeEnterActivity;
import com.edenred.android.apps.avenesg.ocr.CameraManager;
import com.edenred.android.apps.avenesg.ocr.InactivityTimer;
import com.edenred.android.apps.avenesg.ocr.SweepActivityHandler;
import com.edenred.android.apps.avenesg.ocr.ViewfinderView;
import com.edenred.android.apps.avenesg.utils.DialogUtils;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.view.HorizontalListView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by zhaoxin on 2015/7/16.
 * 扫码
 */
public class SweepActivity extends BaseActivity implements SurfaceHolder.Callback {

    private SweepActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;// surface有没有被绘制
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;// 完成扫描时是否震动提示
    private int flag = 0, tag = 0;
    private LinearLayout ll_header;
    private String str = "";

    // private ImageView returnButton;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_sweep);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        flag = getIntent().getIntExtra(Constant.FLAG, 0);
        tag = getIntent().getIntExtra(Constant.TAG, 0);
        CameraManager.init(getApplication());
        /*
         * returnButton = (ImageView) findViewById(R.id.iv_return_btn);
		 * returnButton.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) { SweepActivity.this.finish();
		 * } });
		 */
        initView();
        viewfinderView = (ViewfinderView) findViewById(com.edenred.android.apps.avenesg.R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);// activity静止一段时间会自动关闭

    }

    private void initView() {
        ll_header = (LinearLayout) findViewById(com.edenred.android.apps.avenesg.R.id.ll_header);
        if (flag == 0) {
            initTitle("Registration Process");
            ll_header.setVisibility(View.VISIBLE);
            HorizontalListView hlv_guide = (HorizontalListView) findViewById(com.edenred.android.apps.avenesg.R.id.hlv_guide);
            BarcodeAdapter adapter = new BarcodeAdapter(this);
            hlv_guide.setAdapter(adapter);
        } else {
            if (tag == 8) {
                initLogo();
            } else {
                initLogo2();
            }
            initTitle("Submit EAN Code");
            ll_header.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(com.edenred.android.apps.avenesg.R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = false;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        // initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    // 初始化照相机
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new SweepActivityHandler(SweepActivity.this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    // 二维码扫描结果数据
    public void handleDecode(Result obj, Bitmap barcode) {
        inactivityTimer.onActivity();
        // viewfinderView.drawResultBitmap(barcode);//画结果图片
        playBeepSoundAndVibrate();// 启动声音效果

        str = obj.getText();
//        Toast.makeText(this,"扫描结果：" + str,Toast.LENGTH_LONG).show();

        try {
            showPD("Upload...");
            new MyAsy().execute();


        } catch (Exception e) {

        }
    }

    /*
     * //声音控制 private void initBeepSound() { if (playBeep && mediaPlayer ==
     * null) { setVolumeControlStream(AudioManager.STREAM_MUSIC); mediaPlayer =
     * new MediaPlayer();
     * mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
     * mediaPlayer.setOnCompletionListener(beepListener);
     *
     * AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
     * try { mediaPlayer.setDataSource(file.getFileDescriptor(),
     * file.getStartOffset(), file.getLength()); file.close();
     * mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME); mediaPlayer.prepare(); }
     * catch (IOException e) { mediaPlayer = null; } } }
     */
    private static final long VIBRATE_DURATION = 200L;

    // 启动声音功能
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    class MyAsy extends AsyncTask<Object, Object, String> {


        @Override
        protected String doInBackground(Object... params) {
            HttpUtils httpUtils = new HttpUtils();
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            hashMap.put("barcode", str);
            String result = httpUtils.putParam(hashMap, Urls.BARCODEVALIDATION);
            return result;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cancelPD();
            if (result.equals("error")) {
                ErrorUtils.showErrorMsg(SweepActivity.this, "404");
                return;
            }
            Xmlpull(result);
        }
    }


    private void Xmlpull(String result) {
        //解析返回数据
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            if (!code.equals("0")) {
                                //得到的条形码错误
                                DialogUtils.ProfileDlg(this,
                                        "Invalid EAN Code",
                                        "You have scanned an invalid EAN code. " +
                                                "Please note that bundle packs and kits are not eligible to Eau Thermale Avène points. " +
                                                "Try again or kindly contact our customer service."
                                        , 3, flag);
                                return;
                            }

                        } else if (name.equals("productId")) {
                            AveneApplication.getInstance().productbean.productId = parser.nextText();
                        } else if (name.equals("imgUrl")) {
                            AveneApplication.getInstance().productbean.productImageURL = parser.nextText();
                        } else if (name.equals("ProductName")) {
                            AveneApplication.getInstance().productbean.productName = parser.nextText();
                        } else if (name.equals("ProductDescription")) {
                            AveneApplication.getInstance().productbean.productDesc = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        break;
                }
                eventType = parser.next();
            }
            //得到的条形码正确
            AveneApplication.getInstance().registerUniqueCode = str;
            goto2OtherActivity(UniqueCodeEnterActivity.class, flag, tag);
            finish();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
