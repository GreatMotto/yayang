package com.edenred.android.apps.avenesg.product;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.bean.ProductBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.NumbersFormat;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by wangwm on 2015/7/27.
 * 产品列表详情页面
 */
public class ProductDetailsActivity extends BaseActivity {

    private TextView tv_detail,tv_allpoint,tv_title;
    private SharedPreferencesHelper sp;
    private ProductBean data;
//    private SimpleDraweeView iv_pic;
    private ImageView iv_image_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_promotion_and_product_detail);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        initLogo();
        sp= AveneApplication.getInstance().getSp();
        data= (ProductBean) getIntent().getSerializableExtra("data");
        initTitle("Products");
        initView();
        initData();
    }

    private void initView() {
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_allpoint= (TextView) findViewById(R.id.tv_allpoint);
//        iv_pic= (SimpleDraweeView) findViewById(R.id.iv_pic);
        tv_title= (TextView) findViewById(R.id.tv_productname);
        iv_image_view = (ImageView) findViewById(R.id.iv_image_view);
    }

    private void initData() {
        if(sp.getValue(Constant.ACCOUNTBALANCE)!=null)
        {
            tv_allpoint.setText(getResources().getString(R.string.allpoint)+
                    NumbersFormat.thousand(sp.getValue(Constant.ACCOUNTBALANCE)));
        }
        //改变字体大小
        setTextSize(tv_allpoint.getText().toString(),
                tv_allpoint, 17, 16, tv_allpoint.getText().length());
        tv_title.setText(data.productName);
        tv_detail.setText(data.productDesc);
//        iv_pic.setAspectRatio(1.5f);
//        iv_pic.setImageURI(Uri.parse(Urls.IPANDPORT + data.productImageURL));
//        iv_pic.setImageURI(Uri.parse("http://qq.yh31.com/tp/dw/201402042052319838.jpg"));

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(Urls.IPANDPORT + data.productImageURL, iv_image_view, options);

    }

}
