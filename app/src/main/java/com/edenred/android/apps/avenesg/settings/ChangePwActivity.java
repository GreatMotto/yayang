package com.edenred.android.apps.avenesg.settings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.home.HomeActivity;
import com.edenred.android.apps.avenesg.utils.DialogUtils;
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
 * 修改密码
 */
public class ChangePwActivity extends BaseActivity{
    private EditText et_current_password;
    private EditText et_new_password;
    private EditText et_confirm_new_password;
    private TextView btn_submit;
    private SharedPreferencesHelper sp;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_changepw);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        initLogo();
        sp = AveneApplication.getInstance().getSp();
        initTitle("Change Password");
        initView();
    }

    private void initView() {
        et_current_password = (EditText) findViewById(com.edenred.android.apps.avenesg.R.id.et_current_password);

        et_new_password = (EditText) findViewById(com.edenred.android.apps.avenesg.R.id.et_new_password);

        et_confirm_new_password = (EditText) findViewById(com.edenred.android.apps.avenesg.R.id.et_confirm_new_password);

        btn_submit = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.btn_submit);
        btn_submit.setOnClickListener(this);
    }

    public boolean canSubmit() {
        //旧密码输入位数小于6
        if (et_current_password.getText().toString().trim().length() < 6) {
            DialogUtils.oneButtonDialog("Password Error", "You have entered an invalid current password.", "Continue", 0, 0, this);
            return false;
        }
        //旧密码错误
        if (!et_current_password.getText().toString().trim().equals(sp.getValue(Constant.PASSWORD))) {
            DialogUtils.oneButtonDialog("Password Error", "You have entered an invalid current password.", "Continue", 0, 0, this);
            return false;
        }
        //新密码位数少于6
        if (et_new_password.getText().toString().trim().length() < 6) {
            DialogUtils.oneButtonDialog("Password Error", "You have entered an invalid new password.", "Continue", 0, 0, this);
            return false;
        }
        //确认密码位数少于6
        if (et_confirm_new_password.getText().toString().trim().length() < 6) {
            DialogUtils.oneButtonDialog("Password Error", "You have entered an invalid vertify password.", "Continue", 0, 0, this);
            return false;
        }
        //两次输入的新密码不一致
        if (!et_new_password.getText().toString().trim().equals(et_confirm_new_password.getText().toString().trim())) {
            DialogUtils.oneButtonDialog("Password Error", "You have entered new password don't match.", "Continue", 0, 0, this);
            return false;
        }
        //新密码和旧密码一样
        if (et_new_password.getText().toString().trim().equals(et_current_password.getText().toString().trim())) {
            DialogUtils.oneButtonDialog("Password Error", "You have entered an invalid password.", "Continue", 0, 0, this);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId())
        {
            case com.edenred.android.apps.avenesg.R.id.btn_submit:
                if (canSubmit()) {
                    password=et_new_password.getText().toString().trim();
                    showPD("Requesting...");
                    new MyAsy().execute();
                }
                break;
            default:
                break;
        }

    }

    //Object, Object, String调用参数，进度和结果
    class MyAsy extends AsyncTask<Object, Integer, String> {

        //执行耗时操作
        @Override
        protected String doInBackground(Object... params) {
            //请求接口
            HttpUtils httpUtils = new HttpUtils();
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            hashMap.put("mobileCode", sp.getValue(Constant.AREACODE).replace("+", ""));
            hashMap.put("mobileNumber", sp.getValue(Constant.PHONE));
            hashMap.put("newPassword", password);
            hashMap.put("oldPassword", sp.getValue(Constant.PASSWORD));

            String result = httpUtils.putParam(hashMap, Urls.MODIFYPASSWORD);
            return result;
        }

        //通知进度改变
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //doInBackground执行完成之后执行结果处理  可以更新UI
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelPD();
            if (s.equals("error")) {
                ErrorUtils.showErrorMsg(ChangePwActivity.this, "404");
                return;
            }
            XmlPull(s);
        }
    }
    private void XmlPull(String s) {
        //解析返回数据
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(s));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            if (!code.equals("0")) {
                                DialogUtils.oneButtonDialog("Password Error", "You have entered an invalid password.", "Continue", 0, 0, this);
                                return;
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            sp.putValue(Constant.PASSWORD, et_new_password.getText().toString().trim());
            onBackPressed();
            HomeActivity.instanceHomeAc.toggleMenu();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
