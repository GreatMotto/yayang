package com.edenred.android.apps.avenesg.product;

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
import com.edenred.android.apps.avenesg.bean.ProductBean;
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
 * 产品列表适配器
 */
public class ProductsListAdapter extends BaseAdapter {
    Context mContext;
    private List<ProductBean> plist = new ArrayList<ProductBean>();
    private int title = 0, subtitle = 0;

    public ProductsListAdapter(Context mContext, List<ProductBean> plist) {
        this.mContext = mContext;
        this.plist = plist;
    }

    @Override
    public int getCount() {
        return plist.size();
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
//        SimpleDraweeView iv_pic = ViewHolder.get(convertView, R.id.my_image_view);
        ImageView iv_image_view = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.iv_image_view);
        TextView tv_name = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_name);
        TextView tv_prase = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_prase);

        if (TextUtils.isEmpty(AveneApplication.getInstance().dialogBean.title)) {
            title = Integer.parseInt(AveneApplication.getInstance().dialogBean.title);
            if (title > 30) {
                title = 30;
            }
            tv_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(title)});

        }
        if (TextUtils.isEmpty(AveneApplication.getInstance().dialogBean.subtitle)) {
            subtitle = Integer.parseInt(AveneApplication.getInstance().dialogBean.subtitle);
            if (subtitle > 55) {
                subtitle = 55;
            }
            tv_prase.setFilters(new InputFilter[]{new InputFilter.LengthFilter(subtitle)});

        }


        ProductBean data = plist.get(position);

        tv_name.setText(data.productName);
        if (!TextUtils.isEmpty(data.productDesc)) {
            tv_prase.setText(data.productDesc.replace("\n", ""));
        }
//        iv_pic.setAspectRatio(1.5f);
//        iv_pic.setImageURI(Uri.parse(Urls.IPANDPORT + data.productImageURL));
//        iv_pic.setImageURI(Uri.parse("http://qq.yh31.com/tp/dw/201402042052319838.jpg"));


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(Urls.IPANDPORT + data.productImageURL, iv_image_view, options);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", plist.get(position));
                intent.setClass(mContext, ProductDetailsActivity.class);
                mContext.startActivity(intent);
            }
        });
        FontManager.applyFont(mContext, convertView, Constant.TTFNAME);
        return convertView;
    }
}
