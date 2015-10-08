package com.edenred.android.apps.avenesg.catalogue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.bean.RedeemGiftBean;
import com.edenred.android.apps.avenesg.bean.ShoppingCarBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.DisplayUtil;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.edenred.android.apps.avenesg.utils.ViewHolder;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhaoxin on 2015/7/23.
 * 商品列表适配器
 */
public class GridviewAdapter extends BaseAdapter {

    Context mContext;
    private TextView tv;
    private List<RedeemGiftBean> mlist = new ArrayList<RedeemGiftBean>();
    private SharedPreferencesHelper sp = AveneApplication.getInstance().getSp();
    private List<ShoppingCarBean> shopCarList = new ArrayList<ShoppingCarBean>();
    private boolean[] isFirstAdd;
    private int current = 0, shopNum = 0,title = 0, subtitle = 0;


    public GridviewAdapter(Context mContext, TextView tv, List<RedeemGiftBean> mlist
    ) {
        this.mContext = mContext;
        this.tv = tv;
        this.mlist = mlist;

    }

    public void setUI(int shopNum, List<ShoppingCarBean> shopCarList) {

        this.shopNum = shopNum;
        this.shopCarList = shopCarList;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, com.edenred.android.apps.avenesg.R.layout.home_item, null);
        }

        RelativeLayout rl_all = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.rl_all);
        LinearLayout ll_shoppingcar = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.ll_shoppingcar);
//        SimpleDraweeView iv_pic = ViewHolder.get(convertView, R.id.iv_pic);
        ImageView iv_image_view = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.iv_image_view);
        TextView tv_name = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_name);
        TextView tv_prase = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_prase);
        TextView tv_points = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_points);

        if (TextUtils.isEmpty(AveneApplication.getInstance().dialogBean.max_lenth.title)){
            title = Integer.parseInt(AveneApplication.getInstance().dialogBean.max_lenth.title);
            if (title>30)
            {
                title = 30;
            }
            tv_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(title)});

        }
        if (TextUtils.isEmpty(AveneApplication.getInstance().dialogBean.max_lenth.subtitle)){
            subtitle = Integer.parseInt(AveneApplication.getInstance().dialogBean.max_lenth.subtitle);
            if (subtitle>55)
            {
                subtitle = 55;
            }
            tv_prase.setFilters(new InputFilter[]{new InputFilter.LengthFilter(subtitle)});

        }

        RedeemGiftBean data = mlist.get(position);
        tv_name.setText(data.articleName);
        tv_prase.setText(data.articleDesc);
        tv_points.setText(data.articlePoint + " points");
        if (TextUtils.isEmpty(data.articleDesc)) {
            tv_prase.setText("");
        } else {
            tv_prase.setText(data.articleDesc.replace("\n", ""));
        }
//        iv_pic.setAspectRatio(1.5f);
//        iv_pic.setImageURI(Uri.parse(Urls.IPANDPORT + data.productImageURL));
//        iv_pic.setImageURI(Uri.parse("http://qq.yh31.com/tp/dw/201402042052319838.jpg"));

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(Urls.IPANDPORT + data.productImageURL, iv_image_view, options);

        isFirstAdd = new boolean[mlist.size()];

        ll_shoppingcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShopCar(position);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("giftlist", mlist.get(position));
                intent.setClass(mContext, RewardDetailActivity.class);
                mContext.startActivity(intent);
            }
        });
        FontManager.applyFont(mContext, convertView, Constant.TTFNAME);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl_all
                .getLayoutParams();
        params.height = DisplayUtil.dip2px(mContext, 230);
        rl_all.setLayoutParams(params);
        return convertView;
    }

    private void setShopCar(int pos) {

        isFirstAdd[pos] = false;
        current = 0;
        for (int i = 0; i < shopCarList.size(); i++) {
            if (shopCarList.get(i).id.equals(mlist.get(pos).articleId)) {
                isFirstAdd[pos] = true;
                current = i;
                break;
            }
        }


        if (isFirstAdd[pos]) {
            ((BaseActivity) mContext).startAnim(tv, shopNum);
            int n1 = Integer.parseInt(shopCarList.get(current).num);
            int n2 = Integer.parseInt(shopCarList.get(current).pointfirst);
            n1 += 1;
            n2 += Integer.parseInt(mlist.get(pos).articlePoint);
            shopCarList.get(current).num = String.valueOf(n1);
            shopCarList.get(current).pointfirst = String.valueOf(n2);
        } else {
            isFirstAdd[pos] = true;
            shopNum += 1;
            ((BaseActivity) mContext).startAnim(tv, shopNum);
            ShoppingCarBean bean = new ShoppingCarBean();
            bean.id = mlist.get(pos).articleId;
            bean.title = mlist.get(pos).articleName;
            bean.num = String.valueOf(1);
            bean.pointfirst = mlist.get(pos).articlePoint;
            bean.imgUrl = mlist.get(pos).productImageURL;
            shopCarList.add(bean);
            current = shopCarList.size() - 1;
        }

        sp.putBooleanValue(Constant.ISSHOPCARLIST, true);
        sp.putValue(Constant.SHOPCARLIST, new Gson().toJson(shopCarList));

    }

}
