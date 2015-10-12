package com.edenred.android.apps.avenesg.settings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

/**
 * Created by wangwm on 2015/7/28.
 * 设置
 */
public class SettingsActivity extends BaseActivity{
    private RelativeLayout rl_push;
    private RelativeLayout rl_SMS;
    private RelativeLayout rl_email;
    private RelativeLayout rl_push_on;
    private RelativeLayout rl_push_off;
    private RelativeLayout rl_SMS_on;
    private RelativeLayout rl_SMS_off;
    private RelativeLayout rl_email_on;
    private RelativeLayout rl_email_off;
    private boolean isReceive1, isReceive2, isReceive3;
    private String isAcceptEMail="0",isAcceptPushInfo="0",isAcceptSMS="0";
    private SharedPreferencesHelper sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_settings);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        initLogo();
        sp= AveneApplication.getInstance().getSp();
        initTitle("Settings");
        initView();
        MyAsy myAsy=new MyAsy();
        myAsy.setFlag("0");
        myAsy.execute();
    }

    private void initView() {
        rl_push = (RelativeLayout) findViewById(R.id.rl_push);
        rl_SMS = (RelativeLayout) findViewById(R.id.rl_SMS);
        rl_email = (RelativeLayout) findViewById(R.id.rl_email);
        rl_push_on = (RelativeLayout) findViewById(R.id.rl_push_on);
        rl_push_off = (RelativeLayout) findViewById(R.id.rl_push_off);
        rl_SMS_on = (RelativeLayout) findViewById(R.id.rl_SMS_on);
        rl_SMS_off = (RelativeLayout) findViewById(R.id.rl_SMS_off);
        rl_email_on = (RelativeLayout) findViewById(R.id.rl_email_on);
        rl_email_off = (RelativeLayout) findViewById(R.id.rl_email_off);

        rl_push.setOnClickListener(this);
        rl_SMS.setOnClickListener(this);
        rl_email.setOnClickListener(this);
    }

    private void initData() {

        if(isAcceptSMS.equals("1"))
        {
            rl_SMS_on.setVisibility(View.VISIBLE);
            rl_SMS_off.setVisibility(View.GONE);
            isReceive2 = true;
        }else
        {
            rl_SMS_on.setVisibility(View.GONE);
            rl_SMS_off.setVisibility(View.VISIBLE);
            isReceive2 = false;
        }

        if(isAcceptPushInfo.equals("1"))
        {
            rl_push_on.setVisibility(View.VISIBLE);
            rl_push_off.setVisibility(View.GONE);
            isReceive1 = true;
        }else
        {
            rl_push_on.setVisibility(View.GONE);
            rl_push_off.setVisibility(View.VISIBLE);
            isReceive1 = false;
        }

        if(isAcceptEMail.equals("1"))
        {
            rl_email_on.setVisibility(View.VISIBLE);
            rl_email_off.setVisibility(View.GONE);
            isReceive3 = true;
        }else
        {
            rl_email_on.setVisibility(View.GONE);
            rl_email_off.setVisibility(View.VISIBLE);
            isReceive3 = false;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.rl_push:
                if (!isReceive1){
                    rl_push_on.setVisibility(View.VISIBLE);
                    rl_push_off.setVisibility(View.GONE);
                    isReceive1 = true;
                    isAcceptPushInfo="1";

                } else {
                    rl_push_on.setVisibility(View.GONE);
                    rl_push_off.setVisibility(View.VISIBLE);
                    isReceive1 = false;
                    isAcceptPushInfo="0";
                }

                MyAsy myAsy1=new MyAsy();
                myAsy1.setFlag("1");
                myAsy1.execute();
                break;
            case R.id.rl_SMS:
                if (!isReceive2){
                    rl_SMS_on.setVisibility(View.VISIBLE);
                    rl_SMS_off.setVisibility(View.GONE);
                    isReceive2 = true;
                    isAcceptSMS="1";
                } else {
                    rl_SMS_on.setVisibility(View.GONE);
                    rl_SMS_off.setVisibility(View.VISIBLE);
                    isReceive2 = false;
                    isAcceptSMS="0";
                }

                MyAsy myAsy2=new MyAsy();
                myAsy2.setFlag("1");
                myAsy2.execute();
                break;
            case R.id.rl_email:
                if (!isReceive3){
                    rl_email_on.setVisibility(View.VISIBLE);
                    rl_email_off.setVisibility(View.GONE);
                    isReceive3 = true;
                    isAcceptEMail="1";
                } else {
                    rl_email_on.setVisibility(View.GONE);
                    rl_email_off.setVisibility(View.VISIBLE);
                    isReceive3 = false;
                    isAcceptEMail="0";
                }

                MyAsy myAsy3=new MyAsy();
                myAsy3.setFlag("1");
                myAsy3.execute();
                break;
            default:
                break;
        }
    }

    class MyAsy extends AsyncTask<Object, Object, String> {

        public String flag; // 0--获取会员信息    1--更改设置

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        @Override
        protected String doInBackground(Object... params) {
            HttpUtils httpUtils = new HttpUtils();
            String result = null;
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            hashMap.put("accountId", sp.getValue(Constant.ACCOUNTID));
            if (getFlag().equals("0")) {
                result = httpUtils.putParam(hashMap, Urls.GETMEMBERINFO);
            } else {
                hashMap.put("isAcceptEMail", isAcceptEMail);
                hashMap.put("isAcceptPushInfo", isAcceptPushInfo);
                hashMap.put("isAcceptSMS", isAcceptSMS);
                result = httpUtils.putParam(hashMap, Urls.UPDATEUSERSETTING);
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("error")) {
                ErrorUtils.showErrorMsg(SettingsActivity.this, "404");
                return;
            } else if (getFlag().equals("0")) {
                getMemberXmlpull(result);
            } else {
                setXmlpull(result);
            }
        }
    }
    private void getMemberXmlpull(String result) {
        //解析返回数据
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(SettingsActivity.this, code);
                                return;
                            }
                        }else if(name.equals("isAcceptEMail"))
                        {
                            isAcceptEMail=parser.nextText();
                        }else if(name.equals("isAcceptPushInfo"))
                        {
                            isAcceptPushInfo=parser.nextText();
                        }else if(name.equals("isAcceptSMS"))
                        {
                            isAcceptSMS=parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        break;
                }
                eventType = parser.next();
            }
            Log.e("isAcceptEMail",isAcceptEMail);
            Log.e("isAcceptPushInfo",isAcceptPushInfo);
            Log.e("isAcceptSMS",isAcceptSMS);
            initData();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setXmlpull(String result) {
        //解析返回数据
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(SettingsActivity.this, code);
                                return;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
