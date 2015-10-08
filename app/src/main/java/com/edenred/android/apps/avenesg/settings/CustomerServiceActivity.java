package com.edenred.android.apps.avenesg.settings;

import android.os.Bundle;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.utils.FontManager;

/**
 * Created by wangwm on 2015/7/28.
 * 客户服务
 */
public class CustomerServiceActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_customer_service);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        initLogo();
        initTitle("Customer Service");
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }
}
