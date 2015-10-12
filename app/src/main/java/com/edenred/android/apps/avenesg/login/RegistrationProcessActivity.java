package com.edenred.android.apps.avenesg.login;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.utils.DisplayUtil;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.view.HorizontalListView;

/**
 * Created by wangwm on 2015/7/15.
 * 注册向导
 */
public class RegistrationProcessActivity extends BaseActivity {

    private int screenwidth = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_registration_process);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);

        AveneApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        initTitle("Registration Process");

        TextView btn_start_register = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.btn_start_register);
        btn_start_register.setOnClickListener(this);

        HorizontalListView hlv_guide = (HorizontalListView) findViewById(com.edenred.android.apps.avenesg.R.id.hlv_guide);
        GuideAdapter adapter = new GuideAdapter(this);
        hlv_guide.setAdapter(adapter);

        LinearLayout ll_imagestep = (LinearLayout) findViewById(com.edenred.android.apps.avenesg.R.id.ll_imagestep);

        // 设置向导图片
        screenwidth = DisplayUtil.getWidth(this);

        System.out.println(DisplayUtil.px2dip(this, screenwidth));
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(Constant.RegisterImage[0]);
        imageView.measure(0, 0);
        int imagewidth = imageView.getMeasuredWidth();
        System.out.println(imagewidth);
        for (int i = 1; i < 6; i++) {
            ImageView iv_imagestep = new ImageView(this);
            iv_imagestep.setImageResource(Constant.RegisterImage[i - 1]);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(i != 1){
                params1.leftMargin = (screenwidth - DisplayUtil.dip2px(this, 36) - imagewidth * 5 ) / 4;
            }
            iv_imagestep.setLayoutParams(params1);
            ll_imagestep.addView(iv_imagestep);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case com.edenred.android.apps.avenesg.R.id.btn_start_register:
                goto1OtherActivity(SubmitEANCodeActivity.class,0);
                break;
        }
    }
}
