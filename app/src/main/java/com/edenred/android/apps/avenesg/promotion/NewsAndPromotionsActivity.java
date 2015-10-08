package com.edenred.android.apps.avenesg.promotion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.bean.NotifyMessageBean;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangwm on 2015/7/27.
 * 新闻和促销信息
 */
public class NewsAndPromotionsActivity extends BaseActivity {
    private GridView gv_detail;
    private TextView tv_allpoint;
    private PromotionListAdapter adapter;
    private List<NotifyMessageBean> mlist1;
    private SharedPreferencesHelper sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_promotion_and_product);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        initLogo();
        sp= AveneApplication.getInstance().getSp();
        initTitle("News and Promotions");
        initView();
        initData();
    }

    private void initView() {
        gv_detail = (GridView) findViewById(com.edenred.android.apps.avenesg.R.id.gv_detail);
        tv_allpoint = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_allpoint);
    }

    private void initData() {
        if (sp.getValue(Constant.ACCOUNTBALANCE) != null) {
            tv_allpoint.setText(getResources().getString(com.edenred.android.apps.avenesg.R.string.allpoint) +
                    sp.getValue(Constant.ACCOUNTBALANCE));
        }
        setTextSize(tv_allpoint.getText().toString(),
                tv_allpoint, 17, 16, tv_allpoint.getText().length());

        MyAsy1 myAsy1 = new MyAsy1();
        myAsy1.execute();
    }

    class MyAsy1 extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... params) {
            HttpUtils httpUtils = new HttpUtils();
            String result = null;
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("accountId", sp.getValue(Constant.ACCOUNTID));
            hashMap.put("securityKey", "abc123$");
            result = httpUtils.putParam(hashMap, Urls.GETNOTIFYMESSAGELIST);
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
                ErrorUtils.showErrorMsg(NewsAndPromotionsActivity.this, "404");
                return;
            }
            NewsXmlPull(result);
        }
    }

    // 解析消息推送列表返回数据
    private void NewsXmlPull(String result) {
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();
            NotifyMessageBean db = null;
            mlist1 = new ArrayList<NotifyMessageBean>();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        mlist1.clear();
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            if(!code.equals("0"))
                            {
                                ErrorUtils.showErrorMsg(NewsAndPromotionsActivity.this, code);
                                return;
                            }
                        }
                        if (name.equals("notifyMessageList")) {
                            db = new NotifyMessageBean();
                        } else if (db != null) {
                            if (name.equals("messageId")) {
                                db.messageId = parser.nextText();// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("messageTitle")) {
                                db.messageTitle = parser.nextText();
                            } else if (name.equalsIgnoreCase("sendMessageDate")) {
                                db.sendMessageDate = parser.nextText();
                            } else if (name.equalsIgnoreCase("messageContent")) {
                                db.messageContent = parser.nextText();
                            } else if (name.equalsIgnoreCase("messageImageUrl")) {
                                db.messageImageUrl = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equals("notifyMessageList") && db != null) {
                            mlist1.add(db);
                            db = null;
                        }

                        break;
                }
                eventType = parser.next();
            }

            adapter = new PromotionListAdapter(this, mlist1);
            gv_detail.setAdapter(adapter);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
