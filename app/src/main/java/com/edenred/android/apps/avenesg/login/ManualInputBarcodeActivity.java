package com.edenred.android.apps.avenesg.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.DialogUtils;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.view.HorizontalListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

/**
 * Created by wangwm on 2015/7/16.
 * 手动输入条形码
 */
public class ManualInputBarcodeActivity extends BaseActivity {
    private EditText et_barcode;
    private TextView btn_submit;
    private LinearLayout ll_header;
    private int flag = 0, tag = 0;
    private HorizontalListView hlv_guide;
    private String str = "";
    private View view_header;
    private BarcodeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_manual_input);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        flag = getIntent().getIntExtra(Constant.FLAG, 0);//0,注册跳转  1/2,首页跳转
        tag = getIntent().getIntExtra(Constant.TAG, 0);
        initView();
        initData();
    }

    private void initView() {
        et_barcode = (EditText) findViewById(com.edenred.android.apps.avenesg.R.id.et_barcode);
        btn_submit = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.btn_submit);
        ll_header = (LinearLayout) findViewById(com.edenred.android.apps.avenesg.R.id.ll_header);
        view_header = findViewById(com.edenred.android.apps.avenesg.R.id.view_header);

        btn_submit.setOnClickListener(this);

    }

    private void initData() {
        //0,注册跳转  1,首页跳转
        if (flag == 0) {
            initTitle("Registration Process");
            ll_header.setVisibility(View.VISIBLE);
            view_header.setVisibility(View.VISIBLE);
            hlv_guide = (HorizontalListView) findViewById(com.edenred.android.apps.avenesg.R.id.hlv_guide);
            adapter = new BarcodeAdapter(this);
            hlv_guide.setAdapter(adapter);
        } else {
            if (tag == 8){
                initLogo();
            }else {
                initLogo2();
            }
            initTitle("Manual Input");
            ll_header.setVisibility(View.GONE);
            view_header.setVisibility(View.GONE);
        }
    }


    public boolean canSubmit() {
        if (et_barcode.getText().toString().trim().length() < 13) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case com.edenred.android.apps.avenesg.R.id.btn_submit:
                str = et_barcode.getText().toString().trim();
                if (canSubmit()) {
                    showPD("Upload...");
                    new MyAsy().execute();
//                    goto1OtherActivity(UniqueCodeEnterActivity.class, flag);
                } else {
                    // 信息填写不完整
                    if (flag == 0) {
                        DialogUtils.oneButtonDialog(
                                "Invalid EAN Code",
                                "You have entered an invalid EAN code. Please note that bundle packs and kits are not eligible to Eau Thermale Avene points. " +
                                        "Try again or kindly contact our customer service.",
                                "Close", 156, 172, this);
                    } else {
                        DialogUtils.oneButtonDialog(
                                "Invalid EAN Code",
                                "You have entered an invalid EAN code. Please note that bundle packs and kits are not eligible to Eau Thermale Avene points. " +
                                        "Try again or kindly contact our customer service.",
                                "Close", 156, 172, this);
                    }

                }
                break;
        }
    }

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
                ErrorUtils.showErrorMsg(ManualInputBarcodeActivity.this, "404");
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
                            Log.e("exitCode", code);
                            if (!code.equals("0")) {
                                //得到的条形码错误
                                if (flag == 0) {
                                    DialogUtils.oneButtonDialog(
                                            "Invalid EAN Code",
                                            "You have entered an invalid EAN code. Please note that bundle packs and kits are not eligible to Avene points. " +
                                                    "Please try again or kindly contact our customer service.",
                                            "Close", 150, 166, this);
                                } else {
                                    DialogUtils.oneButtonDialog(
                                            "Invalid EAN Code",
                                            "You have scanned an invalid EAN code. Make sure you have selected the right code. Please try again or kindly contact our customer service.",
                                            "Close", 0, 0, this);
                                }
                                return;
                            }

                        } else if (name.equals("productId")) {
                            AveneApplication.getInstance().productbean.productId = parser.nextText();
                        } else if (name.equals("imgUrl")) {
                            AveneApplication.getInstance().productbean.productImageURL = parser.nextText();
                        } else if (name.equals("productName")) {
                            AveneApplication.getInstance().productbean.productName = parser.nextText();
                        } else if (name.equals("productDescription")) {
                            AveneApplication.getInstance().productbean.productDesc = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        //得到的条形码正确
                        break;
                }
                eventType = parser.next();
            }

            AveneApplication.getInstance().registerUniqueCode = str;
            goto2OtherActivity(UniqueCodeEnterActivity.class, flag, tag);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
