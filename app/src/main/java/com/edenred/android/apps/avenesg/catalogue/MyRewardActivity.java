package com.edenred.android.apps.avenesg.catalogue;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.bean.ShoppingCarBean;
import com.edenred.android.apps.avenesg.bean.StringBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.DialogUtils;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
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

/**
 * Created by zhaoxin on 2015/7/27.
 * 我的兑换
 */
public class MyRewardActivity extends BaseActivity {

    private TextView tv_allpoint, tv_item, tv_allnum,
            tv_checkout, tv_shengyu, tv_cancel, tv_continue;
    private ListView lv_reward;
    private MyRewardAdapter adapter;
    private List<ShoppingCarBean> list = new ArrayList<ShoppingCarBean>();
    private int allnum = 0, test = 0, listsize = 0, shengyu = 0;
    private SharedPreferencesHelper sp;
    private String orderNo="",accountBalance="",pointsEarned="",pointsRedemed="",pointsExpired="",willExpiringNextMon="";
    private String collection="",contact="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_myreward);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        initLogo();
        sp = AveneApplication.getInstance().getSp();
        initTitle("My Rewards");
        collection=AveneApplication.getInstance().dialogBean.redemption.collection;
        contact=AveneApplication.getInstance().dialogBean.redemption.contact;
        initView();
        initData();
    }

    private void initView() {
        tv_allpoint = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_allpoint);
        lv_reward = (ListView) findViewById(com.edenred.android.apps.avenesg.R.id.lv_reward);
        tv_item = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_item);
        tv_allnum = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_allnum);
        tv_checkout = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_checkout);
        tv_shengyu = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_shengyu);
        tv_cancel = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_cancel);
        tv_continue = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_continue);

        tv_continue.setOnClickListener(this);
        tv_checkout.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

    }

    private void initData() {
        setTitlePoint();

        Log.e("list", sp.getValue(Constant.SHOPCARLIST));
        if (sp.getBooleanValue(Constant.ISSHOPCARLIST)) {
            Gson gson = new Gson();
            list = gson.fromJson(sp.getValue(Constant.SHOPCARLIST),
                    new TypeToken<List<ShoppingCarBean>>() {
                    }.getType());
        }

        listsize = list.size();
        for (int i = 0; i < listsize; i++) {
            allnum += Integer.parseInt(list.get(i).pointfirst);
        }

        adapter = new MyRewardAdapter(this, list, allnum);
        lv_reward.setAdapter(adapter);

        resetpoint(allnum, listsize);
    }


    public void resetpoint(int num, int listsize1) {
        tv_allnum.setText(String.valueOf(num));
        setButtonColor(num);
        tv_item.setText(String.valueOf(listsize1));

        listsize = listsize1;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setButtonColor(int num) {
        if (test < num) {
            tv_checkout.setBackground(getResources().getDrawable(com.edenred.android.apps.avenesg.R.drawable.grey_light_box));
            tv_checkout.setEnabled(false);
            tv_shengyu.setText("0");
            shengyu = 0;
        } else {
            tv_shengyu.setText(String.valueOf(test - num));
            shengyu = test - num;
            tv_checkout.setEnabled(true);
            tv_checkout.setBackground(getResources().getDrawable(com.edenred.android.apps.avenesg.R.drawable.grey_dark_box));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case com.edenred.android.apps.avenesg.R.id.tv_continue:
                AveneApplication.getInstance().finishActivity();
                gotoOtherActivity(CatalogueActivity.class);
                break;
            case com.edenred.android.apps.avenesg.R.id.tv_checkout:
                if (listsize <= 0) {
                    Toast.makeText(this, "Choose at least one", Toast.LENGTH_SHORT).show();
                } else {
                    showPD("Upload...");
                    MyAsy myAsy=new MyAsy();
                    myAsy.setFlag("0");
                    myAsy.execute();
                }

                break;
            case com.edenred.android.apps.avenesg.R.id.tv_cancel:
                list.clear();
                adapter.notifyDataSetChanged();
                sp.putValue(Constant.SHOPCARLIST, new Gson().toJson(list));
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private void setTitlePoint() {
        if (sp.getValue(Constant.ACCOUNTBALANCE) != null) {
            tv_allpoint.setText(getResources().getString(com.edenred.android.apps.avenesg.R.string.allpoint) +
                    sp.getValue(Constant.ACCOUNTBALANCE));
            test = Integer.parseInt(sp.getValue(Constant.ACCOUNTBALANCE));
        }
        setTextSize(tv_allpoint.getText().toString(),
                tv_allpoint, 17, 16, tv_allpoint.getText().length());
    }


    //Object, Object, String调用参数，进度和结果
    class MyAsy extends AsyncTask<Object, Integer, String> {

        public String flag; // 0--保存兑换信息    1--获取会员信息拿到积分

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        //执行耗时操作
        @Override
        protected String doInBackground(Object... params) {
            //请求接口
            String result = null;
            String str = "";
            HttpUtils httpUtils = new HttpUtils();
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            hashMap.put("accountId", sp.getValue(Constant.ACCOUNTID));
            if(getFlag().equals("0"))
            {
                hashMap.put("infoChannelId", "1");
                for (int i = 0; i < list.size(); i++) {
                    str += "<redemptionInfoList>\n" +
                            "\t\t\t\t\t<articleId>" + list.get(i).id + "</articleId>\n" +
                            "\t\t\t\t\t<articleQuantity>" + list.get(i).num + "</articleQuantity>\n" +
                            "\t\t\t\t</redemptionInfoList>\n";
                }
                hashMap.put("list", str);
                result = httpUtils.putParam(hashMap, Urls.SAVEREDEMPTIONINFO);
            }else
            {
                result = httpUtils.putParam(hashMap, Urls.GETMEMBERINFO);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("error")) {
                cancelPD();
                ErrorUtils.showErrorMsg(MyRewardActivity.this, "404");
                return;
            }

            if(getFlag().equals("0"))
            {
                XmlPull(s);
            }else
            {
                cancelPD();
                getMemberXmlpull(s);
            }

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

            StringBean db = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            if (code.equals("-14")) {
//                                ErrorUtils.showErrorMsg(MyRewardActivity.this, code);
                                cancelPD();
                                Toast.makeText(MyRewardActivity.this,"Gift inventory shortage",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }else if(name.equals("orderNo"))
                        {
                            orderNo=parser.nextText();
                        }
                        break;
                }
                eventType = parser.next();
            }

            MyAsy myAsy1=new MyAsy();
            myAsy1.setFlag("1");
            myAsy1.execute();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
                                ErrorUtils.showErrorMsg(MyRewardActivity.this, code);
                                return;
                            }
                        }else if(name.equals("accountBalance"))
                        {
                            accountBalance=parser.nextText();
                        }else if(name.equals("pointsEarned"))
                        {
                            pointsEarned=parser.nextText();
                        }else if(name.equals("pointsRedemed"))
                        {
                            pointsRedemed=parser.nextText();
                        }else if(name.equals("pointsExpired"))
                        {
                            pointsExpired=parser.nextText();
                        }else if(name.equals("willExpiringNextMon"))
                        {
                            willExpiringNextMon=parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        break;
                }
                eventType = parser.next();
            }
            tv_allpoint.setText(getResources().getString(com.edenred.android.apps.avenesg.R.string.allpoint) +
                    accountBalance);
            setTextSize(tv_allpoint.getText().toString(),
                    tv_allpoint, 17, 16, tv_allpoint.getText().length());
            tv_allnum.setText("0");
            tv_item.setText("0");
            list.clear();
            adapter.notifyDataSetChanged();
            sp.putValue(Constant.SHOPCARLIST, new Gson().toJson(list));

            sp.putValue(Constant.ACCOUNTBALANCE, accountBalance);
            sp.putValue(Constant.EARNED, pointsEarned);
            sp.putValue(Constant.REDEMEED, pointsRedemed);
            sp.putValue(Constant.EXPIRED, pointsExpired);
            sp.putValue(Constant.WILLEXPIRINGNEXTMON, willExpiringNextMon);
            DialogUtils.ProfileDlg(this, "Thank you for \n  your redemption",
                    getResources().getString(com.edenred.android.apps.avenesg.R.string.test1) +
                            orderNo + "\n\nCollection Point:\n" + collection + getResources().getString(com.edenred.android.apps.avenesg.R.string.test2) + contact, 5, 0);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
