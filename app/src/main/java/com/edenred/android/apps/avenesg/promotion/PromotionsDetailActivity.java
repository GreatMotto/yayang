package com.edenred.android.apps.avenesg.promotion;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.bean.NotifyMessageBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by wangwm on 2015/7/27.
 * 促销商品详情页面
 */
public class PromotionsDetailActivity extends BaseActivity {
    private TextView tv_title, tv_detail, tv_allpoint;
//    private SimpleDraweeView iv_pic;
    private NotifyMessageBean data;
    private int item = 0;
    private SharedPreferencesHelper sp;
    private ImageView iv_image_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_promotion_and_product_detail);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        initLogo();
        sp = AveneApplication.getInstance().getSp();
        data = (NotifyMessageBean) getIntent().getSerializableExtra("newslist");
        initTitle("News and Promotions");
        initView();
        initData();
    }

    private void initView() {
        tv_allpoint = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_allpoint);
        iv_image_view = (ImageView) findViewById(com.edenred.android.apps.avenesg.R.id.iv_image_view);
//        iv_pic = (SimpleDraweeView) findViewById(R.id.iv_pic);
//        iv_pic.setAspectRatio(1.5f);
//        iv_pic.setImageURI(Uri.parse(Urls.IPANDPORT + data.messageImageUrl));
//        iv_pic.setImageURI(Uri.parse("http://qq.yh31.com/tp/dw/201402042052319838.jpg"));

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(Urls.IPANDPORT + data.messageImageUrl, iv_image_view, options);


        tv_title = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_productname);
        tv_title.setText(data.messageTitle);

        tv_detail = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_detail);
        tv_detail.setText(data.messageContent);
    }

    private void initData() {
        if (sp.getValue(Constant.ACCOUNTBALANCE) != null) {
            tv_allpoint.setText(getResources().getString(com.edenred.android.apps.avenesg.R.string.allpoint) +
                    sp.getValue(Constant.ACCOUNTBALANCE));
        }
        setTextSize(tv_allpoint.getText().toString(),
                tv_allpoint, 17, 16, tv_allpoint.getText().length());
    }
}
