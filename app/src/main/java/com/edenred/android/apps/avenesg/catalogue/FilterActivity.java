package com.edenred.android.apps.avenesg.catalogue;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.bean.RedeemGiftBean;
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
 * Created by zhaoxin on 2015/7/23.
 * 通过商品和积分筛选
 */
public class FilterActivity extends BaseActivity {

    private RelativeLayout rl_right, rl_filter_calalogue;
    private TextView tv_sum, tv_allpoint, tv_center;
    private GridView gridview;
    private GridviewAdapter adapter;
    private List<StringBean> list=new ArrayList<StringBean>();
    private int flag = 0, current = 0,carnum=0;
    private List<RedeemGiftBean> mlist = new ArrayList<RedeemGiftBean>();
    private DialogUtils.Listener listener,listener2;
    private boolean isFirstClick;
    private SharedPreferencesHelper sp;
    private String id = "";
    private List<ShoppingCarBean> shopCarList=new ArrayList<ShoppingCarBean>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_filter);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        initLogo();
        sp = AveneApplication.getInstance().getSp();
        flag = getIntent().getIntExtra(Constant.FLAG, 1);//flag  1  商品类别筛选  2  积分筛选
        mlist = (List<RedeemGiftBean>) getIntent().getSerializableExtra("list");
        initTitle("Rewards Catalogue");
        initView();
        initData();
    }

    private void initView() {
        rl_right = (RelativeLayout) findViewById(com.edenred.android.apps.avenesg.R.id.rl_right);//标题栏右边购物车
        tv_sum = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_sum);
        tv_allpoint = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_allpoint);
        tv_center = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_center);
        gridview = (GridView) findViewById(com.edenred.android.apps.avenesg.R.id.gridview);
        rl_filter_calalogue = (RelativeLayout) findViewById(com.edenred.android.apps.avenesg.R.id.rl_filter_calalogue);


        rl_right.setVisibility(View.VISIBLE);
        tv_sum.setVisibility(View.VISIBLE);

        rl_filter_calalogue.setOnClickListener(this);
        rl_right.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sp.getValue(Constant.ACCOUNTBALANCE) != null) {
            tv_allpoint.setText(getResources().getString(com.edenred.android.apps.avenesg.R.string.allpoint) +
                    sp.getValue(Constant.ACCOUNTBALANCE));
        }
        setTextSize(tv_allpoint.getText().toString(),
                tv_allpoint, 17, 16, tv_allpoint.getText().length());

        if(sp.getBooleanValue(Constant.ISSHOPCARLIST))
        {
            Gson gson = new Gson();
            shopCarList = gson.fromJson(sp.getValue(Constant.SHOPCARLIST),
                    new TypeToken<List<ShoppingCarBean>>() {
                    }.getType());
        }
        carnum = shopCarList.size();
        tv_sum.setText(String.valueOf(carnum));
        adapter.setUI(carnum,shopCarList);
    }

    private void initData() {

        adapter = new GridviewAdapter(this, tv_sum, mlist);

        gridview.setAdapter(adapter);

        if (flag == 2) {
            Drawable db = getResources().getDrawable(com.edenred.android.apps.avenesg.R.mipmap.point_icon1);
            db.setBounds(0, 0, db.getMinimumWidth(), db.getMinimumHeight());
            tv_center.setCompoundDrawables(db, null, null, null);
            tv_center.setCompoundDrawablePadding(16);
            id = "3000";
            MyAsy myAsy = new MyAsy();
            myAsy.setFlag("3");
            myAsy.execute();
            listener2=new DialogUtils.Listener() {

                @Override
                public void getPosition(int pos) {
                    current = pos;
                    id = list.get(pos).id;
                    MyAsy myAsy = new MyAsy();
                    myAsy.setFlag("3");
                    myAsy.execute();
                }
            };
            for (int i = 0; i < 2; i++) {
                StringBean bean=new StringBean();
                if(i==0)
                {
                    bean.id="3000";
                    bean.text="3000";
                }else
                {
                    bean.id="5000";
                    bean.text="5000";
                }
                list.add(bean);
            }
            tv_center.setText(list.get(0).text);
            DialogUtils.FilterDlg(FilterActivity.this, list, tv_center, current, listener2);
        } else {
            MyAsy myAsy = new MyAsy();
            myAsy.setFlag("0");
            myAsy.execute();
        }

        //如果进来没有拿到兑换礼品列表数据则请求接口数据
        if (mlist.size() <= 0) {
            MyAsy myAsy = new MyAsy();
            myAsy.setFlag("1");
            myAsy.execute();
        }
        listener = new DialogUtils.Listener() {
            @Override
            public void getPosition(int pos) {
                current = pos;
//                id = String.valueOf(pos + 1);
                id = list.get(pos).id;
                MyAsy myAsy = new MyAsy();
                myAsy.setFlag("2");
                myAsy.execute();
            }
        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case com.edenred.android.apps.avenesg.R.id.rl_filter_calalogue:
                if(flag==1)
                {
                    if (list != null) {
                        DialogUtils.FilterDlg(this, list, tv_center, current, listener);
                    } else if (isFirstClick) {
                        isFirstClick = false;
                        MyAsy myAsy = new MyAsy();
                        myAsy.setFlag("0");
                        myAsy.execute();
                    }
                }else
                {
                    DialogUtils.FilterDlg(this, list, tv_center, current, listener2);
                }
                break;
            case com.edenred.android.apps.avenesg.R.id.rl_right:
                gotoOtherActivity(MyRewardActivity.class);
                break;
            default:
                break;
        }
    }


    //Object, Object, String调用参数，进度和结果
    class MyAsy extends AsyncTask<Object, Integer, String> {

        public String flag; // 0--获取礼品分类列表    1--获取兑换礼品列表
                            // 2--分类筛选后兑换礼品列表  3--积分筛选后兑换礼品列表

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
            HttpUtils httpUtils = new HttpUtils();
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            if (getFlag().equals("2")) {
//                System.out.println("mlist.size() = " + mlist.size());
//                System.out.println("current =" + current);
//                //hashMap.put("redeemGiftCategory", mlist.get(current).productCategroyId);
//                // 设置固定值 因为mList有问题 需要确定问题 @Test By Jason 09.30
                hashMap.put("redeemGiftCategory", id);
            }
            if(getFlag().equals("3"))
            {
                hashMap.put("points", id);
            }
            if (getFlag().equals("0")) {
                result = httpUtils.putParam(hashMap, Urls.REWARDCATEGORY);
            } else {
                result = httpUtils.putParam(hashMap, Urls.GETREDEEMGIFTLIST);
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
            isFirstClick = true;
            if (s.equals("error")) {
                ErrorUtils.showErrorMsg(FilterActivity.this, "404");
                return;
            } else if (getFlag().equals("0")) {
                XmlPull(s);
            } else {
                RedeemXmlPull(s);
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
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        list.clear();
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(FilterActivity.this, code);
                                return;
                            }
                        }
                        if (name.equals("productCategoryList")) {
                            db = new StringBean();
                        } else if (db != null) {
                            if (name.equals("productCategoryId")) {
                                db.id = parser.nextText();// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("productCategoryName")) {
                                db.text = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        //读完一个productCategoryList，可以将其添加到集合类中
                        if (parser.getName().equals("productCategoryList") && db != null) {
                            list.add(db);
                            db = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            if (list.size() > 0) {
                tv_center.setText(list.get(0).text);
                DialogUtils.FilterDlg(FilterActivity.this, list, tv_center, current, listener);
                id = list.get(0).id;
                MyAsy myAsy = new MyAsy();
                myAsy.setFlag("2");
                myAsy.execute();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
                            if(!code.equals("0"))
                            {
                                ErrorUtils.showErrorMsg(FilterActivity.this, code);
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
            adapter.notifyDataSetChanged();
            if(mlist.size()<=0)
            {
                Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
