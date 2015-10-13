package com.edenred.android.apps.avenesg.catalogue;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.bean.NotifyMessageBean;
import com.edenred.android.apps.avenesg.bean.RedeemGiftBean;
import com.edenred.android.apps.avenesg.bean.ShoppingCarBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.promotion.PromotionsDetailActivity;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoxin on 2015/7/27.
 * 横向滑动List适配器
 */
public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.ViewHolder> {

    private int width = 0;
    private int flag = 0;
    static Context mContext;
    private TextView tv;
    private List<RedeemGiftBean> list = new ArrayList<RedeemGiftBean>();
    private List<NotifyMessageBean> list1 = new ArrayList<NotifyMessageBean>();
    private SharedPreferencesHelper sp = AveneApplication.getInstance().getSp();
    private List<ShoppingCarBean> shopCarList = new ArrayList<ShoppingCarBean>();
    private boolean[] isFirstAdd;
    private int current = 0, shopNum = 0, title = 0, subtitle = 0;

    public RecylerAdapter(Context mContext, int width, int flag, List<?> list) {
        super();
        this.mContext = mContext;
        this.width = width;
        this.flag = flag;
        if (flag == 1) {
            this.list1 = (List<NotifyMessageBean>) list;
        } else {
            this.list = (List<RedeemGiftBean>) list;
        }
    }


    public void setUI(TextView tv, int shopNum, List<ShoppingCarBean> shopCarList) {
        this.tv = tv;
        this.shopNum = shopNum;
        this.shopCarList = shopCarList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 创建一个View，简单起见直接使用系统提供的布局
        View view = View.inflate(mContext, com.edenred.android.apps.avenesg.R.layout.home_item, null);
        FontManager.applyFont(mContext, view, Constant.TTFNAME);
        // 创建一个ViewHolder
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        // 绑定数据到ViewHolder上


        if (TextUtils.isEmpty(AveneApplication.getInstance().dialogBean.title)) {
            title = Integer.parseInt(AveneApplication.getInstance().dialogBean.title);
            if (title > 30) {
                title = 30;
            }
            viewHolder.tv_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(title)});

        }
        if (TextUtils.isEmpty(AveneApplication.getInstance().dialogBean.subtitle)) {
            subtitle = Integer.parseInt(AveneApplication.getInstance().dialogBean.subtitle);
            if (subtitle > 55) {
                subtitle = 55;
            }
            viewHolder.tv_prase.setFilters(new InputFilter[]{new InputFilter.LengthFilter(subtitle)});

        }

        //0 首页第一个    1  首页第二个   2 购物车
        if (flag == 1) {
            viewHolder.tv_points.setVisibility(View.GONE);
            viewHolder.ll_shoppingcar.setVisibility(View.GONE);
            viewHolder.iv_left.setVisibility(View.VISIBLE);
            viewHolder.tv_detail.setBackgroundResource(com.edenred.android.apps.avenesg.R.mipmap.detailbase);
            viewHolder.tv_name.setText(list1.get(i).messageTitle);
            if (TextUtils.isEmpty(list1.get(i).messageContent)) {
                viewHolder.tv_prase.setText("");
            } else {
                viewHolder.tv_prase.setText(list1.get(i).messageContent.replace("\n", ""));
            }

//            viewHolder.iv_pic.setAspectRatio(1.5f);
//            viewHolder.iv_pic.setImageURI(Uri.parse(Urls.IPANDPORT + list1.get(i).messageImageUrl));
//            for (int j = 0; j < i; j++){
//                Log.e("Url111111111111s", Urls.IPANDPORT + list1.get(i).messageImageUrl);
//            }
//            viewHolder.iv_pic.setImageURI(Uri.parse("http://img2.pcpop.com/ArticleImages/0x0/0/841/000841994.jpg"));

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(Urls.IPANDPORT + list1.get(i).messageImageUrl, viewHolder.iv_image_view, options);



        } else {
            viewHolder.tv_points.setVisibility(View.VISIBLE);
            viewHolder.ll_shoppingcar.setVisibility(View.VISIBLE);
            viewHolder.iv_left.setVisibility(View.GONE);
            viewHolder.tv_detail.setBackgroundResource(com.edenred.android.apps.avenesg.R.mipmap.orange_grey);
            viewHolder.tv_name.setText(list.get(i).articleName);
            if (TextUtils.isEmpty(list.get(i).articleDesc)) {
                viewHolder.tv_prase.setText("");
            } else {
                viewHolder.tv_prase.setText(list.get(i).articleDesc.replace("\n", ""));
            }
            viewHolder.tv_points.setText(list.get(i).articlePoint + " points");

//            viewHolder.iv_pic.setAspectRatio(1.5f);
//            viewHolder.iv_pic.setImageURI(Uri.parse(Urls.IPANDPORT + list.get(i).productImageURL));
//            for (int j = 0; j < i; j++){
//                Log.e("Url22222222222s", Urls.IPANDPORT + list.get(i).productImageURL);
//            }
//            viewHolder.iv_pic.setImageURI(Uri.parse(Constant.ImageUrls[i]));

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(Urls.IPANDPORT + list.get(i).productImageURL, viewHolder.iv_image_view, options);

            if (flag == 2) {
                isFirstAdd = new boolean[list.size()];

                viewHolder.ll_shoppingcar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setShopCar(i);
                    }
                });
            }

        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag) {
                    case 0:
                        Intent intent = new Intent(mContext, RewardDetailActivity.class);
                        intent.putExtra("giftlist", list.get(i));
                        mContext.startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(mContext, PromotionsDetailActivity.class);
                        intent1.putExtra("newslist", list1.get(i));
                        mContext.startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(mContext, RewardDetailActivity.class);
                        intent2.putExtra("giftlist", list.get(i));
                        mContext.startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        });

        //设置Item的宽高
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.rl_all
                .getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = width;
        viewHolder.rl_all.setLayoutParams(params);

    }

    private void setShopCar(int pos) {
//        if(!isFirstAdd[pos])
//        {
        isFirstAdd[pos] = false;
        current = 0;
        for (int i = 0; i < shopCarList.size(); i++) {
            if (shopCarList.get(i).id.equals(list.get(pos).articleId)) {
                isFirstAdd[pos] = true;
                current = i;
                break;
            }
        }
//        }

        if (isFirstAdd[pos]) {
            ((BaseActivity) mContext).startAnim(tv, shopNum);
            int n1 = Integer.parseInt(shopCarList.get(current).num);
            int n2 = Integer.parseInt(shopCarList.get(current).pointfirst);
            n1 += 1;
            n2 += Integer.parseInt(list.get(pos).articlePoint);
            shopCarList.get(current).num = String.valueOf(n1);
            shopCarList.get(current).pointfirst = String.valueOf(n2);
        } else {
            isFirstAdd[pos] = true;
            shopNum += 1;
            ((BaseActivity) mContext).startAnim(tv, shopNum);
            ShoppingCarBean bean = new ShoppingCarBean();
            bean.id = list.get(pos).articleId;
            bean.title = list.get(pos).articleName;
            bean.num = String.valueOf(1);
            bean.pointfirst = list.get(pos).articlePoint;
            bean.imgUrl = list.get(pos).productImageURL;
            shopCarList.add(bean);
            current = shopCarList.size() - 1;
        }

        sp.putBooleanValue(Constant.ISSHOPCARLIST, true);
        sp.putValue(Constant.SHOPCARLIST, new Gson().toJson(shopCarList));

    }


    @Override
    public int getItemCount() {
        if (flag == 1) {
            return list1.size();
        } else {
            return list.size();
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout rl_all;
        public LinearLayout ll_shoppingcar;
        public TextView tv_points, tv_detail, tv_name, tv_prase;
        public ImageView iv_left, iv_image_view;
//        public SimpleDraweeView iv_pic;


        public ViewHolder(View itemView) {
            super(itemView);
            rl_all = (RelativeLayout) itemView.findViewById(com.edenred.android.apps.avenesg.R.id.rl_all);
            ll_shoppingcar = (LinearLayout) itemView.findViewById(com.edenred.android.apps.avenesg.R.id.ll_shoppingcar);
            tv_points = (TextView) itemView.findViewById(com.edenred.android.apps.avenesg.R.id.tv_points);
            tv_name = (TextView) itemView.findViewById(com.edenred.android.apps.avenesg.R.id.tv_name);
            tv_prase = (TextView) itemView.findViewById(com.edenred.android.apps.avenesg.R.id.tv_prase);
            tv_detail = (TextView) itemView.findViewById(com.edenred.android.apps.avenesg.R.id.tv_detail);
            iv_left = (ImageView) itemView.findViewById(com.edenred.android.apps.avenesg.R.id.iv_left);
//            iv_pic = (SimpleDraweeView) itemView.findViewById(R.id.iv_pic);
            iv_image_view = (ImageView) itemView.findViewById(com.edenred.android.apps.avenesg.R.id.iv_image_view);

        }
    }


}