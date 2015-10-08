package com.edenred.android.apps.avenesg;

/**
 * Created by zhaoxin on 2015/7/14.
 * 引导页
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.edenred.android.apps.avenesg.bean.DialogBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.home.HomeActivity;
import com.edenred.android.apps.avenesg.login.LoginActivity;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.HttpPostUtil;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class SplashScreenActivity extends BaseActivity {

    private SharedPreferencesHelper sp;
    private boolean isRight;//判断登录是否成功

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);
        sp = AveneApplication.getInstance().getSp();
        getDialogInfo();

        //如果已经有登录状态  请求登录接口
        if (sp.getBooleanValue(Constant.ISLOGIN)) {
            new  MyAsy().execute();
        }
        new Handler().postDelayed(new Runnable() {

            public void run() {
                /*
                 * Create an Intent that will start the Main WordPress Activity.
                 */
                sp.putBooleanValue(Constant.ISLOGIN,isRight);
                if (isRight) {
                    // type = sp.getValue(Constant.SP_GOWHERE);
                    gotoOtherActivity(HomeActivity.class);
                    SplashScreenActivity.this.finish();
                } else {
                    gotoFirstAC();
                }

            }
        }, 3000);
    }

    private void gotoFirstAC() {
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        SplashScreenActivity.this.startActivity(intent);
        SplashScreenActivity.this.finish();
    }

    private void getDialogInfo() {
        new Thread() {
            public void run() {
                try {
                    HttpPostUtil u = new HttpPostUtil(Urls.GETDIALOGNAME);
                    byte[] b = u.send();
                    String result = new String(b);
                    JSONObject jsonR = new JSONObject(result);
                    if (jsonR.getString("status").equals("1")) {
                        Gson gson = new Gson();
                        DialogBean dialogData = gson.fromJson(jsonR.getString("data"),
                                new TypeToken<DialogBean>() {
                                }.getType());
                        AveneApplication.getInstance().dialogBean=dialogData;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class MyAsy extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... params) {
            HttpUtils httpUtils = new HttpUtils();
            String result = null;
            HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("securityKey", "abc123$");
                hashMap.put("mobileCode", sp.getValue(Constant.AREACODE).replace("+", ""));
                hashMap.put("mobileNumber", sp.getValue(Constant.PHONE));
                hashMap.put("password", sp.getValue(Constant.PASSWORD));
                result = httpUtils.putParam(hashMap, Urls.MEMBERLOGIN);

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
                isRight=false;
                ErrorUtils.showErrorMsg(SplashScreenActivity.this, "404");
                return;
            }
                MemberXmlPull(result);

        }
    }

    private void MemberXmlPull(String result) {
        //解析返回数据
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();
            //循环取出所有数据
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
                                isRight=false;
                                ErrorUtils.showErrorMsg(SplashScreenActivity.this, code);
                                return;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        break;
                }
                eventType = parser.next();
            }
            isRight=true;

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
