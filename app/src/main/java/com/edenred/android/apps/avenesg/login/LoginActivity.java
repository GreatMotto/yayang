package com.edenred.android.apps.avenesg.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.bean.AccountInfoBean;
import com.edenred.android.apps.avenesg.bean.DialogBean;
import com.edenred.android.apps.avenesg.bean.StringBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.home.HomeActivity;
import com.edenred.android.apps.avenesg.utils.DialogUtils;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.HttpPostUtil;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.utils.NumbersFormat;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangwm on 2015/7/15.
 * 登录
 */
public class LoginActivity extends BaseActivity implements ShowPopWindow.PopListener{

    private RelativeLayout rl_country_code;
    private TextView tv_country_code, btn_login, tv_forgot_password, tv_not_member,
            tv_contact_us, textView;
    private EditText et_mobile, et_password;
    private SharedPreferencesHelper sp;
    private List<StringBean> mlist = null;
    private String mobile, password, countrycode, mareacode, mphone;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);
        AveneApplication.getInstance().addActivity(this);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        sp = AveneApplication.getInstance().getSp();
        initView();
        initData();
        getDialogInfo();
    }

    private void initView() {
        // 国家区号
        rl_country_code = (RelativeLayout) findViewById(R.id.rl_country_code);
        tv_country_code = (TextView) findViewById(R.id.tv_country_code);
        // 手机号
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        // 密码
        et_password = (EditText) findViewById(R.id.et_password);
        // 登录
        btn_login = (TextView) findViewById(R.id.btn_login);
        // 忘记密码
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        // 不是会员
        tv_not_member = (TextView) findViewById(R.id.tv_not_member);
        // 联系我们
        tv_contact_us = (TextView) findViewById(R.id.tv_contact_us);
        tv_contact_us.setOnClickListener(this);

        tv_forgot_password.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_not_member.setOnClickListener(this);
        rl_country_code.setOnClickListener(this);


        //引用外部字体
//        Typeface type= Typeface.createFromAsset(getAssets(), "font/FtraBk.ttf");
//        btn_login.setTypeface(type);
//        btn_login.getPaint().setFakeBoldText(true);//设置粗体

    }


    private void initData() {
        if (sp.getBooleanValue(Constant.ISLOGIN)) {
//            tv_country_code.setText(sp.getValue(Constant.AREACODE));
//            et_mobile.setText(sp.getValue(Constant.PHONE));
//            et_password.setText(sp.getValue(Constant.PASSWORD));
        }
        //进入APP请求国家列表接口  如果已经获取 直接读取
        if (!sp.getBooleanValue(Constant.ISCOUNTRY)) {
            MyAsy myAsy = new MyAsy();
            myAsy.setFlag("4");
            myAsy.execute();
        } else {
            Gson gson = new Gson();
            mlist = gson.fromJson(sp.getValue(Constant.COUNTRYLIST),
                    new TypeToken<List<StringBean>>() {
                    }.getType());
        }
    }

    public boolean canLogin() {
        if (tv_country_code.getText().toString().trim().replace("+", "").equals("65")){
            if (!et_mobile.getText().toString().trim().matches(Constant.CheckSingaporeMobile)){
                return false;
            }
        }
        if (et_mobile.getText().toString().trim().length() < 8) {
            return false;
        }
        if (et_password.getText().toString().trim().length() < 6) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_login:
                if (canLogin()) {
                    showPD("Logging...");
                    MyAsy myAsy = new MyAsy();
                    myAsy.setFlag("1");
                    countrycode = tv_country_code.getText().toString().replace("+", "");
                    mobile = et_mobile.getText().toString().trim();
                    password = et_password.getText().toString().trim();
                    myAsy.execute();
                } else {
                    DialogUtils.oneButtonDialog("Error", "You have entered an invalid mobile number or password.", "Please Try Again", 0, 0, this);
                }
                break;

            case R.id.tv_forgot_password:
                DialogUtils.HttpListener listener = new DialogUtils.HttpListener() {
                    @Override
                    public void sendPassword(String areacode, String phone) {
                        mareacode = areacode;
                        mphone = phone;
                        MyAsy myAsy = new MyAsy();
                        myAsy.setFlag("3");
                        myAsy.execute();
                    }

                    @Override
                    public void getHttp(View v, TextView tv) {
                        view = v;
                        textView = tv;
                        MyAsy myAsy = new MyAsy();
                        myAsy.setFlag("2");
                        myAsy.execute();
                    }
                };
                DialogUtils.withMobileDialog("Forgot Your Password",
                        "Your new password will be sent to your mobile via SMS.",
                        tv_country_code.getText().toString().trim(),
                        et_mobile.getText().toString().trim(),
                        "Retrieve Password", this, mlist, listener);
                break;

            case R.id.tv_not_member:
                gotoOtherActivity(RegistrationProcessActivity.class);
                break;

            case R.id.rl_country_code:
                et_password.clearFocus();
                et_mobile.clearFocus();
                CloseKeyboard();
                if (!sp.getBooleanValue(Constant.ISCOUNTRY)) {
                    MyAsy myAsy = new MyAsy();
                    myAsy.setFlag("0");
                    myAsy.execute();
                } else {
                    ShowPopWindow.showPopWindow(LoginActivity.this, rl_country_code, 0, 0, tv_country_code, mlist,this);
                }
                break;
            case R.id.tv_contact_us:
                DialogUtils.contactUsDialog(this);
                break;
            default:
                break;
        }

    }

    @Override
    public void getPopWindowPosition(View v, int pos, TextView tv) {

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

        public String flag; // 0--弹出登陆界面国家区号列表    1--登录接口
        // 2--弹出忘记密码对话框国家区号列表   3--重置密码接口   4 进入APP获取城市列表

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        @Override
        protected String doInBackground(Object... params) {
            String currentFlag = getFlag();
            HttpUtils httpUtils = new HttpUtils();
            String result = null;
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            if (currentFlag.equals("1")) {
                hashMap.put("mobileCode", countrycode);
                hashMap.put("mobileNumber", mobile);
                hashMap.put("password", password);
                result = httpUtils.putParam(hashMap, Urls.MEMBERLOGIN);
            } else if (currentFlag.equals("3")) {
                hashMap.put("mobileCode", mareacode.replace("+", ""));
                hashMap.put("mobileNumber", mphone);
                hashMap.put("sendType", "1");
                result = httpUtils.putParam(hashMap, Urls.SENDVERIFICATIONINFO);
            } else {
                result = httpUtils.putParam(hashMap, Urls.GETCOUNTRYLIST);
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
            cancelPD();
            if (result.equals("error")) {
                ErrorUtils.showErrorMsg(LoginActivity.this, "404");
                return;
            }
            String currentFlag = getFlag();
            if (currentFlag.equals("1")) {
                MemberXmlPull(result);

            } else if (currentFlag.equals("3")) {
                SendPasswordXmlpull(result);
            } else {
                CountryXmlPull(result, currentFlag);
            }
        }
    }

    private void SendPasswordXmlpull(String result) {
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
                            Log.e("exitCode", code);
                            if (!code.equals("0")) {
//                                ErrorUtils.showErrorMsg(LoginActivity.this, code);
                                switch (code) {
                                    case "-10":
                                        Toast.makeText(this, "No matched member account", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "-11":
                                        Toast.makeText(this, "Multiple member account matched", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "-12":
                                        Toast.makeText(this, "SMS Failure", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "-13":
                                        Toast.makeText(this, "Sending SMS over times", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "12":
                                        Toast.makeText(this, "Sending SMS Failure", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(this, "Network connection is failed", Toast.LENGTH_SHORT).show();
                                        break;
                                }
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

    private void CountryXmlPull(String result, String flag) {
        //解析返回数据
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();
            StringBean db = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        mlist = new ArrayList<StringBean>();
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            Log.e("exitCode", code);
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(LoginActivity.this, code);
                                return;
                            }
                        }
                        if (name.equals("countryList")) {
                            db = new StringBean();
                        } else if (db != null) {
                            if (name.equals("countryId")) {
                                db.id = parser.nextText();// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("countryName")) {
                                db.countryName = parser.nextText();
                            } else if (name.equalsIgnoreCase("countryMobileCode")) {
                                db.text = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        //读完一个countryList，可以将其添加到集合类中
                        if (parser.getName().equals("countryList") && db != null) {
                            mlist.add(db);
                            db = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            if (flag.equals("0")) {
                ShowPopWindow.showPopWindow(LoginActivity.this, rl_country_code, 0, 0, tv_country_code, mlist,this);
            } else if (flag.equals("2")) {
                ShowPopWindow.showPopWindow(LoginActivity.this, view, 0, 0, textView, mlist,this);
            }

            //国家获取后本地存储
            Gson gson = new Gson();
            String citylist = gson.toJson(mlist);

            sp.putBooleanValue(Constant.ISCOUNTRY, true);
            sp.putValue(Constant.COUNTRYLIST, citylist);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void MemberXmlPull(String result) {
        //解析返回数据
        XmlPullParserFactory factory = null;
        String id="";
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();
            AccountInfoBean db = null;
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
                            Log.e("exitCode", code);
                            if (!code.equals("0")) {
                                DialogUtils.oneButtonDialog("Error", "You have entered an invalid mobile number or password.", "Please Try Again", 0, 0, this);
                                return;
                            }
                        }
                        if (name.equals("accountInfoPO")) {
                            db = new AccountInfoBean();
                        } else if (db != null) {
                            if (name.equals("accountId")) {
                                id=parser.nextText();
                                db.accountId = id;// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("firstName")) {
                                db.firstName = parser.nextText();
                            } else if (name.equalsIgnoreCase("lastName")) {
                                db.lastName = parser.nextText();
                            } else if (name.equalsIgnoreCase("accountBalance")) {
                                db.accountBalance = parser.nextText();
                            } else if (name.equalsIgnoreCase("pointsEarned")) {
                                db.pointsEarned = parser.nextText();
                            } else if (name.equalsIgnoreCase("pointsRedemed")) {
                                db.pointsRedemed = parser.nextText();
                            } else if (name.equalsIgnoreCase("pointsExpired")) {
                                db.pointsExpired = parser.nextText();
                            } else if (name.equalsIgnoreCase("willExpiringNextMon")) {
                                db.willExpiringNextMon = parser.nextText();
                            } else if (name.equalsIgnoreCase("expiringPointsDate")) {
                                db.expiringPointsDate = parser.nextText();
                            } else if (name.equalsIgnoreCase("activationStatus")) {
                                db.activationStatus = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equals("accountInfoPO") && db != null) {

                            if(!sp.getValue(Constant.ACCOUNTID).equals(id))
                            {
                                sp.putBooleanValue(Constant.ISSHOPCARLIST,false);
                                sp.putValue(Constant.SHOPCARLIST,"");
                            }
                            sp.putValue(Constant.ACCOUNTBALANCE, db.accountBalance);
                            sp.putValue(Constant.EARNED, db.pointsEarned);
                            sp.putValue(Constant.ACCOUNTID, db.accountId);
                            sp.putValue(Constant.REDEMEED, db.pointsRedemed);
                            sp.putValue(Constant.EXPIRED, db.pointsExpired);
                            sp.putValue(Constant.LASTNAME, db.lastName);
                            sp.putValue(Constant.FIRSTNAME, db.firstName);
                            sp.putValue(Constant.WILLEXPIRINGNEXTMON, db.willExpiringNextMon);
                            sp.putValue(Constant.EXPIRINGPOINTSDATE, db.expiringPointsDate);
                        }
                        break;
                }
                eventType = parser.next();
            }

            //得到数据后操作
            sp.putBooleanValue(Constant.ISLOGIN, true);
            sp.putValue(Constant.AREACODE, tv_country_code.getText().toString());
            sp.putValue(Constant.PHONE, et_mobile.getText().toString().trim());
            sp.putValue(Constant.PASSWORD, et_password.getText().toString().trim());

            gotoOtherActivity(HomeActivity.class);
            finish();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}