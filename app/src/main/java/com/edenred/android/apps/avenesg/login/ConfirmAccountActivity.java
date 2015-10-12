package com.edenred.android.apps.avenesg.login;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.bean.AccountInfoBean;
import com.edenred.android.apps.avenesg.bean.StringBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.DialogUtils;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.edenred.android.apps.avenesg.view.HorizontalListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangwm on 2015/7/20.
 * 确认账户
 */
public class ConfirmAccountActivity extends BaseActivity {
    private EditText et_enter_OTP;
    private RelativeLayout rl_agree_email;
    private ImageView iv_agree_email, iv_agree_SMS;
    private RelativeLayout rl_agree_SMS;
    private TextView btn_submit, tv_send_OTP, tv_bottom_info;
    private boolean isAgree1, isAgree2;
    private HorizontalListView hlv_guide;
    private ConfirmAdapter adapter;
    private String one = "1", two = "1", code = "", id = "0", mareacode, mphone;
    private SharedPreferencesHelper sp;
    private List<StringBean> mlist = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_confirm_account);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        sp = AveneApplication.getInstance().getSp();
        AveneApplication.getInstance().addActivity(this);
        initView();
        initData();
    }

    private void initView() {
        initTitle("Registration Process");
        FontManager.applyFont(this, findViewById(R.id.tv_bottom_info), Constant.ITTFNAME);
        hlv_guide = (HorizontalListView) findViewById(R.id.hlv_guide);
        // 提交按钮
        btn_submit = (TextView) findViewById(R.id.btn_submit);
        // 发送验证码
        tv_send_OTP = (TextView) findViewById(R.id.tv_send_OTP);
        // 输入验证码
        et_enter_OTP = (EditText) findViewById(R.id.et_enter_OTP);
        // 同意发送到Email
        rl_agree_email = (RelativeLayout) findViewById(R.id.rl_agree_email);
        iv_agree_email = (ImageView) findViewById(R.id.iv_agree_email);
        // 同意发送到SMS
        rl_agree_SMS = (RelativeLayout) findViewById(R.id.rl_agree_SMS);
        iv_agree_SMS = (ImageView) findViewById(R.id.iv_agree_SMS);

        adapter = new ConfirmAdapter(this);
        hlv_guide.setAdapter(adapter);
        btn_submit.setOnClickListener(this);
        tv_send_OTP.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_send_OTP.setOnClickListener(this);
        rl_agree_email.setOnClickListener(this);
        rl_agree_SMS.setOnClickListener(this);

    }

    private void initData() {
        mareacode = AveneApplication.getInstance().memberinfo.mobileCode;
        mphone = AveneApplication.getInstance().memberinfo.mobileNumber;
        UpdateAsy updateAsy = new UpdateAsy();
        updateAsy.setFlag("1");
        updateAsy.execute();
        Gson gson = new Gson();
        mlist = gson.fromJson(sp.getValue(Constant.COUNTRYLIST),
                new TypeToken<List<StringBean>>() {
                }.getType());
    }

    public boolean canSubmit() {
        if (TextUtils.isEmpty(et_enter_OTP.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.tv_send_OTP:
                clearfocus();
                DialogUtils.HttpListener listener = new DialogUtils.HttpListener() {
                    @Override
                    public void sendPassword(String areacode, String phone) {
                        mareacode = areacode;
                        mphone = phone;
                        UpdateAsy myAsy = new UpdateAsy();
                        myAsy.setFlag("1");
                        myAsy.execute();
                    }

                    @Override
                    public void getHttp(View v, TextView tv) {
                    }
                };
                DialogUtils.withMobileDialog("One-Time Password Error",
                        "Please re-enter your mobile number.",
                        AveneApplication.getInstance().memberinfo.mobileCode,
                        AveneApplication.getInstance().memberinfo.mobileNumber,
                        "Submit", this, mlist, listener);
                break;

            case R.id.btn_submit:
                if (canSubmit()) {
                    String ss = et_enter_OTP.getText().toString();
//                    String ss1 = null;
//                    if (!TextUtils.isEmpty(code)) {
//                        ss1 = code.substring(code.length() - 4, code.length());
//                    }
                    if (TextUtils.isEmpty(code) || !ss.equalsIgnoreCase(code)) {
                        Toast.makeText(this, "Please enter correct One-Time Password. If you have not received the SMS, please click above to resend a One-Time Password.", Toast.LENGTH_LONG).show();
                    } else {
                        showPD("Upload...");
                        UpdateAsy updateAsy = new UpdateAsy();
                        updateAsy.setFlag("2");
                        updateAsy.execute();
                    }
                } else {
                    // 没填写验证码
                    Toast.makeText(this, "Please enter One-Time Password.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.rl_agree_email:
                clearfocus();
                if (!isAgree1) {
                    iv_agree_email.setVisibility(View.GONE);
                    isAgree1 = true;
                    one = "1";
                } else {
                    iv_agree_email.setVisibility(View.VISIBLE);
                    isAgree1 = false;
                    one = "0";
                }
                break;
            case R.id.rl_agree_SMS:
                clearfocus();
                if (!isAgree2) {
                    iv_agree_SMS.setVisibility(View.GONE);
                    isAgree2 = true;
                    two = "1";
                } else {
                    iv_agree_SMS.setVisibility(View.VISIBLE);
                    isAgree2 = false;
                    two = "0";
                }
                break;
            default:
                break;
        }
    }

    public void clearfocus() {
        et_enter_OTP.clearFocus();
        CloseKeyboard();
    }

    class UpdateAsy extends AsyncTask<Object, Integer, String> {
        //执行耗时操作
        public String flag = ""; // 1--发送验证码接口  2  确认注册   3  登录接口

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        @Override
        protected String doInBackground(Object... params) {
            //请求接口
            String currentFlag = getFlag();
            String result = null;
            String sexId;
            HttpUtils httpUtils = new HttpUtils();
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            if (currentFlag.equals("1")) {
                hashMap.put("mobileCode", mareacode.replace("+", ""));
                hashMap.put("mobileNumber", mphone);
                hashMap.put("sendType", "2");
                result = httpUtils.putParam(hashMap, Urls.SENDVERIFICATIONINFO);
            } else if (currentFlag.equals("2")) {
                hashMap.put("infoChannelId", "1");
                hashMap.put("isAcceptSMS", two);
                hashMap.put("isAcceptEMail", one);
                hashMap.put("isAcceptPushInfo", "1");
                hashMap.put("firstName", AveneApplication.getInstance().memberinfo.firstName);
                hashMap.put("lastName", AveneApplication.getInstance().memberinfo.lastName);
                if (AveneApplication.getInstance().memberinfo.sexId.equals("Female")) {
                    sexId = "0";
                } else {
                    sexId = "1";
                }
                hashMap.put("sexId", sexId);
                hashMap.put("email", AveneApplication.getInstance().memberinfo.email);
                hashMap.put("favorCounterId", AveneApplication.getInstance().PurchaseCounterId);
                hashMap.put("memberInfoRatio", "0.3");
                hashMap.put("accountId", "");
                hashMap.put("mobileCode", AveneApplication.getInstance().memberinfo.mobileCode.replace("+", ""));
                hashMap.put("mobileNumber", AveneApplication.getInstance().memberinfo.mobileNumber);
                hashMap.put("password", AveneApplication.getInstance().memberinfo.password);
                hashMap.put("registerUniqueCode", AveneApplication.getInstance().purchaseCode);
                String str = "\n<productId>" + AveneApplication.getInstance().productbean.productId + "</productId>\n" +
                        "\t\t\t\t\t<purchaseCode>" + AveneApplication.getInstance().purchaseCode + "</purchaseCode>\n" +
                        "\t\t\t\t\t<purchaseCounterId>" + AveneApplication.getInstance().PurchaseCounterId + "</purchaseCounterId>\n" +
                        "\t\t\t\t\t<purchaseDate>" + AveneApplication.getInstance().memberinfo.date + "</purchaseDate>\n";
                hashMap.put("transactionBO", str);

                result = httpUtils.putParam(hashMap, Urls.UPDATEMEMBERINFO);
            } else {
                hashMap.put("mobileCode", AveneApplication.getInstance().memberinfo.mobileCode.replace("+", ""));
                hashMap.put("mobileNumber", AveneApplication.getInstance().memberinfo.mobileNumber);
                hashMap.put("password", AveneApplication.getInstance().memberinfo.password);
                result = httpUtils.putParam(hashMap, Urls.MEMBERLOGIN);
            }
            return result;
        }

        //通知进度改变
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //doInBackground执行完成之后执行结果处理  可以更新UI
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cancelPD();
            if (result.equals("error")) {
                ErrorUtils.showErrorMsg(ConfirmAccountActivity.this, "404");
                return;
            }
            String currentFlag = getFlag();
            if (currentFlag.equals("1")) {
                SendOTPXmlpull(result);
            } else if (currentFlag.equals("2")) {
                UpdateXmlPull(result);
            } else {
                MemberXmlPull(result);
            }
        }
    }

    private void SendOTPXmlpull(String result) {
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
//                                ErrorUtils.showErrorMsg(ConfirmAccountActivity.this, code);
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

                        } else if (name.equals("messageContent")) {
                            code = parser.nextText();
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

    private void UpdateXmlPull(String result) {
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
                            if (code.equals("-13")) {
//                                ErrorUtils.showErrorMsg(ConfirmAccountActivity.this, code);
                                Toast.makeText(ConfirmAccountActivity.this, "Mobile has been registered", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if (name.equals("accountId")) {
                            id = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        break;
                }
                eventType = parser.next();
            }

            UpdateAsy asy = new UpdateAsy();
            asy.setFlag("3");
            asy.execute();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void MemberXmlPull(String result) {
        //解析返回数据
        XmlPullParserFactory factory = null;
        String id = "";
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
                                return;
                            }
                        }
                        if (name.equals("accountInfoPO")) {
                            db = new AccountInfoBean();
                        } else if (db != null) {
                            if (name.equals("accountId")) {
                                id = parser.nextText();
                                db.accountId = id;// 如果后面是Text元素,即返回它的值
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
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equals("accountInfoPO") && db != null) {

                            if (!sp.getValue(Constant.ACCOUNTID).equals(id)) {
                                sp.putBooleanValue(Constant.ISSHOPCARLIST, false);
                                sp.putValue(Constant.SHOPCARLIST, "");
                            }
                            sp.putValue(Constant.ACCOUNTBALANCE, db.accountBalance);
                            sp.putValue(Constant.EARNED, db.pointsEarned);
                            sp.putValue(Constant.ACCOUNTID, db.accountId);
                            sp.putValue(Constant.REDEMEED, db.pointsRedemed);
                            sp.putValue(Constant.EXPIRED, db.pointsExpired);
                            sp.putValue(Constant.LASTNAME, AveneApplication.getInstance().memberinfo.lastName);
                            sp.putValue(Constant.FIRSTNAME, AveneApplication.getInstance().memberinfo.firstName);
                            sp.putValue(Constant.WILLEXPIRINGNEXTMON, db.willExpiringNextMon);
                        }
                        break;
                }
                eventType = parser.next();
            }

            //得到数据后操作
            sp.putBooleanValue(Constant.ISLOGIN, true);
            sp.putValue(Constant.AREACODE, AveneApplication.getInstance().memberinfo.mobileCode);
            sp.putValue(Constant.PHONE, AveneApplication.getInstance().memberinfo.mobileNumber);
            sp.putValue(Constant.PASSWORD, AveneApplication.getInstance().memberinfo.password);
            DialogUtils.congratulationDialog(this);


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
