package com.edenred.android.apps.avenesg.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.bean.StringBean;
import com.edenred.android.apps.avenesg.catalogue.CatalogueActivity;
import com.edenred.android.apps.avenesg.catalogue.DlgAdapter;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.home.HomeActivity;
import com.edenred.android.apps.avenesg.login.ManualInputBarcodeActivity;
import com.edenred.android.apps.avenesg.login.ShowPopWindow;
import com.edenred.android.apps.avenesg.login.SubmitEANCodeActivity;
import com.edenred.android.apps.avenesg.profile.MyProfileActivity;
import com.edenred.android.apps.avenesg.settings.ContactUsActivity;

import java.util.List;

/**
 * Created by wangwm on 2015/7/15.
 */
public class DialogUtils {
    public static EditText et_mobile;
    public static TextView tv_country_code;

    /**
     * 祝贺注册成功对话框
     *
     * @param mContext
     */
    public static void congratulationDialog(final Context mContext) {
        final Dialog withMbDlg = new Dialog(mContext, R.style.MyDialogStyle);
        Window window = withMbDlg.getWindow();
        window.setGravity(Gravity.CENTER);
        withMbDlg.show();
        withMbDlg.setCanceledOnTouchOutside(true);
        withMbDlg.getWindow().setContentView(R.layout.dlg_congratulation);
        FontManager.applyFont(mContext, withMbDlg.getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);

        TextView tv_btnText = (TextView) window.findViewById(R.id.tv_btnText);
        TextView tv_bonus = (TextView) window.findViewById(R.id.tv_bonus);
        tv_bonus.setText("To receive " + AveneApplication.getInstance().dialogBean.bonus + " bonus points now, please complete your profile.");
        tv_btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) mContext).gotoOtherActivity(MyProfileActivity.class);
            }
        });
        TextView tv_complete_later = (TextView) window.findViewById(R.id.tv_complete_later);
        tv_complete_later.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_complete_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AveneApplication.getInstance().finishActivity();
                ((BaseActivity) mContext).gotoOtherActivity(HomeActivity.class);
            }
        });
    }

    /**
     * 联系我们对话框
     *
     * @param mContext
     */
    public static void contactUsDialog(Context mContext) {
        final Dialog withMbDlg = new Dialog(mContext, R.style.MyDialogStyle);
        Window window = withMbDlg.getWindow();
        window.setGravity(Gravity.CENTER);
        withMbDlg.show();
        withMbDlg.setCanceledOnTouchOutside(true);
        withMbDlg.getWindow().setContentView(R.layout.dlg_contact_us);
        ImageView iv_cancel = (ImageView) window.findViewById(R.id.iv_cancel);
        TextView tv_email = (TextView) window.findViewById(R.id.tv_email);
        TextView tv_address = (TextView) window.findViewById(R.id.tv_address);
        TextView tv_phone = (TextView) window.findViewById(R.id.tv_phone);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withMbDlg.dismiss();
            }
        });
        tv_email.setText(AveneApplication.getInstance().dialogBean.email);
        tv_address.setText(AveneApplication.getInstance().dialogBean.address);
        tv_phone.setText(AveneApplication.getInstance().dialogBean.phone);
        FontManager.applyFont(mContext, withMbDlg.getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);

    }

    /**
     * 一个按钮对话框
     *
     * @param title    标题
     * @param mContext 信息
     * @param btn_next 　按钮内容
     * @param mContext
     */
    public static void oneButtonDialog(String title, String message, String btn_next, int start, int end, Context mContext) {
        final Dialog withMbDlg = new Dialog(mContext, R.style.MyDialogStyle);
        Window window = withMbDlg.getWindow();
        window.setGravity(Gravity.CENTER);
        withMbDlg.show();
        withMbDlg.setCanceledOnTouchOutside(true);
        withMbDlg.getWindow().setContentView(R.layout.dlg_one_button);
        FontManager.applyFont(mContext, withMbDlg.getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);

        TextView tv_title = (TextView) window.findViewById(R.id.title);
        TextView tv_message = (TextView) window.findViewById(R.id.message);

        TextView tv_btnText = (TextView) window.findViewById(R.id.tv_btnText);
        tv_title.setText(title);
        ((BaseActivity) mContext).setlineSpan(message, tv_message, start, end, ContactUsActivity.class, 2);
        if (btn_next != null) {
            tv_btnText.setText(btn_next);
            tv_btnText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    withMbDlg.cancel();
                }
            });
        }
    }

    /**
     * 手机号对话框
     *
     * @param title    对话框标题
     * @param message  对话框信息
     * @param btn_next 按钮内容
     * @param mContext
     */
    public static void withMobileDialog(String title, String message, String areacode, String phone, String btn_next,
                                        final Context mContext, final List<StringBean> list, final HttpListener mListener) {
        final Dialog withMbDlg = new Dialog(mContext, R.style.MyDialogStyle);
        Window window = withMbDlg.getWindow();
        window.setGravity(Gravity.CENTER);
        withMbDlg.show();
        withMbDlg.setCanceledOnTouchOutside(true);
        withMbDlg.getWindow().setContentView(R.layout.dlg_with_mobile);
        FontManager.applyFont(mContext, withMbDlg.getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);

        TextView tv_title = (TextView) window.findViewById(R.id.title);
        TextView tv_message = (TextView) window.findViewById(R.id.message);
        RelativeLayout rl_country_code = (RelativeLayout) window.findViewById(R.id.rl_country_code);
        tv_country_code = (TextView) window.findViewById(R.id.tv_country_code);
        et_mobile = (EditText) window.findViewById(R.id.et_mobile);
        TextView tv_btnText = (TextView) window.findViewById(R.id.tv_btnText);
        tv_title.setText(title);
        tv_message.setText(message);
        tv_country_code.setText(areacode);
        et_mobile.setText(phone);
        if (btn_next != null) {
            tv_btnText.setText(btn_next);
            tv_btnText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (tv_country_code.getText().toString().trim().replace("+", "").equals("65")) {
                        if (!et_mobile.getText().toString().trim().matches(Constant.CheckSingaporeMobile)) {
                            Toast.makeText(mContext, "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                            withMbDlg.cancel();
                        } else {
                            mListener.sendPassword(tv_country_code.getText().toString().trim(), et_mobile.getText().toString().trim());
                            withMbDlg.cancel();
                        }
                    } else {
                        mListener.sendPassword(tv_country_code.getText().toString().trim(), et_mobile.getText().toString().trim());
                        withMbDlg.cancel();
                    }
                }
            });
        }

        rl_country_code.setTag(R.id.pop1, et_mobile);
        rl_country_code.setTag(R.id.pop2, tv_country_code);
        rl_country_code.setTag(R.id.pop3, list);
        rl_country_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText view = (EditText) v.getTag(R.id.pop1);
                TextView tv = (TextView) v.getTag(R.id.pop2);
                List<StringBean> list1 = (List<StringBean>) v.getTag(R.id.pop3);
                view.clearFocus();
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (list1 != null) {
                    ShowPopWindow.showPopWindow(mContext, v, 0, 0, tv, list1, new ShowPopWindow.PopListener() {
                        @Override
                        public void getPopWindowPosition(View v, int pos, TextView tv) {

                        }
                    });
                } else {
                    mListener.getHttp(v, tv);
                }
            }
        });

    }

    public interface HttpListener {
        void sendPassword(String areacode, String phone);

        void getHttp(View v, TextView tv);
    }



    /**
     * 日历对话框
     *
     * @param mContext
     * @param tv       显示日期的textview
     * @param calendar  当前textview时间
     */
    public static void DateDlg(Context mContext, TextView tv, String calendar, final int flag, final DataListener dataListener) {
        final Dialog alertDialog = new Dialog(mContext, R.style.MyDialogStyle);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setContentView(R.layout.dlg_date);
        FontManager.applyFont(mContext, alertDialog.getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);

        DatePicker date = (DatePicker) window.findViewById(R.id.date_picker);

        ImageView iv_cancle = (ImageView) window.findViewById(R.id.iv_cancle);
        ImageView iv_commit = (ImageView) window.findViewById(R.id.iv_commit);

//        if (Integer.valueOf(android.os.Build.VERSION.SDK)
//                >= Build.VERSION_CODES.LOLLIPOP){
//            ((BaseActivity) mContext).setDatePickerDividerColor(date, 1);
//        }else {
            ((BaseActivity) mContext).setDatePickerDividerColor(date);
//        }
        //初始化事件控件数据
        if (!TextUtils.isEmpty(calendar)) {
            String[] str = calendar.split("/");
            int day = Integer.parseInt(str[0]);
            int month = Integer.parseInt(str[1]) - 1;
            int year = Integer.parseInt(str[2]);
            date.updateDate(year, month, day);
        }
        //取消
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //确定
        iv_commit.setTag(R.id.pop1, date);
        iv_commit.setTag(R.id.pop2, tv);
        iv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker picker = (DatePicker) v.getTag(R.id.pop1);
                TextView textView = (TextView) v.getTag(R.id.pop2);
                int year = picker.getYear();
                int month = picker.getMonth() + 1;
                int day = picker.getDayOfMonth();
                String data = "";
                String textdata = "";
                String month1 = String.valueOf(month);
                String day1 = String.valueOf(day);
                if (month < 10) {
                    month1 = "0" + month;
                }
                if (day < 10) {
                    day1 = "0" + day;
                }
                data = year + "-" + month1 + "-" + day1;
//                textdata = month1 + "/" + day1 + "/" + year;
                textdata = day1 + "/" + month1 + "/" + year;

                textView.setText(textdata);
                if (flag == 0) {
                    dataListener.getDataHttp(data);
                }
                alertDialog.dismiss();
            }
        });
    }

    public interface DataListener {
        void getDataHttp(String time);
    }


    /**
     * 积分主页对话框
     *
     * @param mContext
     * @param tv1
     * @param tv2
     * @param tv3
     * @param tv4
     * @param tv5
     */
    public static void PointDlg(Context mContext, String tv1, String tv2,
                                String tv3, String tv4, String tv5) {
        final Dialog alertDialog = new Dialog(mContext, R.style.MyDialogStyle);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setContentView(R.layout.dlg_point);
        FontManager.applyFont(mContext, alertDialog.getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);

        TextView textView1 = (TextView) window.findViewById(R.id.tv1);
        TextView textView2 = (TextView) window.findViewById(R.id.tv2);
        TextView textView3 = (TextView) window.findViewById(R.id.tv3);
        TextView textView4 = (TextView) window.findViewById(R.id.tv4);
        TextView textView5 = (TextView) window.findViewById(R.id.tv5);
        TextView tv_close = (TextView) window.findViewById(R.id.tv_close);

        textView1.setText("Redemption no: " + tv1);
        textView2.setText("Date: " + tv2);
        textView3.setText("Description: " + tv3);
        textView4.setText("Qty: " + tv4);
        textView5.setText("Points: " + tv5);

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 个人信息主页对话框
     *
     * @param mContext
     * @param title    标题
     * @param content  内容
     * @param flag     显示按钮个数
     */
    public static void ProfileDlg(final Context mContext, String title, String content, final int flag, final int tag) {
        final Dialog alertDialog = new Dialog(mContext, R.style.MyDialogStyle);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setContentView(R.layout.dlg_profile);
        FontManager.applyFont(mContext, alertDialog.getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);

        TextView textView1 = (TextView) window.findViewById(R.id.tv_productname);
        TextView textView2 = (TextView) window.findViewById(R.id.tv_content);
        TextView tv_close = (TextView) window.findViewById(R.id.tv_close);
        TextView tv_back = (TextView) window.findViewById(R.id.tv_back);

        textView1.setText(title);

        if (flag == 3) {
            ((BaseActivity) mContext).setlineSpan(content, textView2, 156, 172, ContactUsActivity.class, 0);
        } else {
            textView2.setText(content);
        }

        //1,个人信息100%  2,个人信息<100%  ,3扫描条形码  4,注册兑换商品  5,购物车成功兑换
        if (flag == 1 || flag == 5) {
            tv_back.setVisibility(View.GONE);
            tv_close.setText("Close");
        } else {
            tv_back.setVisibility(View.VISIBLE);
            if (flag == 4) {
                tv_close.setText("Register New Purchase");
                tv_back.setText("Return to Homepage");
            }
            if (flag == 3) {
                tv_close.setText("Close");
                tv_back.setText("Manual Input");
            }
        }

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (flag == 4) {
                    AveneApplication.getInstance().finishActivity();
                    ((BaseActivity) mContext).goto1OtherActivity(SubmitEANCodeActivity.class, tag);
                } else {
                    if (flag == 5) {
                        AveneApplication.getInstance().finishActivity();
                        ((BaseActivity) mContext).gotoOtherActivity(CatalogueActivity.class);
                    } else {
                        ((BaseActivity) mContext).onBackPressed();
                    }
                }

            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (flag == 4) {
//                    HomeActivity.instanceHomeAc.toggleMenu();
                    AveneApplication.getInstance().finishActivity();
                }
                if (flag == 3) {
                    ((BaseActivity) mContext).goto1OtherActivity(ManualInputBarcodeActivity.class, tag);
                    ((BaseActivity) mContext).onBackPressed();
                }
            }
        });
    }

    /**
     * 商品筛选条件对话框
     *
     * @param mContext
     * @param list     筛选数据
     * @param title    选中后标题改变
     */
    public static void FilterDlg(final Context mContext, final List<StringBean> list, final TextView title, int current, final Listener listener, final int flag) {
        final Dialog alertDialog = new Dialog(mContext, R.style.MyDialogStyle);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        alertDialog.show();
        WindowManager manager = ((BaseActivity) mContext).getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight() / 3;
        alertDialog.getWindow().setLayout(width,
                height);
        alertDialog.setCanceledOnTouchOutside(true);


        alertDialog.getWindow().setContentView(R.layout.dlg_filter);
        FontManager.applyFont(mContext, alertDialog.getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);

        ListView listView = (ListView) window.findViewById(R.id.listview);

        if (flag == 1){
            DlgAdapter adapter = new DlgAdapter(mContext, list, current, flag);
            listView.setAdapter(adapter);
        }else {
            DlgAdapter adapter = new DlgAdapter(mContext, list, current, flag);
            listView.setAdapter(adapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listener.getPosition(position);
                if (flag == 1){
                    title.setText(NumbersFormat.thousand(list.get(position).text));
                }else {
                    title.setText(list.get(position).text);
                }
                alertDialog.dismiss();
            }
        });
    }

    //记录当前位置
    public interface Listener {
        void getPosition(int pos);
    }
}
