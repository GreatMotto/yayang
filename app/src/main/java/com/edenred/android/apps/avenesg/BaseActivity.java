package com.edenred.android.apps.avenesg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.home.HomeActivity;
import com.edenred.android.apps.avenesg.utils.DisplayUtil;
import com.edenred.android.apps.avenesg.utils.SpantoActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;


/**
 * Created by zhaoxin on 2015/7/14.
 */
public class BaseActivity extends SlidingFragmentActivity implements View.OnClickListener {

    private int x, y;
    private LinearLayout ll_back, ll_to_home;
    private TextView tv_content;
    private ProgressDialog pd;
    private String str;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setBehindContentView(R.layout.fg_home_menu);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void initTitle(String content) {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_content = (TextView) findViewById(R.id.tv_content);
        ll_back.setOnClickListener(this);
        tv_content.setText(content);
    }
    public void initLogo() {
        ll_to_home = (LinearLayout) findViewById(R.id.ll_to_home);
        ll_to_home.setOnClickListener(this);
    }

    // 返回时关闭软键盘
    public void CloseKeyboard() {
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        // TODO Auto-generated method stub
        super.startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        // TODO Auto-generated method stub
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        CloseKeyboard();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) ev.getX();
                y = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs((int) ev.getY() - y) <= Math.abs((int) ev.getX() - x)) {
                    View v = getCurrentFocus();
                    if (isShouldHideInput(v, ev)) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                    x = 0;
                    y = 0;
                    return super.dispatchTouchEvent(ev);
                }
                x = 0;
                y = 0;
                break;

            default:
                break;
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    //根据触摸位置计算是否隐藏输入法
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1] - 20;
            int bottom = top + v.getHeight() + 40;
            int right = left + v.getWidth() + 10000;
            if (event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 跳转到其他Activity
    public void gotoOtherActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }

    // 带一个参数跳转到其他Activity
    public void goto1OtherActivity(Class<?> cls, int flag) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtra(Constant.FLAG, flag);
        startActivity(intent);
    }

    /**
     * 改变字体大小
     *
     * @param str
     * @param tv
     */
    public void setTextSize(String str, TextView tv, int tvsize, int first, int end) {
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        // 设置字体第一个和第9个字大
        style.setSpan(new AbsoluteSizeSpan(DisplayUtil.sp2px(this, tvsize)
        ), first, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(style);
    }

    /**
     * 设置文本下划线
     *
     * @param str   文本内容
     * @param tv    文本框
     * @param start 下划线开始位置
     * @param end   下划线结束位置
     */
    public void setlineSpan(String str, TextView tv, int start, int end, Class<?> cls, int flag) {
        SpannableString ss = new SpannableString(str);
//        ss.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new SpantoActivity(this, cls, flag), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.grey_dark)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * 购物车放大
     */
    public void startAnim(final TextView tv_sum, int num) {
//        String str = tv_sum.getText().toString();
//        int num = 0;
//        if (!TextUtils.isEmpty(str)) {
//            num = Integer.parseInt(str);
//        }
        tv_sum.setText(String.valueOf(num));
        //购物车数量增加是购物车放大效果
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);//设置动画持续时间
        animation.setFillAfter(true);
        tv_sum.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {

            public void run() {
                //购物车数量增加是购物车缩小效果
                ScaleAnimation animation1 = new ScaleAnimation(1.3f, 1.0f, 1.3f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation1.setDuration(300);//设置动画持续时间
                animation1.setFillAfter(true);
                tv_sum.startAnimation(animation1);
            }
        }, 300);


    }

    /**
     * 设置时间选择器的分割线颜色
     *
     * @param datePicker
     */
    public void setDatePickerDividerColor(DatePicker datePicker) {
        // Divider changing:

        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(getResources().getColor(R.color.normal_orange)));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    /**
     * 显示等待窗
     *
     * @param content
     */
    protected void showPD(String content) {
        str = content;
        if (null == pd) {
            pd = new ProgressDialog(this);
        }
        if (null != content) {
            pd.setMessage(content);
            pd.setCancelable(true);
        }
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * 关闭等待窗
     */
    public void cancelPD() {
        if (null != pd && pd.isShowing()) {
            pd.cancel();
        }
    }

    /**
     * 获取系统当前月份并转换成英文
     *
     * @return
     */
    public void ChangeMonth2English(TextView tv, String points, String date) {
        String[] d = new String[3];
//        Calendar c = Calendar.getInstance();
//        String str = String.valueOf(c.get(Calendar.MONTH) + 1);
        if (!TextUtils.isEmpty(date)) {
            d = date.split("-");
        }
        String str = d[1];
        switch (str) {
            case "01":
                str = "January";
                break;
            case "02":
                str = "February";
                break;
            case "03":
                str = "March ";
                break;
            case "04":
                str = "April";
                break;
            case "05":
                str = "May";
                break;
            case "06":
                str = "June";
                break;
            case "07":
                str = "July";
                break;
            case "08":
                str = "August";
                break;
            case "09":
                str = "September";
                break;
            case "10":
                str = "October";
                break;
            case "11":
                str = "November";
                break;
            case "12":
                str = "December";
                break;
            default:
                break;
        }
        tv.setText("You have " + points + " points expiring in " + str);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.ll_to_home:
//                getSlidingMenu().isShown();
//                if (getSlidingMenu().isShown()){
                    HomeActivity.instanceHomeAc.toggleMenu();
//                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AveneApplication.getInstance().finishActivity();
                    }
                },350);
//                AveneApplication.getInstance().finishActivity();
//                startActivity(new Intent(this, HomeActivity.class));
//                this.finish();
                break;
            default:
                break;
        }
    }


}
