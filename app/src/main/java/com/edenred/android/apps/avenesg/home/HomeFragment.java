package com.edenred.android.apps.avenesg.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.bean.NotifyMessageBean;
import com.edenred.android.apps.avenesg.bean.RedeemGiftBean;
import com.edenred.android.apps.avenesg.catalogue.CatalogueActivity;
import com.edenred.android.apps.avenesg.catalogue.RecylerAdapter;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.login.SubmitEANCodeActivity;
import com.edenred.android.apps.avenesg.points.MyPointActivity;
import com.edenred.android.apps.avenesg.promotion.NewsAndPromotionsActivity;
import com.edenred.android.apps.avenesg.utils.DisplayUtil;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.utils.NumbersFormat;
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
 * Created by zhaoxin on 2015/7/15.
 * 首页
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_allpoint, tv_allview1, tv_allview2,
            tv_point1, tv_point2, tv_point3, tv_monthpoint, tv_welcome, tv_website;
    private LinearLayout ll_point1, ll_point2, ll_point3, ll_register;
    private RecyclerView recyclerView1, recyclerView2;
    private RecylerAdapter adapter1, adapter2;
    private SharedPreferencesHelper sp;
    private List<RedeemGiftBean> mlist;
    private List<NotifyMessageBean> mlist1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = AveneApplication.getInstance().getSp();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (sp.getValue(Constant.ACCOUNTBALANCE) != null) {
            tv_allpoint.setText(getResources().getString(R.string.allpoint) +
                    NumbersFormat.thousand(sp.getValue(Constant.ACCOUNTBALANCE)));
        }
        setTextSize(tv_allpoint.getText().toString(),
                tv_allpoint, 17, 16, tv_allpoint.getText().length());
        tv_welcome.setText("Welcome " + sp.getValue(Constant.FIRSTNAME));
        tv_point1.setText(NumbersFormat.thousand(sp.getValue(Constant.EARNED)));
        tv_point2.setText(NumbersFormat.thousand(sp.getValue(Constant.REDEMEED)));
        tv_point3.setText(NumbersFormat.thousand(sp.getValue(Constant.EXPIRED)));

        //获取月份显示成英文
        ((BaseActivity) getActivity()).ChangeMonth2English(tv_monthpoint, sp.getValue(Constant.WILLEXPIRINGNEXTMON), sp.getValue(Constant.EXPIRINGPOINTSDATE));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_main, container, false);
        FontManager.applyFont(getActivity(), getActivity().
                getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);

        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_allpoint = (TextView) view.findViewById(R.id.tv_allpoint);
        tv_welcome = (TextView) view.findViewById(R.id.tv_welcome);
        ll_point1 = (LinearLayout) view.findViewById(R.id.ll_point1);//积分1
        ll_point2 = (LinearLayout) view.findViewById(R.id.ll_point2);//积分2
        ll_point3 = (LinearLayout) view.findViewById(R.id.ll_point3);//积分3
        tv_point1 = (TextView) view.findViewById(R.id.tv_earned);
        tv_point2 = (TextView) view.findViewById(R.id.tv_redemeed);
        tv_point3 = (TextView) view.findViewById(R.id.tv_expired);
        tv_monthpoint = (TextView) view.findViewById(R.id.tv_monthpoint);
        tv_allview1 = (TextView) view.findViewById(R.id.tv_allview1);//All View1
        tv_allview2 = (TextView) view.findViewById(R.id.tv_allview2);//All View2

        ll_register = (LinearLayout) view.findViewById(R.id.ll_register);//底部加号
        tv_website = (TextView) view.findViewById(R.id.tv_website);
        recyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerView1);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerView2);

        iv_back.setOnClickListener(this);
        ll_point1.setOnClickListener(this);
        ll_point2.setOnClickListener(this);
        ll_point3.setOnClickListener(this);
        tv_allview1.setOnClickListener(this);
        tv_allview2.setOnClickListener(this);
        ll_register.setOnClickListener(this);

        initData();
        return view;
    }

    private void initData() {
        // 开启线程请求接口
        MyAsy myAsy = new MyAsy();
        myAsy.setFlag("0");
        myAsy.execute();

        MyAsy myAsy1 = new MyAsy();
        myAsy1.setFlag("1");
        myAsy1.execute();

        recyclerView1.setHasFixedSize(true);
        // 创建一个线性布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 设置布局管理器
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(layoutManager);
        setRecyclerView(recyclerView2);

        //横向lietview右拉加载
//        recyclerView1.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                int visibleItemCount = recyclerView1.getChildCount();//3
//                int totalItemCount = layoutManager.getItemCount();//3
//                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
//
//                if (isLoading) {
//                    return;
//                }
//                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
//                    isLoading = true;
//                    Toast.makeText(getActivity(), "加载更多", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });
        if (!AveneApplication.getInstance().dialogBean.website.isEmpty()) {
            tv_website.setText(AveneApplication.getInstance().dialogBean.website);
        }
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 设置布局管理器
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 改变字体大小
     *
     * @param str
     * @param tv
     */
    private void setTextSize(String str, TextView tv, int tvsize, int first, int end) {
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        // 设置字体字大
//        style.setSpan(new AbsoluteSizeSpan(DisplayUtil.sp2px(getActivity(), tvsize)
//        ), first, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(style);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                HomeActivity.instanceHomeAc.toggleMenu();
                break;
            case R.id.ll_point1:
                ((BaseActivity) getActivity()).goto1OtherActivity(MyPointActivity.class, 1);
                break;
            case R.id.ll_point2:
                ((BaseActivity) getActivity()).goto1OtherActivity(MyPointActivity.class, 2);
                break;
            case R.id.ll_point3:
                ((BaseActivity) getActivity()).goto1OtherActivity(MyPointActivity.class, 3);
                break;
            case R.id.tv_allview1:
                ((BaseActivity) getActivity()).gotoOtherActivity(CatalogueActivity.class);
                break;
            case R.id.tv_allview2:
                ((BaseActivity) getActivity()).gotoOtherActivity(NewsAndPromotionsActivity.class);
                break;
            case R.id.ll_register:
                ((BaseActivity) getActivity()).goto1OtherActivity(SubmitEANCodeActivity.class, 2);
                break;
            default:
                break;
        }
    }

    class MyAsy extends AsyncTask<Object, Object, String> {

        public String flag; // 0--兑换礼品列表    1--促销列表

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
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            if (getFlag().equals("0")) {
                result = httpUtils.putParam(hashMap, Urls.GETREDEEMGIFTLIST);
            } else {
                hashMap.put("accountId", sp.getValue(Constant.ACCOUNTID));
                result = httpUtils.putParam(hashMap, Urls.GETNOTIFYMESSAGELIST);
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
                ErrorUtils.showErrorMsg(getActivity(), "404");
                return;
            } else if (getFlag().equals("0")) {
                RedeemXmlPull(result);
            } else {
                NewsXmlPull(result);
            }
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
            mlist = new ArrayList<RedeemGiftBean>();
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
                                ErrorUtils.showErrorMsg(getActivity(), code);
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
            adapter1 = new RecylerAdapter(getActivity(), DisplayUtil.getWidth(getActivity()) * 5 / 11, 0, mlist);
            recyclerView1.setAdapter(adapter1);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(getActivity(), code);
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
            adapter2 = new RecylerAdapter(getActivity(), DisplayUtil.getWidth(getActivity()) * 5 / 11, 1, mlist1);
            recyclerView2.setAdapter(adapter2);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
