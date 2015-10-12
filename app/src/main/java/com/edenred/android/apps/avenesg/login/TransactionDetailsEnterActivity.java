package com.edenred.android.apps.avenesg.login;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.bean.StringBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.home.HomeActivity;
import com.edenred.android.apps.avenesg.utils.DialogUtils;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.edenred.android.apps.avenesg.view.HorizontalListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangwn on 2015/7/17.
 * 输入交易详情
 */
public class TransactionDetailsEnterActivity extends BaseActivity implements ShowPopWindow.PopListener {


    private ImageView iv_calendar, iv_image_view;
//    private SimpleDraweeView iv_product;
    private TextView tv_date_of_purchase, btn_submit, btn_cancel, tv_retailer_type,
            tv_area, tv_store_location, tv_title;
    private RelativeLayout rl_retailer_type, rl_area, rl_store_location;
    private LinearLayout ll_header;
    private View view_header;
    private int flag = 0, tag = 0;
    private List<StringBean> chainlist = new ArrayList<StringBean>();
    private List<StringBean> arealist = new ArrayList<StringBean>();
    private List<StringBean> counterlist = new ArrayList<StringBean>();
    private SharedPreferencesHelper sp;
    private boolean isclickmore1, isclickmore2, isclickmore3;
    private String date = "", chainId = "", regionId = "",
            accountBalance="",pointsEarned="",pointsRedemed="",pointsExpired="",willExpiringNextMon="";
    private String chainText, areaText, favorCounterId = "";


    private String str="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_transaction_details_enter);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        sp = AveneApplication.getInstance().getSp();
        flag = getIntent().getIntExtra(Constant.FLAG, 0);//0,注册跳转  1/2,首页跳转
        tag = getIntent().getIntExtra(Constant.TAG, 0);
        if (tag == 8){
            initLogo();
        }else {
            initLogo2();
        }
        if (flag == 1){
            HomeActivity.instanceHomeAc.toggleMenu();
        }
        str=AveneApplication.getInstance().dialogBean.awarded;
        initView();
        initData();
    }

    private void initView() {
        btn_submit = (TextView) findViewById(R.id.btn_submit);// 提交按钮
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);//取消
//        iv_product = (SimpleDraweeView) findViewById(R.id.iv_product);//商品图片
        iv_image_view = (ImageView) findViewById(R.id.iv_image_view);
        rl_retailer_type = (RelativeLayout) findViewById(R.id.rl_retailer_type);// 零售商
        tv_retailer_type = (TextView) findViewById(R.id.tv_retailer_type);
        rl_area = (RelativeLayout) findViewById(R.id.rl_area);// 区域
        tv_area = (TextView) findViewById(R.id.tv_area);
        rl_store_location = (RelativeLayout) findViewById(R.id.rl_store_location);// 商店位置
        tv_store_location = (TextView) findViewById(R.id.tv_store_location);
        iv_calendar = (ImageView) findViewById(R.id.iv_calendar);// 日历按钮
        tv_date_of_purchase = (TextView) findViewById(R.id.tv_date_of_purchase);
        tv_title = (TextView) findViewById(R.id.tv_productname);
        ll_header = (LinearLayout) findViewById(R.id.ll_header);
        view_header = findViewById(R.id.view_header);

        btn_submit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        rl_retailer_type.setOnClickListener(this);
        rl_area.setOnClickListener(this);
        rl_store_location.setOnClickListener(this);
        iv_calendar.setOnClickListener(this);
    }

    private void initData() {
        tv_title.setText(AveneApplication.getInstance().productbean.productName);
        if (!TextUtils.isEmpty(AveneApplication.getInstance().productbean.productImageURL)) {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(Urls.IPANDPORT + AveneApplication.getInstance().productbean.productImageURL, iv_image_view, options);

        }
        //0,注册跳转  1/2,首页跳转
        if (flag == 0) {
            initTitle("Registration Process");
            ll_header.setVisibility(View.VISIBLE);
            view_header.setVisibility(View.VISIBLE);
            HorizontalListView hlv_guide = (HorizontalListView) findViewById(R.id.hlv_guide);
            TransactionAdapter adapter = new TransactionAdapter(this);
            hlv_guide.setAdapter(adapter);

        } else {
            initLogo();
            initTitle("Enter Transaction Details");
            ll_header.setVisibility(View.GONE);
            view_header.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.VISIBLE);
        }
        MyAsy myAsy = new MyAsy();
        myAsy.setFlag("00");
        myAsy.execute();
