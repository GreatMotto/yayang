package com.edenred.android.apps.avenesg.settings;

import android.os.Bundle;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.utils.FontManager;

/**
 * Created by wangwm on 2015/7/28.
 * 联系我们
 */
public class ContactUsActivity extends BaseActivity{
    private TextView tv_email,tv_address,tv_phone;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_contact_us);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        initLogo();
        initTitle("Contact Us");
        initView();
        initData();
    }

    private void initView() {
        tv_email = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_email);
        tv_address = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_address);
        tv_phone = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_phone);
    }

    private void initData() {
        tv_email.setText(AveneApplication.getInstance().dialogBean.contact_us.email);
        tv_address.setText(AveneApplication.getInstance().dialogBean.contact_us.address);
        tv_phone.setText(AveneApplication.getInstance().dialogBean.contact_us.phone);
    }
}
