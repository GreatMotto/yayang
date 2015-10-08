package com.edenred.android.apps.avenesg.login;

import android.annotation.TargetApi;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.bean.StringBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.settings.WebViewActivity;
import com.edenred.android.apps.avenesg.utils.DialogUtils;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.edenred.android.apps.avenesg.view.HorizontalListView;
import com.edenred.android.apps.avenesg.view.PopAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangwm on 2015/7/17.
 * 输入用户信息
 */
public class PersonalDetailsEnterActivity extends BaseActivity {

    private TextView btn_submit, tv_agree, tv_gender, tv_country_code;
    private EditText et_first_name, et_last_name, et_email, et_mobile, et_create_password, et_confirm_password;
    private ImageView iv_agree;
    private PopupWindow popSort;
    private List<StringBean> listpop = new ArrayList<StringBean>();
    private PopAdapter sortAdapter;
    private HorizontalListView hlv_guide;
    private RelativeLayout rl_agree, rl_gender, rl_country_code;
    private boolean isAgree = false;
    private List<StringBean> mlist = null;
    private SharedPreferencesHelper sp;
    private PersonalAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_personal_details_enter);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);

        AveneApplication.getInstance().addActivity(this);
        sp = AveneApplication.getInstance().getSp();
        initView();
        initData();
    }


    private void initView() {
        initTitle("Registration process");
        hlv_guide = (HorizontalListView) findViewById(com.edenred.android.apps.avenesg.R.id.hlv_guide);
        btn_submit = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.btn_submit);
        // 性别
        rl_gender = (RelativeLayout) findViewById(com.edenred.android.apps.avenesg.R.id.rl_gender);
        tv_gender = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_gender);
        // 国家区号
        rl_country_code = (RelativeLayout) findViewById(com.edenred.android.apps.avenesg.R.id.rl_country_code);
        tv_country_code = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_country_code);
        // 姓
        et_first_name = (EditText) findViewById(com.edenred.android.apps.avenesg.R.id.et_first_name);
        // 名
        et_last_name = (EditText) findViewById(com.edenred.android.apps.avenesg.R.id.et_last_name);
        // 电子邮箱
        et_email = (EditText) findViewById(com.edenred.android.apps.avenesg.R.id.et_email);
        // 手机号
        et_mobile = (EditText) findViewById(com.edenred.android.apps.avenesg.R.id.et_mobile);
        // 创建密码
        et_create_password = (EditText) findViewById(com.edenred.android.apps.avenesg.R.id.et_create_password);
        // 确认密码
        et_confirm_password = (EditText) findViewById(com.edenred.android.apps.avenesg.R.id.et_confirm_password);
        // 同意
        rl_agree = (RelativeLayout) findViewById(com.edenred.android.apps.avenesg.R.id.rl_agree);
        iv_agree = (ImageView) findViewById(com.edenred.android.apps.avenesg.R.id.iv_agree);
        tv_agree = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_agree);

        btn_submit.setOnClickListener(this);
        rl_gender.setOnClickListener(this);
        rl_country_code.setOnClickListener(this);
        tv_agree.setOnClickListener(this);
        rl_agree.setOnClickListener(this);

    }

    private void initData() {
        setlineSpan(getResources().getString(com.edenred.android.apps.avenesg.R.string.agree), tv_agree, 75, 95, WebViewActivity.class, 1);
        adapter = new PersonalAdapter(this);
        hlv_guide.setAdapter(adapter);
        if (!sp.getBooleanValue(Constant.ISCOUNTRY)) {
            MyAsy myAsy = new MyAsy();
            myAsy.execute();
        } else {
            Gson gson = new Gson();
            mlist = gson.fromJson(sp.getValue(Constant.COUNTRYLIST),
                    new TypeToken<List<StringBean>>() {
                    }.getType());
        }
    }

    public boolean canSubmit() {
        if (TextUtils.isEmpty(et_first_name.getText().toString().trim())) {
            return false;
        }
        if (TextUtils.isEmpty(et_last_name.getText().toString().trim())) {
            return false;
        }
        if (TextUtils.isEmpty(tv_gender.getText().toString().trim())) {
            return false;
        }
        if (TextUtils.isEmpty(et_email.getText().toString().trim())) {
            return false;
        }
        String email = et_email.getText().toString().trim();
        Pattern pattern = Pattern
                .compile(Constant.TESTEMAIL);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find()) {
            return false;
        }
        if (TextUtils.isEmpty(tv_country_code.getText().toString().trim())) {
            return false;
        }
        if (tv_country_code.getText().toString().trim().replace("+", "").equals("65")) {
            if (!et_mobile.getText().toString().trim().matches(Constant.CheckSingaporeMobile)) {
                return false;
            }
        }
        if (et_mobile.getText().toString().trim().length() < 8) {
            return false;
        }
        if (et_create_password.getText().toString().trim().length() < 6) {
            return false;
        }
        if (et_confirm_password.getText().toString().trim().length() < 6) {
            return false;
        }
        if (!et_create_password.getText().toString().trim().equals(et_confirm_password.getText().toString().trim())) {
            return false;
        }
        if (!isAgree) {
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case com.edenred.android.apps.avenesg.R.id.btn_submit:
                if (canSubmit()) {
//                    mareacode = tv_country_code.getText().toString().trim();
//                    mphone = et_mobile.getText().toString().trim();
//                    MyAsy myAsy = new MyAsy();
//                    myAsy.setFlag("1");
//                    myAsy.execute();
                    AveneApplication.getInstance().memberinfo.firstName = et_first_name.getText().toString().trim();
                    AveneApplication.getInstance().memberinfo.lastName = et_last_name.getText().toString().trim();
                    AveneApplication.getInstance().memberinfo.sexId = tv_gender.getText().toString().trim();
                    AveneApplication.getInstance().memberinfo.email = et_email.getText().toString().trim();
                    AveneApplication.getInstance().memberinfo.mobileCode = tv_country_code.getText().toString().trim();
                    AveneApplication.getInstance().memberinfo.mobileNumber = et_mobile.getText().toString().trim();
                    AveneApplication.getInstance().memberinfo.password = et_create_password.getText().toString().trim();
                    gotoOtherActivity(ConfirmAccountActivity.class);
                } else {
//                  // 信息填写不完整
                    String email = et_email.getText().toString().trim();
                    Pattern pattern = Pattern
                            .compile(Constant.TESTEMAIL);
                    Matcher matcher = pattern.matcher(email);
                    if (TextUtils.isEmpty(et_first_name.getText().toString().trim())) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Please enter a valid firstName.", "Continue", 0, 0, this);
                    } else if (TextUtils.isEmpty(et_last_name.getText().toString().trim())) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Please enter a valid lastName.", "Continue", 0, 0, this);
                    } else if (TextUtils.isEmpty(tv_gender.getText().toString().trim())) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Please select a gender.", "Continue", 0, 0, this);
                    } else if (TextUtils.isEmpty(et_email.getText().toString().trim())) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Please enter a valid e-mail.", "Continue", 0, 0, this);
                    } else if (!matcher.find()) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Please enter a valid e-mail.", "Continue", 0, 0, this);
                    } else if (TextUtils.isEmpty(tv_gender.getText().toString().trim())) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Please select a gender.", "Continue", 0, 0, this);
                    } else if (TextUtils.isEmpty(tv_country_code.getText().toString().trim())) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Please select a country code.", "Continue", 0, 0, this);
                    } else if (tv_country_code.getText().toString().trim().replace("+", "").equals("65") && !et_mobile.getText().toString().trim().matches(Constant.CheckSingaporeMobile)) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Please enter a valid mobile number.", "Continue", 0, 0, this);
                    } else if (et_mobile.getText().toString().trim().length() < 8) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Please enter a valid mobile number.", "Continue", 0, 0, this);
                    } else if (et_create_password.getText().toString().trim().length() < 6) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Please enter your password. (Password has to be a minimum of 6 characters).", "Continue", 0, 0, this);
                    } else if (et_confirm_password.getText().toString().trim().length() < 6) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Please re-enter your password. (Password has to be a minimum of 6 characters).", "Continue", 0, 0, this);
                    } else if (!et_create_password.getText().toString().trim().equals(et_confirm_password.getText().toString().trim())) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Password not match above.", "Continue", 0, 0, this);
                    } else if (!isAgree) {
                        DialogUtils.oneButtonDialog("Incomplete Submission", "Please read and agree to our Terms and Conditions before you submit the form.", "Continue", 0, 0, this);
                    }
                }
                break;
            case com.edenred.android.apps.avenesg.R.id.rl_gender:
                clearfocus();
                showPopWindow(0, 0, tv_gender, Constant.Gender);
                break;
            case com.edenred.android.apps.avenesg.R.id.rl_country_code:
                clearfocus();
                if (!sp.getBooleanValue(Constant.ISCOUNTRY)) {
                    MyAsy myAsy = new MyAsy();
                    myAsy.execute();
                } else {
                    ShowPopWindow.showPopWindow(PersonalDetailsEnterActivity.this, rl_country_code, 0, 0, tv_country_code, mlist, new ShowPopWindow.PopListener() {
                        @Override
                        public void getPopWindowPosition(View v, int pos, TextView tv) {

                        }
                    });
                }
                break;
            case com.edenred.android.apps.avenesg.R.id.rl_agree:
                clearfocus();
                if (!isAgree) {
                    iv_agree.setVisibility(View.VISIBLE);
                    btn_submit.setBackground(getResources().getDrawable(com.edenred.android.apps.avenesg.R.drawable.submit_dark_box));
                    btn_submit.setEnabled(true);
                    isAgree = true;
                } else {
                    iv_agree.setVisibility(View.GONE);
                    btn_submit.setBackground(getResources().getDrawable(com.edenred.android.apps.avenesg.R.drawable.submit_light_box));
                    btn_submit.setEnabled(false);
                    isAgree = false;
                }
                break;
            case com.edenred.android.apps.avenesg.R.id.tv_agree:
                break;
            default:
                break;
        }
    }

    public void clearfocus() {
        et_first_name.clearFocus();
        et_last_name.clearFocus();
        et_email.clearFocus();
        et_mobile.clearFocus();
        et_create_password.clearFocus();
        et_confirm_password.clearFocus();
        CloseKeyboard();
    }

    /**
     * popwindow
     *
     * @param x x位置
     * @param y y位置
     */
    public void showPopWindow(int x, int y, final TextView tv, String[] str) {
        CloseKeyboard();
        clearfocus();
        View popv1 = LayoutInflater.from(this).inflate(com.edenred.android.apps.avenesg.R.layout.pop, null);
        if (popSort == null) {
            popSort = new PopupWindow(popv1, tv.getWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        popSort.setFocusable(true);
        popSort.setBackgroundDrawable(new BitmapDrawable());
        popSort.setOutsideTouchable(true);
        popSort.update();
        popSort.showAsDropDown(tv, x, y);
        popSort.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                popSort = null;
            }
        });
        listpop.clear();
        for (int i = 0; i < str.length; i++) {
            StringBean db = new StringBean();
            db.text = str[i];
            listpop.add(db);
        }
        ListView listview = (ListView) popv1.findViewById(com.edenred.android.apps.avenesg.R.id.lv_choice);
        sortAdapter = new PopAdapter(this, listpop);
        listview.setAdapter(sortAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                tv.setText(listpop.get(position).text);
                popSort.dismiss();
            }
        });
    }

    class MyAsy extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... params) {
            String result = null;
            HttpUtils httpUtils = new HttpUtils();
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
//            if (currentFlag.equals("1")) {
//                hashMap.put("mobileCode", mareacode.replace("+", ""));
//                hashMap.put("mobileNumber", mphone);
//                hashMap.put("sendType", "2");
//                result = httpUtils.putParam(hashMap, Urls.SENDVERIFICATIONINFO);
//            } else {
            result = httpUtils.putParam(hashMap, Urls.GETCOUNTRYLIST);
//            }
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
                ErrorUtils.showErrorMsg(PersonalDetailsEnterActivity.this, "404");
                return;
            }
//            String currentFlag = getFlag();
//            if (currentFlag.equals("1")){
//                SendOTPXmlpull(result);
//            }else {
            CountryXmlPull(result);
//            }
        }
    }

    private void CountryXmlPull(String result) {
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
                                ErrorUtils.showErrorMsg(PersonalDetailsEnterActivity.this, code);
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

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
