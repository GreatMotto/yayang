package com.edenred.android.apps.avenesg.catalogue;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.bean.ShoppingCarBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.NumbersFormat;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.edenred.android.apps.avenesg.utils.ViewHolder;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoxin on 2015/7/27.
 * 我的兑换适配器
 */
public class MyRewardAdapter extends BaseAdapter {

    Context mContext;
    private int allnum = 0;
    private List<ShoppingCarBean> list = new ArrayList<ShoppingCarBean>();
    private SharedPreferencesHelper sp = AveneApplication.getInstance().getSp();

    public MyRewardAdapter(Context mContext, List<ShoppingCarBean> list, int allnum) {
        this.mContext = mContext;
        this.list = list;
        this.allnum = allnum;
    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView = View.inflate(mContext, R.layout.item_myreward, null);
        }

        TextView tv_title = ViewHolder.get(convertView, R.id.tv_productname);
        ImageView iv_delete = ViewHolder.get(convertView, R.id.iv_delete);
        LinearLayout ll_subtract = ViewHolder.get(convertView, R.id.ll_subtract);//数字减
        LinearLayout ll_plus = ViewHolder.get(convertView, R.id.ll_plus);//数字加
        TextView tv_calagolue_num = ViewHolder.get(convertView, R.id.tv_calagolue_num);//商品数量
        TextView tv_point = ViewHolder.get(convertView, R.id.tv_point);//商品分数
        TextView tv_point_true = ViewHolder.get(convertView, R.id.tv_point_true);

//        SimpleDraweeView iv_pic=ViewHolder.get(convertView,R.id.iv_pic);//商品图片
        ImageView iv_image_view = ViewHolder.get(convertView, R.id.iv_image_view);
        tv_title.setText(list.get(position).title);
        tv_calagolue_num.setText(list.get(position).num);
        tv_point.setText(list.get(position).pointfirst);

        int startnum = 1;
        int startpoint = 1;
        if (!TextUtils.isEmpty(list.get(position).num)) {
            startnum = Integer.parseInt(list.get(position).num);
        }
        if (!TextUtils.isEmpty(list.get(position).pointfirst)) {
            startpoint = Integer.parseInt(list.get(position).pointfirst);
            startpoint = startpoint / startnum;
        }
        tv_point_true.setText(NumbersFormat.thousand(String.valueOf(startpoint)));

        if (!TextUtils.isEmpty(list.get(position).imgUrl)) {
//            iv_pic.setAspectRatio(1.5f);
//            iv_pic.setImageURI(Uri.parse(Urls.IPANDPORT + list.get(position).imgUrl));

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(Urls.IPANDPORT + list.get(position).imgUrl, iv_image_view, options);

        }

        iv_delete.setTag(R.id.tv2, tv_point);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
                TextView tv2 = (TextView) v.getTag(R.id.tv2);
                if (!TextUtils.isEmpty(tv2.getText().toString())) {
                    allnum -= Integer.parseInt(tv2.getText().toString());
                }
                ((MyRewardActivity) mContext).resetpoint(allnum, getCount());
                sp.putValue(Constant.SHOPCARLIST, new Gson().toJson(list));
            }
        });

        ll_subtract.setTag(R.id.tv1, tv_calagolue_num);
        ll_subtract.setTag(R.id.tv2, tv_point);
        ll_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int num = 0;
                int pointfirst = 0;
                int pointnum = 0;
                TextView tv1 = (TextView) v.getTag(R.id.tv1);
                TextView tv2 = (TextView) v.getTag(R.id.tv2);
                if (!TextUtils.isEmpty(tv1.getText().toString())) {
                    num = Integer.parseInt(tv1.getText().toString());
                }
                if (!TextUtils.isEmpty(tv2.getText().toString())) {
                    pointfirst = Integer.parseInt(tv2.getText().toString());
                    pointfirst = pointfirst / num;
                }
                if (num > 1) {
                    num -= 1;
                    tv1.setText(String.valueOf(num));
                    pointnum = pointfirst * num;
                    tv2.setText(String.valueOf(pointnum));
                    allnum -= pointfirst;
                    ((MyRewardActivity) mContext).resetpoint(allnum, getCount());
                    list.get(position).pointfirst = String.valueOf(pointnum);
                    list.get(position).num = String.valueOf(num);
                    sp.putValue(Constant.SHOPCARLIST, new Gson().toJson(list));
                }
            }
        });
        ll_plus.setTag(R.id.tv1, tv_calagolue_num);
        ll_plus.setTag(R.id.tv2, tv_point);
        ll_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int num = 0;
                int pointfirst = 0;
                int pointnum = 0;
                TextView tv1 = (TextView) v.getTag(R.id.tv1);
                TextView tv2 = (TextView) v.getTag(R.id.tv2);
                if (!TextUtils.isEmpty(tv1.getText().toString())) {
                    num = Integer.parseInt(tv1.getText().toString());
                }
                if (!TextUtils.isEmpty(tv2.getText().toString())) {
                    pointfirst = Integer.parseInt(tv2.getText().toString());
                    pointfirst = pointfirst / num;
                }
                num += 1;
                tv1.setText(String.valueOf(num));
                pointnum = pointfirst * num;
                tv2.setText(String.valueOf(pointnum));
                allnum += pointfirst;
                ((MyRewardActivity) mContext).resetpoint(allnum, getCount());
                list.get(position).pointfirst = String.valueOf(pointnum);
                list.get(position).num = String.valueOf(num);
                sp.putValue(Constant.SHOPCARLIST, new Gson().toJson(list));
            }
        });
        FontManager.applyFont(mContext, convertView, Constant.TTFNAME);
        return convertView;
    }
}
