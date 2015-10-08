package com.edenred.android.apps.avenesg.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.utils.ViewHolder;

/**
 * Created by zhaoxin on 2015/7/15.
 * 首页横向商品适配器
 */
public class Adapter extends BaseAdapter{

    Context mContext;
    private int coloumWidth,flag;
    private TextView tv;

    public Adapter(Context mContext,int coloumWidth,int flag)
    {
        this.mContext=mContext;
        this.coloumWidth=coloumWidth;
        this.flag=flag;
    }

    public void setUI(TextView tv)
    {
        this.tv=tv;
    }
    @Override
    public int getCount() {
        return 5;
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
        if(convertView==null)
        {
            convertView=View.inflate(mContext, com.edenred.android.apps.avenesg.R.layout.home_item,null);
        }

        RelativeLayout rl_all= ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.rl_all);
        TextView tv_points=ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_points);
        LinearLayout ll_shoppingcar=ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.ll_shoppingcar);

        //0 首页第一个    1  首页第二个   2 购物车
        if(flag==1)
        {
            tv_points.setVisibility(View.GONE);
            ll_shoppingcar.setVisibility(View.GONE);

        }else
        {
            tv_points.setVisibility(View.VISIBLE);
            ll_shoppingcar.setVisibility(View.VISIBLE);
            if(flag==2)
            {
                ll_shoppingcar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((BaseActivity)mContext).startAnim(tv,0);
                    }
                });
            }

        }


        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl_all
                .getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = coloumWidth;
        rl_all.setLayoutParams(params);
        return convertView;
    }
}
