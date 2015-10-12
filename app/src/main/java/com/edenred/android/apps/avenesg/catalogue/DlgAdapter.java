package com.edenred.android.apps.avenesg.catalogue;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.bean.StringBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.NumbersFormat;
import com.edenred.android.apps.avenesg.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoxin on 2015/7/23.
 * 筛选数据对话框适配器
 */
public class DlgAdapter extends BaseAdapter {

    Context mContext;
    private List<StringBean> list = new ArrayList<StringBean>();
    private int current, flag;

    public DlgAdapter(Context mContext, List<StringBean> list, int current, int flag) {
        this.mContext = mContext;
        this.list = list;
        this.current = current;
        this.flag = flag;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, com.edenred.android.apps.avenesg.R.layout.item_menu, null);
        }

        TextView tv_item = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_item);
        if (flag == 1){
            tv_item.setText(NumbersFormat.thousand(list.get(position).text));
        }else {
            tv_item.setText(list.get(position).text);
        }
        tv_item.setTextColor(mContext.getResources().getColor(com.edenred.android.apps.avenesg.R.color.black_dark));

        if (position == current) {
            convertView.setBackgroundColor(mContext.getResources().getColor(com.edenred.android.apps.avenesg.R.color.grey_light));
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(com.edenred.android.apps.avenesg.R.color.white));
        }

        FontManager.applyFont(mContext, convertView, Constant.TTFNAME);
        return convertView;
    }
}
