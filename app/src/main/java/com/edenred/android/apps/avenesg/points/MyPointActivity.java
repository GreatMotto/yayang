package com.edenred.android.apps.avenesg.points;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.bean.PointBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.DialogUtils;
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
 * Created by zhaoxin on 2015/7/17.
 * 我的积分
 */
public class MyPointActivity extends BaseActivity {

    private ImageView iv_line, iv_line1, iv_line2, iv_line3,
            iv_line4, iv_line5, iv_left, iv_right;
    private LinearLayout ll_point1, ll_point2, ll_point3, ll_plus;
    private TextView tv_data, tv_data_left, tv_data_right, tv_allpoint,
            tv_point, tv_point3, tv_point4, tv_des, tv_time, tv_earned,
            tv_redemeed, tv_expired, tv_monthpoint;
    private RelativeLayout rl_all;
    private ListView lv_data;
    private PointAdapter adapter;
    private int current = 1;
    private int width = 0, diswidth = 0, flag = 0;
    private String id = "0", startTime = "", endTime = "";
    private SharedPreferencesHelper sp;
    private List<PointBean> list = new ArrayList<PointBean>();
    private List<PointBean> list1 = new ArrayList<PointBean>();
    private List<PointBean> list2 = new ArrayList<PointBean>();
    private List<PointBean> list3 = new ArrayList<PointBean>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_mypoint);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        sp = AveneApplication.getInstance().getSp();
        flag = getIntent().getIntExtra(Constant.FLAG, 0);
        if (flag == 0) {
            initLogo();
        } else {
            initLogo2();
        }
        id = sp.getValue(Constant.ACCOUNTID);
        current = flag;
        initTitle(getResources().getString(R.string.point));
        initView();
    }


    private void initView() {
        iv_line = (ImageView) findViewById(R.id.iv_line);//滚动线
        tv_allpoint = (TextView) findViewById(R.id.tv_allpoint);//积分字体变大
        ll_point1 = (LinearLayout) findViewById(R.id.ll_point1);//积分1
        ll_point2 = (LinearLayout) findViewById(R.id.ll_point2);//积分2
        ll_point3 = (LinearLayout) findViewById(R.id.ll_point3);//积分3
        tv_earned = (TextView) findViewById(R.id.tv_earned);
        tv_redemeed = (TextView) findViewById(R.id.tv_redemeed);
        tv_expired = (TextView) findViewById(R.id.tv_expired);
        tv_monthpoint = (TextView) findViewById(R.id.tv_monthpoint);

        tv_data_left = (TextView) findViewById(R.id.tv_data_left);//开始日期
        tv_data_right = (TextView) findViewById(R.id.tv_data_right);//结束日期
        iv_left = (ImageView) findViewById(R.id.iv_left);//开始日期按钮
        iv_right = (ImageView) findViewById(R.id.iv_right);//结束日期按钮

        rl_all = (RelativeLayout) findViewById(R.id.rl_all);
        tv_data = (TextView) findViewById(R.id.tv_data);
        tv_des = (TextView) findViewById(R.id.tv_des);
        tv_point = (TextView) findViewById(R.id.tv_point);
        tv_point3 = (TextView) findViewById(R.id.tv_point3);
        tv_point4 = (TextView) findViewById(R.id.tv_point4);
        tv_time = (TextView) findViewById(R.id.tv_time);
        ll_plus = (LinearLayout) findViewById(R.id.ll_plus);

        iv_line1 = (ImageView) findViewById(R.id.iv_line1);
        iv_line2 = (ImageView) findViewById(R.id.iv_line2);
        iv_line3 = (ImageView) findViewById(R.id.iv_line3);
        iv_line4 = (ImageView) findViewById(R.id.iv_line4);
        iv_line5 = (ImageView) findViewById(R.id.iv_line5);
        lv_data = (ListView) findViewById(R.id.lv_data);

        initData();


        //初始化动画横线
        if (flag == 2) {
            ll_point2.setEnabled(false);
            startAnim(0, diswidth / 3, 0);
            initData2();
        } else {
            if (flag == 3) {
                ll_point3.setEnabled(false);
                startAnim(0, diswidth * 2 / 3, 0);
                initData3();
            } else {
                ll_point1.setEnabled(false);
                initData1();
            }
        }

        getHttp();


        ll_point1.setOnClickListener(this);
        ll_point2.setOnClickListener(this);
        ll_point3.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
    }

    private void initData() {
        if (sp.getValue(Constant.ACCOUNTBALANCE) != null) {
            tv_allpoint.setText(getResources().getString(R.string.allpoint) +
                    NumbersFormat.thousand(sp.getValue(Constant.ACCOUNTBALANCE)));
        }
        //改变字体大小
        setTextSize(tv_allpoint.getText().toString(),
                tv_allpoint, 17, 16, tv_allpoint.getText().length());

        tv_earned.setText(NumbersFormat.thousand(sp.getValue(Constant.EARNED)));
        tv_redemeed.setText(NumbersFormat.thousand(sp.getValue(Constant.REDEMEED)));
        tv_expired.setText(NumbersFormat.thousand(sp.getValue(Constant.EXPIRED)));

        //获取月份显示成英文
        ChangeMonth2English(tv_monthpoint, sp.getValue(Constant.WILLEXPIRINGNEXTMON), sp.getValue(Constant.EXPIRINGPOINTSDATE));

        diswidth = DisplayUtil.getWidth(this);//获取系统宽度
        rl_all.measure(0, 0);
        width = rl_all.getMeasuredWidth();
        int heght = rl_all.getMeasuredHeight();
        tv_data.measure(0, 0);
        int dataheigh = tv_data.getMeasuredHeight();
        //设置ListView宽高
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lv_data.getLayoutParams();
        lp.width = width - DisplayUtil.dip2px(this, 8);
        lp.height = heght - dataheigh - DisplayUtil.dip2px(this, 18);
        lv_data.setLayoutParams(lp);
    }

    private void initData1() {
        iv_line2.setVisibility(View.VISIBLE);
        tv_point.setVisibility(View.VISIBLE);
        iv_line3.setVisibility(View.GONE);
        iv_line4.setVisibility(View.GONE);
        iv_line5.setVisibility(View.GONE);
        tv_point3.setVisibility(View.GONE);
        tv_point4.setVisibility(View.GONE);
        ll_plus.setVisibility(View.GONE);
        tv_data.setText("Date");
        tv_des.setText("Description");
        tv_point.setText("Points");
        tv_time.setText(getResources().getString(R.string.point_p1));
        //设置两根线距左的距离
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) iv_line1.getLayoutParams();
        lp1.leftMargin = width * 2 / 7;
        iv_line1.setLayoutParams(lp1);

        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) iv_line2.getLayoutParams();
        lp2.leftMargin = width * 5 / 7;
        iv_line2.setLayoutParams(lp2);
    }

    private void initData2() {
        iv_line2.setVisibility(View.VISIBLE);
        iv_line3.setVisibility(View.VISIBLE);
        iv_line4.setVisibility(View.VISIBLE);
        iv_line5.setVisibility(View.VISIBLE);
        tv_point.setVisibility(View.VISIBLE);
        tv_point3.setVisibility(View.VISIBLE);
        tv_point4.setVisibility(View.VISIBLE);
        ll_plus.setVisibility(View.VISIBLE);
        tv_data.setText("Ref no");
        tv_des.setText("Date");
        tv_point.setText("Description");
        tv_point3.setText("Qty");
        tv_point4.setText("Points");
        tv_time.setText(getResources().getString(R.string.point_p2));
        //设置5根线距左的距离
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) iv_line1.getLayoutParams();
        lp1.leftMargin = width / 6;
        iv_line1.setLayoutParams(lp1);

        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) iv_line2.getLayoutParams();
        lp2.leftMargin = width * 5 / 12;
        iv_line2.setLayoutParams(lp2);

        RelativeLayout.LayoutParams lp3 = (RelativeLayout.LayoutParams) iv_line3.getLayoutParams();
        lp3.leftMargin = width * 2 / 3;
        iv_line3.setLayoutParams(lp3);

        RelativeLayout.LayoutParams lp4 = (RelativeLayout.LayoutParams) iv_line4.getLayoutParams();
        lp4.leftMargin = width * 3 / 4;
        iv_line4.setLayoutParams(lp4);

        RelativeLayout.LayoutParams lp5 = (RelativeLayout.LayoutParams) iv_line5.getLayoutParams();
        lp5.leftMargin = width * 11 / 12;
        iv_line5.setLayoutParams(lp5);

    }

    private void initData3() {
        iv_line2.setVisibility(View.GONE);
        iv_line3.setVisibility(View.GONE);
        iv_line4.setVisibility(View.GONE);
        iv_line5.setVisibility(View.GONE);
        tv_point.setVisibility(View.GONE);
        tv_point3.setVisibility(View.GONE);
        tv_point4.setVisibility(View.GONE);
        ll_plus.setVisibility(View.GONE);
        tv_data.setGravity(Gravity.CENTER);
        tv_des.setGravity(Gravity.CENTER);
        tv_time.setText(getResources().getString(R.string.point_p3));
        tv_data.setText("Date");
        tv_des.setText("Points");
        //设置1根线距左的距离
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) iv_line1.getLayoutParams();
        lp1.leftMargin = width / 2;
        iv_line1.setLayoutParams(lp1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_point1:

                ll_point1.setEnabled(false);
                ll_point2.setEnabled(true);
                ll_point3.setEnabled(true);

                if (current == 2) {
                    startAnim(diswidth / 3, 0, 200);
                } else {
                    startAnim(diswidth * 2 / 3, 0, 200);
                }
                current = 1;
                initData1();
                if (list1.size() <= 0) {
                    getHttp();
                } else {
                    adapter = new PointAdapter(this, width, list1);
                    adapter.setFlag(0);
                    lv_data.setAdapter(adapter);
                }
//                adapter.setFlag(0);
//                adapter.notifyDataSetChanged();
                break;
            case R.id.ll_point2:

                ll_point2.setEnabled(false);
                ll_point3.setEnabled(true);
                ll_point1.setEnabled(true);
                if (current == 1) {
                    startAnim(0, diswidth / 3, 200);
                } else {
                    startAnim(diswidth * 2 / 3, diswidth / 3, 200);
                }
                current = 2;
                initData2();
                if (list2.size() <= 0) {
                    getHttp();
                } else {
                    adapter = new PointAdapter(this, width, list2);
                    adapter.setFlag(1);
                    lv_data.setAdapter(adapter);
                }
//                adapter.setFlag(1);
//                adapter.notifyDataSetChanged();
                break;
            case R.id.ll_point3:
                ll_point3.setEnabled(false);
                ll_point2.setEnabled(true);
                ll_point1.setEnabled(true);
                if (current == 1) {
                    startAnim(0, diswidth * 2 / 3, 200);
                } else {
                    startAnim(diswidth / 3, diswidth * 2 / 3, 200);
                }
                current = 3;
                initData3();
                if (list3.size() <= 0) {
                    getHttp();
                } else {
                    adapter = new PointAdapter(this, width, list3);
                    adapter.setFlag(2);
                    lv_data.setAdapter(adapter);
                }
//                adapter.setFlag(2);
//                adapter.notifyDataSetChanged();
                break;
            case R.id.iv_left:
                DialogUtils.DateDlg(this, tv_data_left, tv_data_left.getText().toString(), 0, new DialogUtils.DataListener() {
                    @Override
                    public void getDataHttp(String time) {
                        startTime = time;
                        list1.clear();
                        list2.clear();
                        list3.clear();
                        getHttp();
                    }
                });
                break;
            case R.id.iv_right:
                DialogUtils.DateDlg(this, tv_data_right, tv_data_right.getText().toString(), 0, new DialogUtils.DataListener() {
                    @Override
                    public void getDataHttp(String time) {
                        endTime = time;
                        list1.clear();
                        list2.clear();
                        list3.clear();
                        getHttp();
                    }
                });
                break;
            default:
                break;
        }
    }

    //执行动画
    private void startAnim(int start, int end, int time) {
        TranslateAnimation anim = new TranslateAnimation(start, end, 0, 0);
        anim.setDuration(time);//动画执行时间
        anim.setFillAfter(true);//动画结束后停在结束位置
        iv_line.startAnimation(anim);
    }

    //请求网络数据
    private void getHttp() {
        list.clear();
        adapter = new PointAdapter(this, width, list);
        adapter.setFlag(current - 1);
        lv_data.setAdapter(adapter);
        MyAsy myask2 = new MyAsy();
        myask2.setTag("00" + String.valueOf(current));
        myask2.execute();
    }

    //Object, Object, String调用参数，进度和结果
    class MyAsy extends AsyncTask<Object, Integer, String> {

        public String tag;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        //执行耗时操作
        @Override
        protected String doInBackground(Object... params) {
            //请求接口
            HttpUtils httpUtils = new HttpUtils();
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            hashMap.put("accountId", id);
            hashMap.put("pointGetDateFrom", startTime);
            hashMap.put("pointGetDateTo", endTime);
            hashMap.put("pointTypeCode", getTag());

            String result = httpUtils.putParam(hashMap, Urls.GETPOINTLIST);

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
                ErrorUtils.showErrorMsg(MyPointActivity.this, "404");
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

            PointBean db = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
//                        list=new ArrayList<PointBean>();
                        list.clear();
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(MyPointActivity.this, code);
                                return;
                            }
                        }
                        if (name.equals("pointList")) {
                            db = new PointBean();
                        } else if (db != null) {
                            if (name.equals("description")) {
                                db.description = parser.nextText();// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("point")) {
                                db.point = parser.nextText();
                            } else if (name.equalsIgnoreCase("pointGetDate")) {
                                db.pointGetDate = parser.nextText();
                            } else if (name.equalsIgnoreCase("quantity")) {
                                db.quantity = parser.nextText();
                            } else if (name.equalsIgnoreCase("id")) {
                                db.id = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        //读完一个productCategoryList，可以将其添加到集合类中
                        if (parser.getName().equals("pointList") && db != null) {
                            list.add(db);
                            db = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            switch (current) {
                case 1:
                    list1.clear();
                    list1.addAll(list);
                    break;
                case 2:
                    list2.clear();
                    list2.addAll(list);
                    break;
                case 3:
                    list3.clear();
                    list3.addAll(list);
                    break;
                default:
                    break;

            }
            if (list.size() <= 0) {
                Toast.makeText(MyPointActivity.this, "No data", Toast.LENGTH_SHORT).show();
            }

            adapter.notifyDataSetChanged();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
