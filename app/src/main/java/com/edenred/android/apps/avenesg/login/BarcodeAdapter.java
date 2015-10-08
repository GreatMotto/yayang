package com.edenred.android.apps.avenesg.login;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.utils.DisplayUtil;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.ViewHolder;

/**
 * Created by wangwm on 2015/7/22.
 */
public class BarcodeAdapter extends BaseAdapter {

    Context mContext;

    public BarcodeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 4;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, com.edenred.android.apps.avenesg.R.layout.item_guide, null);
        }
        RelativeLayout ll_circle = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.ll_circle);
        RelativeLayout ll_rectangle = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.ll_rectangle);
        RelativeLayout ll_circle_1 = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.ll_circle_1);
        TextView tv_circle_grey = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_circle_grey);
        TextView tv_circle_orange = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_circle_orange);
        TextView tv_circle_grey_1 = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_circle_grey_1);

        int screenwidth = DisplayUtil.getWidth(mContext);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_circle.getLayoutParams();
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) ll_rectangle.getLayoutParams();
        params1.width = (screenwidth - DisplayUtil.dip2px(mContext, 112)) / 4;
        tv_circle_grey.setText(String.valueOf(position + 1));

        if (position > 0) {
            params.leftMargin = -2;
            ll_circle.setLayoutParams(params);
            if (position == 3){
                ll_circle_1.setVisibility(View.VISIBLE);
                tv_circle_grey_1.setText("5");
            }
        } else {
            tv_circle_orange.setVisibility(View.VISIBLE);
        }
        ll_rectangle.setLayoutParams(params1);
        FontManager.applyFont(mContext, convertView, Constant.TTFNAME);
        return convertView;
    }
}
