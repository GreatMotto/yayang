package com.edenred.android.apps.avenesg.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.home.SweepActivity;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.view.HorizontalListView;

/**
 * Created by wangwm on 2015/7/16.
 * 提交商品条形码
 */
public class SubmitEANCodeActivity extends BaseActivity {

    private int flag = 0, tag = 0;
    private LinearLayout ll_header;
    private TextView btn_to_manual;
    private ImageView iv_scan;
    private View view_header;
    private HorizontalListView hlv_guide;
    private BarcodeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_submit_product_barcode);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        flag = getIntent().getIntExtra(Constant.FLAG, 0);//0,注册跳转  1,菜单跳转  2,首页跳转
        tag = getIntent().getIntExtra(Constant.TAG, 0);
        initView();
        initData();
    }

    private void initView() {
        iv_scan = (ImageView) findViewById(com.edenred.android.apps.avenesg.R.id.iv_scan);
        btn_to_manual = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.btn_to_manual);
        ll_header = (LinearLayout) findViewById(com.edenred.android.apps.avenesg.R.id.ll_header);
        view_header = findViewById(com.edenred.android.apps.avenesg.R.id.view_header);

        iv_scan.setOnClickListener(this);
        btn_to_manual.setOnClickListener(this);
    }

    private void initData() {
        //0,注册跳转  1,菜单跳转  2,首页跳转
        if (flag == 0) {
            initTitle("Registration Process");
            ll_header.setVisibility(View.VISIBLE);
            view_header.setVisibility(View.VISIBLE);
            hlv_guide = (HorizontalListView) findViewById(com.edenred.android.apps.avenesg.R.id.hlv_guide);
            adapter = new BarcodeAdapter(this);
            hlv_guide.setAdapter(adapter);
        } else {
            if (tag == 8) {
                initLogo();
            } else {
                initLogo2();
            }
            initTitle("Submit EAN Code");
            ll_header.setVisibility(View.GONE);
            view_header.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case com.edenred.android.apps.avenesg.R.id.iv_scan:
                goto2OtherActivity(SweepActivity.class, flag, tag);
                break;

            case com.edenred.android.apps.avenesg.R.id.btn_to_manual:
                goto2OtherActivity(ManualInputBarcodeActivity.class, flag, tag);
                break;
        }
    }
}
