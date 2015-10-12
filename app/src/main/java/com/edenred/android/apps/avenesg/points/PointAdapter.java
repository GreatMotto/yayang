package com.edenred.android.apps.avenesg.points;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.bean.PointBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.utils.DialogUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.NumbersFormat;
import com.edenred.android.apps.avenesg.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoxin on 2015/7/20.
 * 我的积分适配器
 */
public class PointAdapter extends BaseAdapter{

    Context mContext;
    private int width=0,flag=0;
    private List<PointBean> list=new ArrayList<PointBean>();

    public  PointAdapter(Context mContext,int width,List<PointBean>list)
    {
        this.mContext=mContext;
        this.width=width;
        this.list=list;
    }
    public void setFlag(int flag)
    {
        this.flag=flag;
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
        if (flag==1)
        {
            convertView=View.inflate(mContext, com.edenred.android.apps.avenesg.R.layout.item_point_two,null);
            TextView tv_data= ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_data);
            TextView tv_center= ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_center);
            TextView tv_right= ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_right);
            TextView tv_right1= ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_right1);
            TextView tv_right2= ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_right2);
            LinearLayout ll_plus=ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.ll_plus);

            PointBean data=list.get(position);
            tv_data.setText(data.id);
            tv_right.setText(data.description.replace("null",""));
            tv_right1.setText(data.quantity);
            tv_right2.setText(NumbersFormat.thousand(data.point));

            if(!TextUtils.isEmpty(data.pointGetDate))
            {
                String[] str=data.pointGetDate.split("-");
                tv_center.setText(str[2]+"/"+str[1]+"/"+str[0]);
            }

            ll_plus.setTag(com.edenred.android.apps.avenesg.R.id.tv1,tv_data);
            ll_plus.setTag(com.edenred.android.apps.avenesg.R.id.tv2,tv_center);
            ll_plus.setTag(com.edenred.android.apps.avenesg.R.id.tv3,tv_right);
            ll_plus.setTag(com.edenred.android.apps.avenesg.R.id.tv4,tv_right1);
            ll_plus.setTag(com.edenred.android.apps.avenesg.R.id.tv5,tv_right2);

            ll_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv1= (TextView) v.getTag(com.edenred.android.apps.avenesg.R.id.tv1);
                    TextView tv2= (TextView) v.getTag(com.edenred.android.apps.avenesg.R.id.tv2);
                    TextView tv3= (TextView) v.getTag(com.edenred.android.apps.avenesg.R.id.tv3);
                    TextView tv4= (TextView) v.getTag(com.edenred.android.apps.avenesg.R.id.tv4);
                    TextView tv5= (TextView) v.getTag(com.edenred.android.apps.avenesg.R.id.tv5);
                    DialogUtils.PointDlg(mContext,tv1.getText().toString(),tv2.getText().toString(),
                            tv3.getText().toString(),tv4.getText().toString(),tv5.getText().toString());
                }
            });
        }else
        {
            convertView=View.inflate(mContext, com.edenred.android.apps.avenesg.R.layout.item_point,null);
            TextView tv_data=ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_data);
            TextView tv_center= ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_center);
            TextView tv_right= ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_right);
            PointBean data=list.get(position);
            if(flag==0)
            {
                tv_center.setVisibility(View.VISIBLE);
                tv_center.setText(data.description.replace("null",""));

            }else
            {
                tv_center.setVisibility(View.GONE);
            }

            tv_right.setText(NumbersFormat.thousand(data.point));
            if(!TextUtils.isEmpty(data.pointGetDate))
            {
                String[] str=data.pointGetDate.split("-");
                tv_data.setText(str[2]+"/"+str[1]+"/"+str[0]);
            }


        }
        FontManager.applyFont(mContext, convertView, Constant.TTFNAME);
        return convertView;
    }
}
