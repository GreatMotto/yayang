package com.edenred.android.apps.avenesg.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.bean.StringBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.ViewHolder;

import java.util.List;


public class PopAdapter extends BaseAdapter {

    private Context mContext;
    private List<StringBean> list;

    public PopAdapter(Context context, List<StringBean> list) {
        super();
        this.mContext = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.pop_item, null);
        }
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_item_label);
        tv_name.setText(list.get(position).text);
        FontManager.applyFont(mContext, convertView, Constant.TTFNAME);
        return convertView;
    }

}
