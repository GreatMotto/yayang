package com.edenred.android.apps.avenesg.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import com.edenred.android.apps.avenesg.BaseActivity;

/**
 * Created by zhaoxin on 2015/7/15.
 */
public abstract class BaseViewActivity extends BaseActivity{

    private boolean isSecondTime = false;
    private Handler mExitHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            isSecondTime = false;
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_base);
    }
//    /**
//     * 跳抓其他一级页面
//     *
//     * @author ZhangYi 2014年9月10日 下午10:50:26
//     * @param v
//     * @param clazz
//     */
//    private void go2OtherAc(View v, Class<?> clazz) {
//        if (getAcIndex() != Integer.parseInt(v.getTag().toString())) {
//            startActivity(new Intent(this, clazz));
//            overridePendingTransition(0, 0);
//            finish();
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (isSecondTime) {
                finish();
                System.exit(0);
            } else {
                Toast.makeText(this,"Again according to exit the application!",Toast.LENGTH_SHORT).show();
                isSecondTime = true;
                mExitHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mExitHandler.sendEmptyMessage(0);
                    }
                }, 2000);
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 获取一级页面下标
     *
     * @author ZhangYi 2014年9月10日 下午10:43:21
     * @return
     */
    protected abstract int getAcIndex();
}
