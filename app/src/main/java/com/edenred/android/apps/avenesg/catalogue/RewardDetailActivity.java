package com.edenred.android.apps.avenesg.catalogue;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.bean.RedeemGiftBean;
import com.edenred.android.apps.avenesg.bean.ShoppingCarBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.NumbersFormat;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhaoxin on 2015/7/23.
 * 商品详情
 */
public class RewardDetailActivity extends BaseActivity {

    private RelativeLayout rl_right;
    private TextView tv_sum, tv_allpoint, tv_add, tv_calagolue_num, tv_point, tv_name, tv_desc;
    private LinearLayout ll_subtract, ll_plus;
    private int num = 0, pointfirst = 0, pointnum = 0, carnum = 0,current=0,carnumfirst=0, tag = 0;
    private RedeemGiftBean data;
//    private SimpleDraweeView iv_pic;
    private ImageView iv_image_view;
    private SharedPreferencesHelper sp;
    private List<ShoppingCarBean> shopCarList=new ArrayList<ShoppingCarBean>();
    private boolean isFirstAdd;//判断当前商品是否已加入购物车

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_reward_detail);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        sp = AveneApplication.getInstance().getSp();
        data = (RedeemGiftBean) getIntent().getSerializableExtra("giftlist");
        tag = getIntent().getIntExtra(Constant.TAG, 0);
        if (tag == 8){
            initLogo();
        }else {
            initLogo2();
        }
        initTitle("Rewards Catalogue");
        initView();
        initData();
    }

    private void initView() {
        rl_right = (RelativeLayout) findViewById(R.id.rl_right);//标题栏右边购物车
        tv_sum = (TextView) findViewById(R.id.tv_sum);
        tv_allpoint = (TextView) findViewById(R.id.tv_allpoint);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_add = (TextView) findViewById(R.id.tv_add);
        ll_subtract = (LinearLayout) findViewById(R.id.ll_subtract);//数字减
        ll_plus = (LinearLayout) findViewById(R.id.ll_plus);//数字加
        tv_calagolue_num = (TextView) findViewById(R.id.tv_calagolue_num);//商品数量
        tv_point = (TextView) findViewById(R.id.tv_point);//商品分数
//        iv_pic = (SimpleDraweeView) findViewById(R.id.iv_pic);
        iv_image_view = (ImageView) findViewById(R.id.iv_image_view);

        rl_right.setVisibility(View.VISIBLE);
        tv_sum.setVisibility(View.VISIBLE);

        tv_add.setOnClickListener(this);
        ll_subtract.setOnClickListener(this);
        ll_plus.setOnClickListener(this);
        rl_right.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sp.getValue(Constant.ACCOUNTBALANCE) != null) {
            tv_allpoint.setText(getResources().getString(R.string.allpoint) +
                    NumbersFormat.thousand(sp.getValue(Constant.ACCOUNTBALANCE)));
        }
        setTextSize(tv_allpoint.getText().toString(),
                tv_allpoint, 17, 16, tv_allpoint.getText().length());
        if(sp.getBooleanValue(Constant.ISSHOPCARLIST))
        {
            Gson gson = new Gson();
            shopCarList = gson.fromJson(sp.getValue(Constant.SHOPCARLIST),
                    new TypeToken<List<ShoppingCarBean>>() {
                    }.getType());
        }
        carnum = shopCarList.size();
        carnumfirst=carnum;
        tv_sum.setText(String.valueOf(carnum));
        isFirstAdd=false;
        current=0;
        for (int i = 0; i <carnum ; i++) {
            if(shopCarList.get(i).id.equals(data.articleId))
            {
                isFirstAdd=true;
                current=i;
                break;
            }
        }
    }

    private void initData() {
        tv_name.setText(data.articleName);
        tv_desc.setText(data.articleDesc);
        tv_point.setText(NumbersFormat.thousand((data.articlePoint).replace(" points", "")));
//        iv_pic.setAspectRatio(1.5f);
//        iv_pic.setImageURI(Uri.parse(Urls.IPANDPORT + data.productImageURL));
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(Urls.IPANDPORT + data.productImageURL, iv_image_view, options);


        if (!TextUtils.isEmpty(tv_calagolue_num.getText().toString())) {
            num = Integer.parseInt(tv_calagolue_num.getText().toString());
        }
        if (!TextUtils.isEmpty(tv_point.getText().toString())) {
            pointfirst = Integer.parseInt(NumbersFormat.noThousand(tv_point.getText().toString()));
            pointnum = pointfirst * num;
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_add:
                if (isFirstAdd) {
                    startAnim(tv_sum, carnumfirst);
                    int n1=Integer.parseInt(shopCarList.get(current).num);
                    int n2=Integer.parseInt(shopCarList.get(current).pointfirst);
                    n1+=num;
                    n2+=pointnum;
                    shopCarList.get(current).num=String.valueOf(n1);
                    shopCarList.get(current).pointfirst=String.valueOf(n2);

                }else
                {
                    isFirstAdd=true;
                    carnumfirst=carnum+1;
                    startAnim(tv_sum, carnumfirst);
                    ShoppingCarBean bean = new ShoppingCarBean();
                    bean.id = data.articleId;
                    bean.title = data.articleName;
                    bean.num = String.valueOf(num);
                    bean.pointfirst = String.valueOf(pointnum);
                    bean.imgUrl = data.productImageURL;
                    shopCarList.add(bean);
                    current=shopCarList.size()-1;
                }
                Log.e("hhhhhhhhhhhhhhhh",shopCarList.get(current).num);
                Log.e("ssssssssssssssss",shopCarList.get(current).pointfirst);
                sp.putBooleanValue(Constant.ISSHOPCARLIST,true);
                sp.putValue(Constant.SHOPCARLIST,new Gson().toJson(shopCarList));

                break;
            case R.id.ll_subtract:
                if (num > 1) {
                    num -= 1;
                    tv_calagolue_num.setText(String.valueOf(num));
                    pointnum = pointfirst * num;
                    tv_point.setText(NumbersFormat.thousand(String.valueOf(pointnum)));
                }
                break;
            case R.id.ll_plus:
                num += 1;
                tv_calagolue_num.setText(String.valueOf(num));
                pointnum = pointfirst * num;
                tv_point.setText(NumbersFormat.thousand(String.valueOf(pointnum)));
                break;
            case R.id.rl_right:
                goto1AnotherActivity(MyRewardActivity.class, tag);
                break;
            default:
                break;
        }
    }


}
