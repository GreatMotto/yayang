package com.edenred.android.apps.avenesg.product;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.bean.ProductBean;
import com.edenred.android.apps.avenesg.bean.StringBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.DialogUtils;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.utils.NumbersFormat;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;

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
 * 产品列表页面
 */
public class ProductsActivity extends BaseActivity {
    private RelativeLayout rl_filter;
    private GridView gv_detail;
    private TextView tv_center,tv_allpoint;
    private ProductsListAdapter adapter;
    private List<StringBean> list = new ArrayList<StringBean>();
    private List<ProductBean> plist=new ArrayList<ProductBean>();
    private int current = 0;
    private String id="1";
    private DialogUtils.Listener listener;
    private SharedPreferencesHelper sp;
    private boolean isFirstClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.edenred.android.apps.avenesg.R.layout.ac_promotion_and_product);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        initLogo();
        sp= AveneApplication.getInstance().getSp();
        initTitle("Products");
        initView();
        initData();
        MyAsy myAsy=new MyAsy();
        myAsy.setFlag("0");
        myAsy.execute();
    }

    private void initView() {
        tv_allpoint= (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_allpoint);
        rl_filter = (RelativeLayout) findViewById(com.edenred.android.apps.avenesg.R.id.rl_filter);
        rl_filter.setVisibility(View.VISIBLE);
        rl_filter.setOnClickListener(this);

        tv_center = (TextView) findViewById(com.edenred.android.apps.avenesg.R.id.tv_center);

        gv_detail = (GridView) findViewById(com.edenred.android.apps.avenesg.R.id.gv_detail);
    }

    private void initData() {

        if(sp.getValue(Constant.ACCOUNTBALANCE)!=null)
        {
            tv_allpoint.setText(getResources().getString(com.edenred.android.apps.avenesg.R.string.allpoint)+
                    NumbersFormat.thousand(sp.getValue(Constant.ACCOUNTBALANCE)));
        }
        //改变字体大小
        setTextSize(tv_allpoint.getText().toString(),
                tv_allpoint, 17, 16, tv_allpoint.getText().length());
        adapter = new ProductsListAdapter(this,plist);
        gv_detail.setAdapter(adapter);



        listener = new DialogUtils.Listener() {
            @Override
            public void getPosition(int pos) {
                current = pos;
                id=String.valueOf(pos+1);
                MyAsy myAsy=new MyAsy();
                myAsy.setFlag("0");
                myAsy.execute();
            }
        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case com.edenred.android.apps.avenesg.R.id.rl_filter:
                if(list.size()<=0&&!isFirstClick)
                {
                    isFirstClick=true;
                    MyAsy myAsy=new MyAsy();
                    myAsy.setFlag("1");
                    myAsy.execute();
                }else
                {
                    DialogUtils.FilterDlg(this, list, tv_center, current, listener, 0);
                }
                break;
            default:
                break;
        }
    }

    //Object, Object, String调用参数，进度和结果
    class MyAsy extends AsyncTask<Object, Integer, String> {


        public String flag;//0  请求产品列表   1  获取分类列表

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
            String result=null;
            HttpUtils httpUtils = new HttpUtils();
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            if (getFlag().equals("0"))
            {
                hashMap.put("productType",id);
                result= httpUtils.putParam(hashMap, Urls.GETPRODUCTLIST);
            }else
            {
                result= httpUtils.putParam(hashMap, Urls.PRODUCTCATEGORY);
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
            if(s.equals("error"))
            {
                ErrorUtils.showErrorMsg(ProductsActivity.this, "404");
                isFirstClick=false;
                return;
            }
            if(getFlag().equals("0"))
            {
                XmlPull(s);
            }else
            {
                ChoiceXmlPull(s);
            }

        }
    }

    private void XmlPull(String s)
    {
        //解析返回数据
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(s));
            int eventType = parser.getEventType();

            ProductBean db=null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        plist.clear();
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if(name.equals("exitCode"))
                        {
                            String code=parser.nextText();
                            Log.e("exitCode", code);

                            if(!code.equals("0"))
                            {
                                isFirstClick=false;
                                ErrorUtils.showErrorMsg(ProductsActivity.this, code);
                                adapter.notifyDataSetChanged();
                                return;
                            }
                        }
                        if(name.equals("productList"))
                        {
                            db=new ProductBean();
                        }else if(db!=null)
                        {
                            if (name.equals("productCategoryId")) {
                                db.productCategoryId=parser.nextText();// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("productCode")) {
                                db.productCode=parser.nextText();
                            }
                            else if (name.equalsIgnoreCase("productDesc")) {
                                db.productDesc=parser.nextText();
                            }
                            else if (name.equalsIgnoreCase("productId")) {
                                db.productId=parser.nextText();
                            }
                            else if (name.equalsIgnoreCase("productName")) {
                                db.productName=parser.nextText();
                            }
                            else if (name.equalsIgnoreCase("productPrice")) {
                                db.productPrice=parser.nextText();
                            }
                            else if (name.equalsIgnoreCase("productImageURL")) {
                                db.productImageURL=parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        //读完一个productCategoryList，可以将其添加到集合类中
                        if (parser.getName().equals("productList")&& db != null) {
                            plist.add(db);
                            db = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            if(plist.size()<=0)
            {
                Toast.makeText(ProductsActivity.this,"No data",Toast.LENGTH_SHORT).show();
            }
            isFirstClick=false;
            adapter.notifyDataSetChanged();

//            Log.e("plist",new Gson().toJson(plist));

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void ChoiceXmlPull(String s)
    {
        //解析返回数据
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(s));
            int eventType = parser.getEventType();

            StringBean db=null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
//                        list=new ArrayList<StringBean>();
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if(name.equals("exitCode"))
                        {
                            String code=parser.nextText();
                            Log.e("exitCode", code);

                            if(!code.equals("0"))
                            {
                                ErrorUtils.showErrorMsg(ProductsActivity.this, code);
                                return;
                            }
                        }
                        if(name.equals("productCategoryList"))
                        {
                            db=new StringBean();
                        }else if(db!=null)
                        {
                            if (name.equals("productCategoryId")) {
                                db.id=parser.nextText();// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("productCategoryName")) {
                                db.text=parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        //读完一个productCategoryList，可以将其添加到集合类中
                        if (parser.getName().equals("productCategoryList")&& db != null) {
                            list.add(db);
                            db = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            Log.e("list",new Gson().toJson(list));

            if(list.size()>0)
            {
                tv_center.setText(list.get(0).text);
                DialogUtils.FilterDlg(ProductsActivity.this, list, tv_center, current, listener, 0);
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