//        MyAsy myAsy1 = new MyAsy();
//        myAsy1.setFlag("11");
//        myAsy1.execute();
//        MyAsy myAsy2 = new MyAsy();
//        myAsy2.setFlag("22");
//        myAsy2.execute();
    }

    public boolean canSubmit() {
        if (TextUtils.isEmpty(tv_date_of_purchase.getText().toString().trim())) {
            return false;
        }
        if (TextUtils.isEmpty(tv_retailer_type.getText().toString().trim())) {
            return false;
        }
        if (TextUtils.isEmpty(tv_area.getText().toString().trim())) {
            return false;
        }
        if (TextUtils.isEmpty(tv_store_location.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.iv_calendar:
                DialogUtils.DateDlg(this, tv_date_of_purchase, tv_date_of_purchase.getText().toString(), 1, null);
                break;

            case R.id.btn_submit:
                if (canSubmit()) {
                    if (flag == 1||flag == 2) {
                        showPD("Upload...");
                        getTransactionInfo();
                        SaveAsy saveAsy=new SaveAsy();
                        saveAsy.setFlag("0");
                        saveAsy.execute();
                    } else {
                        String[] str = tv_date_of_purchase.getText().toString().split("/");
                        AveneApplication.getInstance().memberinfo.date = str[2] + "-" + str[1] + "-" + str[0];
                        gotoOtherActivity(PersonalDetailsEnterActivity.class);
                    }

                } else {
                    // 信息填写不完整
                    DialogUtils.oneButtonDialog("Incomplete Submission", "Please fill in all the fields.", "Close", 0, 0, this);
                }
                break;
            case R.id.btn_cancel:
                AveneApplication.getInstance().finishActivity();
                break;
            case R.id.rl_retailer_type:
                if (chainlist.size() <= 0) {
                    if (!isclickmore1) {
                        isclickmore1 = true;
                        MyAsy myAsy = new MyAsy();
                        myAsy.setFlag("0");
                        myAsy.execute();
                    }
                } else {
                    ShowPopWindow.showPopWindow(this, rl_retailer_type, 0, 0, tv_retailer_type, chainlist, this);
                }
                break;
            case R.id.rl_area:
                    if (!isclickmore2) {
                        isclickmore2 = true;
                        MyAsy myAsy = new MyAsy();
                        myAsy.setFlag("1");
                        myAsy.execute();
                    }
                break;
            case R.id.rl_store_location:
                    if (!isclickmore3) {
                        isclickmore3 = true;
                        MyAsy myAsy = new MyAsy();
                        myAsy.setFlag("2");
                        myAsy.execute();
                    }
                break;
            default:
                break;
        }
    }

    @Override
    public void getPopWindowPosition(View v, int pos, TextView tv) {
        switch (v.getId()) {
            case R.id.rl_retailer_type:
                chainId = chainlist.get(pos).id;
                chainText = tv.getText().toString();
                if (!chainText.equals(chainlist.get(pos).text)) {
                    tv_area.setText("");
                    tv_store_location.setText("");
                }
                break;
            case R.id.rl_area:
                regionId = arealist.get(pos).id;
                areaText = tv.getText().toString();
                if (!areaText.equals(arealist.get(pos).text)) {
                    tv_store_location.setText("");
                }
                break;
            case R.id.rl_store_location:
                AveneApplication.getInstance().PurchaseCounterId = counterlist.get(pos).id;
                break;
            default:
                break;
        }
    }

    private void getTransactionInfo() {
        String[] date1 = tv_date_of_purchase.getText().toString().split("/");
        date = date1[2] + "-" + date1[1] + "-" + date1[0];
    }

    class SaveAsy extends AsyncTask<Object, Object, String> {

        public String flag; // 0--保存交易信息    1--获取会员信息拿到会员积分

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        @Override
        protected String doInBackground(Object... params) {
            HttpUtils httpUtils = new HttpUtils();
            String result = null;
            String str = "";
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            hashMap.put("accountId", sp.getValue(Constant.ACCOUNTID));
            if(getFlag().equals("0"))
            {
                hashMap.put("infoChannelId", "1");
                str = "\n<productId>" + AveneApplication.getInstance().productbean.productId + "</productId>\n" +
                        "\t\t\t\t\t<purchaseCode>" + AveneApplication.getInstance().purchaseCode + "</purchaseCode>\n" +
                        "\t\t\t\t\t<purchaseCounterId>" + AveneApplication.getInstance().PurchaseCounterId + "</purchaseCounterId>\n" +
                        "\t\t\t\t\t<purchaseDate>" + date + "</purchaseDate>\n"
                ;
                hashMap.put("transactions", str);
                result = httpUtils.putParam(hashMap, Urls.SAVETRANSACTION);
            }else
            {
                result = httpUtils.putParam(hashMap, Urls.GETMEMBERINFO);
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

            if (result.equals("error")) {
                cancelPD();
                ErrorUtils.showErrorMsg(TransactionDetailsEnterActivity.this, "404");
                return;
            }
            if(getFlag().equals("0"))
            {
                SaveXmlPull(result);
            }else
            {
                cancelPD();
                getMemberXmlpull(result);
            }

        }
    }

    class MyAsy extends AsyncTask<Object, Object, String> {

        public String flag; // 0--Chain信息    1--城市下属区域信息   2--柜台信息

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
            if (currentFlag.equals("0")||currentFlag.equals("00")) {
                result = httpUtils.putParam(hashMap, Urls.GETCHAINLIST);
            } else if (currentFlag.equals("1")||currentFlag.equals("11")) {
                hashMap.put("chainId", chainId);
                result = httpUtils.putParam(hashMap, Urls.GETAREALIST);
            } else if (currentFlag.equals("2")||currentFlag.equals("22")) {
                hashMap.put("chainId", chainId);
                hashMap.put("regionId", regionId);
                result = httpUtils.putParam(hashMap, Urls.GETCOUNTERLIST);
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
            String currentFlag = getFlag();
            if (currentFlag.equals("0")) {
                isclickmore1 = false;
            } else if (currentFlag.equals("1")) {
                isclickmore2 = false;
            } else if (currentFlag.equals("2")) {
                isclickmore3 = false;
            }
            if (result.equals("error")) {
                ErrorUtils.showErrorMsg(TransactionDetailsEnterActivity.this, "404");
                return;
            }
            if (currentFlag.equals("0")||currentFlag.equals("00")) {
                ChainXmlPull(result,currentFlag);
            } else if (currentFlag.equals("1")||currentFlag.equals("11")) {
                AreaXmlPull(result,currentFlag);
            } else if (currentFlag.equals("2")||currentFlag.equals("22")) {
                CounterXmlPull(result,currentFlag);
            }
        }
    }

    private void SaveXmlPull(String result) {
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
                                cancelPD();
                                ErrorUtils.showErrorMsg(TransactionDetailsEnterActivity.this, code);
                                return;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        break;
                }
                eventType = parser.next();
            }
            SaveAsy saveAsy1=new SaveAsy();
            saveAsy1.setFlag("1");
            saveAsy1.execute();

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
                                ErrorUtils.showErrorMsg(TransactionDetailsEnterActivity.this, code);
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

            sp.putValue(Constant.ACCOUNTBALANCE, accountBalance);
            sp.putValue(Constant.EARNED, pointsEarned);
            sp.putValue(Constant.REDEMEED, pointsRedemed);
            sp.putValue(Constant.EXPIRED, pointsExpired);
            sp.putValue(Constant.WILLEXPIRINGNEXTMON, willExpiringNextMon);

            DialogUtils.ProfileDlg(this, "Submitted", str, 4, flag);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ChainXmlPull(String result, String flag) {
        //解析返回数据
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();
            StringBean db = null;
            //循环取出所有数据
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        chainlist.clear();
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            Log.e("exitCode", code);
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(TransactionDetailsEnterActivity.this, code);
                                return;
                            }
                        }
                        if (name.equals("chainList")) {
                            db = new StringBean();
                        } else if (db != null) {
                            if (name.equals("chainId")) {
                                db.id = parser.nextText();// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("chainName")) {
                                db.text = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equals("chainList") && db != null) {
                            chainlist.add(db);
                            db = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            // 0 点击后显示    00 不显示
            if (flag.equals("0")){
                ShowPopWindow.showPopWindow(this, rl_retailer_type, 0, 0, tv_retailer_type, chainlist, this);
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void AreaXmlPull(String result, String flag) {
        //解析返回数据
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();
            StringBean db = null;
            //循环取出所有数据
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        arealist.clear();
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            Log.e("exitCode", code);
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(TransactionDetailsEnterActivity.this, code);
                                return;
                            }
                        }
                        if (name.equals("areaList")) {
                            db = new StringBean();
                        } else if (db != null) {
                            if (name.equals("areaId")) {
                                db.id = parser.nextText();// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("areaName")) {
                                db.text = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equals("areaList") && db != null) {
                            arealist.add(db);
                            db = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            // 1 点击后显示    11 不显示
            if (flag.equals("1")){
                ShowPopWindow.showPopWindow(this, rl_area, 0, 0, tv_area, arealist, this);
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CounterXmlPull(String result, String flag) {
        //解析返回数据
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();
            StringBean db = null;
            //循环取出所有数据
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        counterlist.clear();
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            Log.e("exitCode", code);
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(TransactionDetailsEnterActivity.this, code);
                                return;
                            }
                        }
                        if (name.equals("counterList")) {
                            db = new StringBean();
                        } else if (db != null) {
                            if (name.equals("counterId")) {
                                db.id = parser.nextText();// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("counterName")) {
                                db.text = parser.nextText();
                            } else if (name.equalsIgnoreCase("counterCode")) {
                                db.counterCode = parser.nextText();
                            } else if (name.equalsIgnoreCase("chainId")) {
                                db.chainId = parser.nextText();
                            } else if (name.equalsIgnoreCase("cityId")) {
                                db.cityId = parser.nextText();
                            } else if (name.equalsIgnoreCase("areaId")) {
                                db.areaId = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equals("counterList") && db != null) {
                            counterlist.add(db);
                            db = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            // 2 点击后显示    22 不显示
            if (flag.equals("2")){
                ShowPopWindow.showPopWindow(this, rl_store_location, 0, 0, tv_store_location, counterlist, this);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
