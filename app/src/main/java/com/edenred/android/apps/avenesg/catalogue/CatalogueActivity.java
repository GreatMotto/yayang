package com.edenred.android.apps.avenesg.catalogue;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.bean.RedeemGiftBean;
import com.edenred.android.apps.avenesg.bean.ShoppingCarBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.DisplayUtil;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.utils.NumbersFormat;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by zhaoxin on 2015/7/22.
 * 购物车主页
 */
public class CatalogueActivity extends BaseActivity {

    private RelativeLayout rl_right, rl_filter_calalogue, rl_filter_point;
    private TextView tv_sum, tv_allpoint;
    private RecyclerView recyclerView;
    private RecylerAdapter recylerAdapter;
    private List<RedeemGiftBean> mlist = new ArrayList<RedeemGiftBean>();
    private List<ShoppingCarBean> shopCarList = new ArrayList<ShoppingCarBean>();
    private SharedPreferencesHelper sp;
    private int tag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_catalogue);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        tag = getIntent().getIntExtra(Constant.TAG, 0);
        if (tag == 8) {
            initLogo();
        } else {
            initLogo2();
        }
        sp = AveneApplication.getInstance().getSp();
        AveneApplication.getInstance().addActivity(this);
        initTitle("Rewards Catalogue");
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sp.getValue(Constant.ACCOUNTBALANCE) != null) {
            tv_allpoint.setText(getResources().getString(com.edenred.android.apps.avenesg.R.string.allpoint) +
                    NumbersFormat.thousand(sp.getValue(Constant.ACCOUNTBALANCE)));
        }
        setTextSize(tv_allpoint.getText().toString(),
                tv_allpoint, 17, 16, tv_allpoint.getText().length());

        if (sp.getBooleanValue(Constant.ISSHOPCARLIST)) {
            Gson gson = new Gson();
            shopCarList = gson.fromJson(sp.getValue(Constant.SHOPCARLIST),
                    new TypeToken<List<ShoppingCarBean>>() {
                    }.getType());
        }
        int carnum = shopCarList.size();
        tv_sum.setText(String.valueOf(carnum));
        recylerAdapter.setUI(tv_sum, carnum, shopCarList);

    }

    private void initView() {
        rl_right = (RelativeLayout) findViewById(com.edenred.android.apps.avenesg.R.id.rl_right);//标题栏右边购物车
        tv_sum = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_sum);
        tv_allpoint = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_allpoint);
        rl_filter_calalogue = (RelativeLayout) findViewById(com.edenred.android.apps.avenesg.R.id.rl_filter_calalogue);
        rl_filter_point = (RelativeLayout) findViewById(com.edenred.android.apps.avenesg.R.id.rl_filter_point);

        recyclerView = (RecyclerView) findViewById(com.edenred.android.apps.avenesg.R.id.recyclerView);

        rl_right.setVisibility(View.VISIBLE);
        tv_sum.setVisibility(View.VISIBLE);

        rl_filter_calalogue.setOnClickListener(this);
        rl_filter_point.setOnClickListener(this);
        rl_right.setOnClickListener(this);


    }

    private void initData() {

        recylerAdapter = new RecylerAdapter(this, DisplayUtil.getWidth(this) * 5 / 11, 2, tag, mlist);
        recyclerView.setAdapter(recylerAdapter);


        recyclerView.setHasFixedSize(true);
        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // 设置布局管理器
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        MyAsy myAsy = new MyAsy();
        myAsy.execute();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case com.edenred.android.apps.avenesg.R.id.rl_filter_calalogue:
//                goto1OtherActivity(FilterActivity.class, 1);
                gotoFiterActivity(1, tag);
                break;
            case com.edenred.android.apps.avenesg.R.id.rl_filter_point:
                gotoFiterActivity(2, tag);
                break;
            case com.edenred.android.apps.avenesg.R.id.rl_right:
                goto1AnotherActivity(MyRewardActivity.class, tag);
                break;
            default:
                break;
        }
    }

    private void gotoFiterActivity(int flag, int tag) {
        Intent intent = new Intent();
        intent.putExtra(Constant.FLAG, flag);
        intent.putExtra(Constant.TAG, tag);
        intent.putExtra("list", (Serializable) mlist);
        intent.setClass(this, FilterActivity.class);
        startActivity(intent);
    }

    class MyAsy extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... params) {
            HttpUtils httpUtils = new HttpUtils();
            String result = null;
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            result = httpUtils.putParam(hashMap, Urls.GETREDEEMGIFTLIST);
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
                ErrorUtils.showErrorMsg(CatalogueActivity.this, "404");
                return;
            }
            RedeemXmlPull(result);
        }
    }

    // 解析兑换礼品列表返回数据
    private void RedeemXmlPull(String result) {
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();
            RedeemGiftBean db = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        mlist.clear();
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(CatalogueActivity.this, code);
                                return;
                            }
                        }
                        if (name.equals("redeemGiftList")) {
                            db = new RedeemGiftBean();
                        } else if (db != null) {
                            if (name.equals("productId")) {
                                db.productId = parser.nextText();// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("articleName")) {
                                db.articleName = parser.nextText();
                            } else if (name.equalsIgnoreCase("articleDesc")) {
                                db.articleDesc = parser.nextText();
                            } else if (name.equalsIgnoreCase("productImageURL")) {
                                db.productImageURL = parser.nextText();
                            } else if (name.equalsIgnoreCase("articlePoint")) {
                                db.articlePoint = parser.nextText();
                            } else if (name.equalsIgnoreCase("articleId")) {
                                db.articleId = parser.nextText();
                            } else if (name.equalsIgnoreCase("productCategroyId")) {
                                db.productCategroyId = parser.nextText();
                            } else if (name.equalsIgnoreCase("rewardCatalogueFlag")) {
                                db.rewardCatalogueFlag = parser.nextText();
                            } else if (name.equalsIgnoreCase("rewardCatalogueOrderBy")) {
                                db.rewardCatalogueOrderBy = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equals("redeemGiftList") && db != null) {
                            mlist.add(db);
                            db = null;
                        }

                        break;
                }
                eventType = parser.next();
            }

            if (mlist.size() <= 0) {
                Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            }
            recylerAdapter.notifyDataSetChanged();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
