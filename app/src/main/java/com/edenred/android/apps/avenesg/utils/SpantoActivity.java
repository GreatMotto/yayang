package com.edenred.android.apps.avenesg.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.style.ClickableSpan;
import android.view.View;

import com.edenred.android.apps.avenesg.BaseActivity;

/**
 * Created by zhaoxin on 2015/8/24.
 * Span自定义超链接
 */
public class SpantoActivity extends ClickableSpan implements ParcelableSpan {

    Context mContext;
    Class<?> cls;
    int flag=0;//0 扫码  1 注册第四步   2 注册其他步奏对话框

    public SpantoActivity(Context mContext,Class<?> cls,int flag)
    {
        this.mContext=mContext;
        this.cls=cls;
        this.flag=flag;
    }
    @Override
    public void onClick(View widget) {
        widget.setBackgroundColor(mContext.getResources().getColor(com.edenred.android.apps.avenesg.R.color.white));
        Intent intent=new Intent(mContext,cls);
        if (flag == 1){
            intent.putExtra("flag", "Terms and Conditions");
            intent.putExtra("url", "file:///android_asset/Terms_and_Conditions.html");
        }
        mContext.startActivity(intent);
        if(flag==0)
        {
            ((BaseActivity)mContext).finish();
        }

    }

    @Override
    public int getSpanTypeId() {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
