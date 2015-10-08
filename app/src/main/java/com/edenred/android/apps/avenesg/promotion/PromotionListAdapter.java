package com.edenred.android.apps.avenesg.promotion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.bean.NotifyMessageBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwm on 2015/7/27.
 * 促销商品的适配器
 */
public class PromotionListAdapter extends BaseAdapter {
    Context mContext;
    List<NotifyMessageBean> list = new ArrayList<NotifyMessageBean>();
    private int title = 0, subtitle = 0;

    public PromotionListAdapter(Context mContext, List<NotifyMessageBean> list) {
        this.mContext = mContext;
        this.list = list;
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
            convertView = View.inflate(mContext, com.edenred.android.apps.avenesg.R.layout.item_detail, null);
        }
        FontManager.applyFont(mContext, convertView, Constant.TTFNAME);
        TextView tv_name = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_name);
        TextView tv_prase = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_prase);
//        SimpleDraweeView my_image_view = ViewHolder.get(convertView, R.id.my_image_view);
        ImageView iv_image_view = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.iv_image_view);

        if (TextUtils.isEmpty(AveneApplication.getInstance().dialogBean.max_lenth.title)) {
            title = Integer.parseInt(AveneApplication.getInstance().dialogBean.max_lenth.title);
            if (title > 30) {
                title = 30;
            }
            tv_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(title)});

        }
        if (TextUtils.isEmpty(AveneApplication.getInstance().dialogBean.max_lenth.subtitle)) {
            subtitle = Integer.parseInt(AveneApplication.getInstance().dialogBean.max_lenth.subtitle);
            if (subtitle > 55) {
                subtitle = 55;
            }
            tv_prase.setFilters(new InputFilter[]{new InputFilter.LengthFilter(subtitle)});

        }

        NotifyMessageBean data = list.get(position);
        tv_name.setText(data.messageTitle);
        tv_prase.setText(data.messageContent);
//        my_image_view.setAspectRatio(1.5f);
//        my_image_view.setImageURI(Uri.parse(Urls.IPANDPORT + data.messageImageUrl));
//        my_image_view.setImageURI(Uri.parse("http://qq.yh31.com/tp/dw/201402042052319838.jpg"));

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(Urls.IPANDPORT + data.messageImageUrl, iv_image_view, options);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, PromotionsDetailActivity.class);
                intent1.putExtra("newslist", list.get(position));
                mContext.startActivity(intent1);
            }
        });

        return convertView;
    }


}
