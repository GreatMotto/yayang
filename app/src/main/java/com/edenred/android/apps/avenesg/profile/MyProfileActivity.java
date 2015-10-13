package com.edenred.android.apps.avenesg.profile;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.R;
import com.edenred.android.apps.avenesg.bean.MemberInfoReturnBean;
import com.edenred.android.apps.avenesg.bean.StringBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.utils.DialogUtils;
import com.edenred.android.apps.avenesg.utils.ErrorUtils;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.HttpUtils;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.edenred.android.apps.avenesg.view.PopAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhaoxin on 2015/7/22.
 * 个人信息
 */
public class MyProfileActivity extends BaseActivity implements View.OnFocusChangeListener {

    private TextView tv_bonus_text, tvProgressLeft, tvProgressRight, tvAreaCode, tvBirthday, tvCity,
            tvMtatus, tvRange, tvNationality, tvTyle, tvConcerns, tvSensitivity, tvSeclect,
            tvRetailer, tvArea, tvLocation, tvUpdate, tvSex, tvMobile, tv_occupation;
    private ProgressBar progress;
    private EditText etFirstName, etLastName, etEmail, etEmailRight, etAddress, etPostalCode;
    private ImageView ivCalendar, iv_orange_left, iv_orange_right;
    private PopAdapter sortAdapter;
    private PopupWindow popSort;
    private RelativeLayout ll_retailer, ll_area, ll_location;
    private double sum = 0.00;
    private SharedPreferencesHelper sp;
    private boolean[] is_select = new boolean[20];
    private List<StringBean> sexlist = new ArrayList<StringBean>();
    private List<StringBean> postalcodelist = new ArrayList<StringBean>();
    private List<StringBean> selectlist = new ArrayList<StringBean>();
    private List<StringBean> occupationlist = new ArrayList<StringBean>();
    private List<StringBean> concernslist = new ArrayList<StringBean>();
    private List<StringBean> rangelist = new ArrayList<StringBean>();
    private List<StringBean> sensitivitylist = new ArrayList<StringBean>();
    private List<StringBean> typeslist = new ArrayList<StringBean>();
    private List<StringBean> statuslist = new ArrayList<StringBean>();
    private List<StringBean> chainlist = new ArrayList<StringBean>();
    private List<StringBean> arealist = new ArrayList<StringBean>();
    private List<StringBean> counterlist = new ArrayList<StringBean>();
    private List<StringBean> citylist = new ArrayList<StringBean>();
    private List<StringBean> countrylist = new ArrayList<StringBean>();
    private String address, birthday, cityId = "", countryId = "", chainId = "0", regionId = "0", email,
            firstName, lastName, memberInfoRatio, postcode = "", sexId, occupationid = "",
            concernsid = "", selectid = "", rangeid = "", sensitivityid = "", typesid = "", statusid = "",
            retailerTypeid = "", areaid = "", favorCounterId = "",
            occupation = "", concerns = "", select = "", range = "", sensitivity = "", types = "", status = "";
    private int per;
    private String chainText, areaText;
    private String bonus = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_myprofile);
        FontManager.applyFont(this, getWindow().getDecorView().findViewById(android.R.id.content), Constant.TTFNAME);
        AveneApplication.getInstance().addActivity(this);
        initLogo();
        sp = AveneApplication.getInstance().getSp();
        initTitle("My Profile");
        initView();
    }

    private void initView() {
        tv_bonus_text = (TextView) findViewById(R.id.tv_bonus_text);
        tvProgressLeft = (TextView) findViewById(R.id.tv_progress_left);
        progress = (ProgressBar) findViewById(R.id.progress);
        tvProgressRight = (TextView) findViewById(R.id.tv_progress_right);
        etFirstName = (EditText) findViewById(R.id.et_first_name);
        etLastName = (EditText) findViewById(R.id.et_last_name);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        etEmail = (EditText) findViewById(R.id.et_email);
        etEmailRight = (EditText) findViewById(R.id.et_email_right);
        tvAreaCode = (TextView) findViewById(R.id.tv_area_code);
        tvMobile = (TextView) findViewById(R.id.tv_mobile);
        tvBirthday = (TextView) findViewById(R.id.tv_birthday);
        ivCalendar = (ImageView) findViewById(R.id.iv_calendar);
        etAddress = (EditText) findViewById(R.id.et_address);
        etPostalCode = (EditText) findViewById(R.id.et_postal_code);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tv_occupation = (TextView) findViewById(R.id.tv_occupation);
        tvMtatus = (TextView) findViewById(R.id.tv_mtatus);
        tvRange = (TextView) findViewById(R.id.tv_range);
        tvNationality = (TextView) findViewById(R.id.tv_nationality);
        tvTyle = (TextView) findViewById(R.id.tv_tyle);
        tvConcerns = (TextView) findViewById(R.id.tv_concerns);
        tvSensitivity = (TextView) findViewById(R.id.tv_sensitivity);
        tvSeclect = (TextView) findViewById(R.id.tv_seclect);
        tvRetailer = (TextView) findViewById(R.id.tv_retailer);
        tvArea = (TextView) findViewById(R.id.tv_area);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvUpdate = (TextView) findViewById(R.id.tv_update);
        iv_orange_left = (ImageView) findViewById(R.id.iv_orange_left);
        iv_orange_right = (ImageView) findViewById(R.id.iv_orange_right);
        ll_retailer = (RelativeLayout) findViewById(R.id.ll_retailer);
        ll_area = (RelativeLayout) findViewById(R.id.ll_area);
        ll_location = (RelativeLayout) findViewById(R.id.ll_location);

        ivCalendar.setOnClickListener(this);
//        etPostalCode.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        tvMtatus.setOnClickListener(this);
        tvRange.setOnClickListener(this);
        tvNationality.setOnClickListener(this);
        tvTyle.setOnClickListener(this);
        tvConcerns.setOnClickListener(this);
        tvSensitivity.setOnClickListener(this);
        tvSeclect.setOnClickListener(this);
        ll_retailer.setOnClickListener(this);
        ll_area.setOnClickListener(this);
        ll_location.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
        tvSex.setOnClickListener(this);
        tv_occupation.setOnClickListener(this);

        etFirstName.setOnFocusChangeListener(this);
        etLastName.setOnFocusChangeListener(this);
        etEmail.setOnFocusChangeListener(this);
        etEmailRight.setOnFocusChangeListener(this);
        etAddress.setOnFocusChangeListener(this);
        etPostalCode.setOnFocusChangeListener(this);
        initData();
    }

    private void initData() {
        bonus = AveneApplication.getInstance().dialogBean.bonus;
        tv_bonus_text.setText("Complete 100% of your profile to get additional " + bonus + " bonus points");
        tvAreaCode.setText(sp.getValue(Constant.AREACODE));
        tvMobile.setText(sp.getValue(Constant.PHONE));
        showPD("Upload...");
        MyAsy myAsy = new MyAsy();
        myAsy.setFlag("0");
        myAsy.execute();
        MyAsy1 myAsy0 = new MyAsy1();
        myAsy0.setFlag("00");
        myAsy0.execute();
        MyAsy1 myAsy1 = new MyAsy1();
        myAsy1.setFlag("11");
        myAsy1.execute();
        MyAsy1 myAsy2 = new MyAsy1();
        myAsy2.setFlag("22");
        myAsy2.execute();
        MyAsy1 myAsy3 = new MyAsy1();
        myAsy3.setFlag("33");
        myAsy3.execute();
        MyAsy1 myAsy4 = new MyAsy1();
        myAsy4.setFlag("44");
        myAsy4.execute();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_calendar:
                BirthDayDlg(this, tvBirthday, tvBirthday.getText().toString());
                break;
            case R.id.tv_update:
                if (isOK()) {
                    per = (int) Math.round(sum / 20 * 100);
                    memberInfoRatio = String.valueOf((double) per / 100.00);
                    getUpdateinfo();
                    showPD("Upload...");
                    UpdateAsy updateAsy = new UpdateAsy();
                    updateAsy.execute();
                }

                break;
            case R.id.tv_sex:
                if (sexlist.size() <= 0) {
                    for (int i = 0; i < Constant.Gender.length; i++) {
                        StringBean db = new StringBean();
                        db.text = Constant.Gender[i];
                        sexlist.add(db);
                    }
                }
                showPopWindow(0, 0, tvSex, sexlist, 2);
                break;
//            case R.id.et_postal_code:
//                if (postalcodelist.size() <= 0) {
//                    MyAsy myAsy = new MyAsy();
//                    myAsy.setFlag("7");
//                    myAsy.execute();
//                } else {
//                    showPopWindow(0, 0, etPostalCode, postalcodelist, 7);
//                }
//                break;
            case R.id.tv_city:
                if (citylist.size() <= 0) {
                    MyAsy1 myAsy1 = new MyAsy1();
                    myAsy1.setFlag("3");
                    myAsy1.execute();
                } else {
                    showPopWindow(0, 0, tvCity, citylist, 8);
                }
                break;
            case R.id.tv_occupation:
                if (occupationlist.size() <= 0) {
                    MyAsy myAsy = new MyAsy();
                    myAsy.setFlag("9");
                    myAsy.execute();
                } else {
                    showPopWindow(0, 0, tv_occupation, occupationlist, 9);
                }
                break;
            case R.id.tv_mtatus://Marital status
                if (statuslist.size() <= 0) {
                    MyAsy myAsy = new MyAsy();
                    myAsy.setFlag("10");
                    myAsy.execute();
                } else {
                    showPopWindow(0, 0, tvMtatus, statuslist, 10);
                }
                break;
            case R.id.tv_range://Income Range
                if (rangelist.size() <= 0) {
                    MyAsy myAsy = new MyAsy();
                    myAsy.setFlag("11");
                    myAsy.execute();
                } else {
                    showPopWindow(0, 0, tvRange, rangelist, 11);
                }
                break;
            case R.id.tv_nationality:
                if (countrylist.size() <= 0) {
                    MyAsy1 myAsy1 = new MyAsy1();
                    myAsy1.setFlag("4");
                    myAsy1.execute();
                } else {
                    showPopWindow(0, 0, tvNationality, countrylist, 12);
                }
                break;
            case R.id.tv_tyle://Skin Type
                if (typeslist.size() <= 0) {
                    MyAsy myAsy = new MyAsy();
                    myAsy.setFlag("13");
                    myAsy.execute();
                } else {
                    showPopWindow(0, 0, tvTyle, typeslist, 13);
                }
                break;
            case R.id.tv_concerns:
                if (concernslist.size() <= 0) {
                    MyAsy myAsy = new MyAsy();
                    myAsy.setFlag("14");
                    myAsy.execute();
                } else {
                    showPopWindow(0, 0, tvConcerns, concernslist, 14);
                }
                break;
            case R.id.tv_sensitivity:
                if (sensitivitylist.size() <= 0) {
                    MyAsy myAsy = new MyAsy();
                    myAsy.setFlag("15");
                    myAsy.execute();
                } else {
                    showPopWindow(0, 0, tvSensitivity, sensitivitylist, 15);
                }
                break;
            case R.id.tv_seclect:
                if (selectlist.size() <= 0) {
                    MyAsy myAsy = new MyAsy();
                    myAsy.setFlag("16");
                    myAsy.execute();
                } else {
                    showPopWindow(0, 0, tvSeclect, selectlist, 16);
                }
                break;
            case R.id.ll_retailer://Retailer Type
                chainText = tvRetailer.getText().toString();
                if (chainlist.size() <= 0) {
                    MyAsy1 myAsy1 = new MyAsy1();
                    myAsy1.setFlag("0");
                    myAsy1.execute();
                } else {
                    showPopWindow(0, 0, tvRetailer, chainlist, 17);
                }
                break;
            case R.id.ll_area:
                areaText = tvArea.getText().toString();
                MyAsy1 myAsy1 = new MyAsy1();
                myAsy1.setFlag("1");
                myAsy1.execute();
                break;
            case R.id.ll_location:
                MyAsy1 myAsy2 = new MyAsy1();
                myAsy2.setFlag("2");
                myAsy2.execute();
                break;

            default:
                break;
        }
    }

    private void getUpdateinfo() {
        address = etAddress.getText().toString();
        postcode = etPostalCode.getText().toString();
        if (!TextUtils.isEmpty(tvBirthday.getText().toString())) {
            String[] bir = tvBirthday.getText().toString().split("/");
            birthday = bir[2] + "-" + bir[1] + "-" + bir[0];
        } else {
            birthday = "";
        }
        email = etEmail.getText().toString() + "@" + etEmailRight.getText().toString();
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        sexId = tvSex.getText().toString().equals("Female") ? "0" : "1";
    }


    //验证是否可以下一步
    private boolean isOK() {
        if (TextUtils.isEmpty(etFirstName.getText().toString())) {
            Toast.makeText(this, "firstname can not null", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etLastName.getText().toString())) {
            Toast.makeText(this, "lastname can not null", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(tvSex.getText().toString())) {
            Toast.makeText(this, "sex can not null", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            Toast.makeText(this, "Email can not null", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etEmailRight.getText().toString())) {
            Toast.makeText(this, "Email can not null", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPostalCode.getText().toString().trim().length() > 0 && etPostalCode.getText().toString().trim().length() < 6) {
            Toast.makeText(this, "invalid post code", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //计算完成百分比
    private void CompleteNum() {
        is_select[0] = true;
        is_select[1] = true;
        is_select[2] = true;
        is_select[3] = true;
        is_select[4] = true;
        sum = 5.00;

        if (!TextUtils.isEmpty(tvBirthday.getText().toString())) {
            is_select[5] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(etAddress.getText().toString())) {
            is_select[6] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(etPostalCode.getText().toString())) {
            is_select[7] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(tvCity.getText().toString())) {
            is_select[8] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(tv_occupation.getText().toString())) {
            is_select[9] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(tvMtatus.getText().toString())) {
            is_select[10] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(tvRange.getText().toString())) {
            is_select[11] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(tvNationality.getText().toString())) {
            is_select[12] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(tvTyle.getText().toString())) {
            is_select[13] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(tvConcerns.getText().toString())) {
            is_select[14] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(tvSensitivity.getText().toString())) {
            is_select[15] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(tvSeclect.getText().toString())) {
            is_select[16] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(tvRetailer.getText().toString())) {
            is_select[17] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(tvArea.getText().toString())) {
            is_select[18] = true;
            sum += 1;
        }
        if (!TextUtils.isEmpty(tvLocation.getText().toString())) {
            is_select[19] = true;
            sum += 1;
        }

        int per = (int) Math.round(sum / 20 * 100);
        iv_orange_left.setVisibility(View.VISIBLE);
        progress.setProgress(per);
        if (per == 100) {
            iv_orange_right.setVisibility(View.VISIBLE);
        } else {
            iv_orange_right.setVisibility(View.GONE);
        }
    }

    private void progressChange() {
        int per = (int) Math.round(sum / 20 * 100);
        iv_orange_left.setVisibility(View.VISIBLE);
        progress.setProgress(per);
        if (per == 100) {
            iv_orange_right.setVisibility(View.VISIBLE);
        } else {
            iv_orange_right.setVisibility(View.GONE);
        }
    }

    private void clearFoucs() {
        etFirstName.clearFocus();
        etLastName.clearFocus();
        etEmail.clearFocus();
        etEmailRight.clearFocus();
        etAddress.clearFocus();
        etPostalCode.clearFocus();
    }

    /**
     * popwindow
     *
     * @param x x位置
     * @param y y位置
     */
    public void showPopWindow(int x, int y, final TextView tv, final List<StringBean> list, final int current) {
        CloseKeyboard();
        clearFoucs();
        View popv1 = LayoutInflater.from(this).inflate(R.layout.pop, null);
        FontManager.applyFont(this, popv1, Constant.TTFNAME);

        if (popSort == null) {
            popSort = new PopupWindow(popv1, tv.getWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        popSort.setFocusable(true);
        popSort.setBackgroundDrawable(new BitmapDrawable());
        popSort.setOutsideTouchable(true);
        popSort.update();
        popSort.showAsDropDown(tv, x, y);
        popSort.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                popSort = null;
            }
        });
        ListView listview = (ListView) popv1.findViewById(R.id.lv_choice);
        sortAdapter = new PopAdapter(this, list);
        listview.setAdapter(sortAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                tv.setText(list.get(position).text);
                switch (current) {
                    case 8:
                        cityId = list.get(position).id;
                        break;
                    case 12:
                        countryId = list.get(position).id;
                        break;
//                    case 7:
//                        postcode = list.get(position).text;
//                        break;
                    case 9:
                        occupationid = list.get(position).id;
                        break;
                    case 11:
                        rangeid = list.get(position).id;
                        break;
                    case 16:
                        selectid = list.get(position).id;
                        break;
                    case 15:
                        sensitivityid = list.get(position).id;
                        break;
                    case 14:
                        concernsid = list.get(position).id;
                        break;
                    case 13:
                        typesid = list.get(position).id;
                        break;
                    case 10:
                        statusid = list.get(position).id;
                        break;
                    case 17:
                        chainId = list.get(position).id;
                        if (!chainText.equals(list.get(position).text)) {
                            tvArea.setText("");
                            tvLocation.setText("");
                            regionId = "0";
                            favorCounterId = "";
                            is_select[18] = false;
                            is_select[19] = false;
                            CompleteNum();
                        }
                        break;
                    case 18:
                        regionId = list.get(position).id;
                        if (!areaText.equals(list.get(position).text)) {
                            tvLocation.setText("");
                            favorCounterId = "";
                            is_select[19] = false;
                            CompleteNum();
                        }
                        break;
                    case 19:
                        favorCounterId = list.get(position).id;
                        break;
                    default:
                        break;
                }
                popSort.dismiss();
                if (!is_select[current]) {
                    is_select[current] = true;
                    sum += 1;
                    progressChange();
                }
            }
        });
    }

    private void BirthDayDlg(Context mContext, TextView tv, String candler) {
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
//                >= Build.VERSION_CODES.LOLLIPOP) {
//            setDatePickerDividerColor(date, 1);
//        } else {
            setDatePickerDividerColor(date);
//        }
        //初始化事件控件数据
        if (!TextUtils.isEmpty(candler)) {
            String[] str = candler.split("/");
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
                String month1 = month + "";
                String day1 = day + "";
                if (month < 10) {
                    month1 = "0" + month;
                }
                if (day < 10) {
                    day1 = "0" + day;
                }
//                textView.setText(month1 + "/" + day1 + "/" + year);
                textView.setText(day1 + "/" + month1 + "/" + year);
                alertDialog.dismiss();
                if (!is_select[5]) {
                    is_select[5] = true;
                    sum += 1;
                    progressChange();
                }
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_first_name:
                if (!hasFocus) {
                    if (TextUtils.isEmpty(etFirstName.getText().toString()) && is_select[0]) {
                        is_select[0] = false;
                        sum -= 1;
                        progressChange();
                    } else if (!TextUtils.isEmpty(etFirstName.getText().toString()) && !is_select[0]) {
                        is_select[0] = true;
                        sum += 1;
                        progressChange();
                    }
                }
                break;
            case R.id.et_last_name:
                if (!hasFocus) {
                    if (TextUtils.isEmpty(etLastName.getText().toString()) && is_select[1]) {
                        is_select[1] = false;
                        sum -= 1;
                        progressChange();
                    } else if (!TextUtils.isEmpty(etLastName.getText().toString()) && !is_select[1]) {
                        is_select[1] = true;
                        sum += 1;
                        progressChange();
                    }
                }
                break;
            case R.id.et_email:
                if (!hasFocus) {
                    if (TextUtils.isEmpty(etEmail.getText().toString()) && is_select[3]) {
                        is_select[3] = false;
                        sum -= 1;
                        progressChange();
                    } else if (!TextUtils.isEmpty(etEmail.getText().toString()) && !is_select[3]) {
                        is_select[3] = true;
                        sum += 1;
                        progressChange();
                    }
                }
                break;
            case R.id.et_email_right:
                if (!hasFocus) {
                    if (TextUtils.isEmpty(etEmailRight.getText().toString()) && is_select[4]) {
                        is_select[4] = false;
                        sum -= 1;
                        progressChange();
                    } else if (!TextUtils.isEmpty(etEmailRight.getText().toString()) && !is_select[4]) {
                        is_select[4] = true;
                        sum += 1;
                        progressChange();
                    }
                }
                break;
            case R.id.et_address:
                if (!hasFocus) {
                    if (TextUtils.isEmpty(etAddress.getText().toString()) && is_select[6]) {
                        is_select[6] = false;
                        sum -= 1;
                        progressChange();
                    } else if (!TextUtils.isEmpty(etAddress.getText().toString()) && !is_select[6]) {
                        is_select[6] = true;
                        sum += 1;
                        progressChange();
                    }
                }
                break;
            case R.id.et_postal_code:
                if (!hasFocus) {
                    if (TextUtils.isEmpty(etPostalCode.getText().toString()) && is_select[7]) {
                        is_select[7] = false;
                        sum -= 1;
                        progressChange();
                    } else if (!TextUtils.isEmpty(etPostalCode.getText().toString()) && !is_select[7]) {
                        is_select[7] = true;
                        sum += 1;
                        progressChange();
                    }
                }
                break;
            default:
                break;
        }
    }

    class UpdateAsy extends AsyncTask<Object, Integer, String> {
        //执行耗时操作
        @Override
        protected String doInBackground(Object... params) {
            //请求接口
            String result = null;
            HttpUtils httpUtils = new HttpUtils();
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("securityKey", "abc123$");
            hashMap.put("infoChannelId", "1");
            hashMap.put("address", address);
            hashMap.put("birthday", birthday);
            hashMap.put("firstName", firstName);
            hashMap.put("lastName", lastName);
            hashMap.put("sexId", sexId);
            hashMap.put("email", email);
            hashMap.put("cityId", cityId);
            hashMap.put("countryId", countryId);
            hashMap.put("favorCounterId", favorCounterId);
            hashMap.put("postcode", postcode);
            hashMap.put("memberInfoRatio", memberInfoRatio);
            hashMap.put("accountId", sp.getValue(Constant.ACCOUNTID));
            hashMap.put("mobileCode", sp.getValue(Constant.AREACODE).replace("+", ""));
            hashMap.put("mobileNumber", sp.getValue(Constant.PHONE));
            hashMap.put("password", sp.getValue(Constant.PASSWORD));
            String str = "\t\t\t\t<surveyResultList>" + occupationid + "</surveyResultList>\n" +
                    "\t\t\t\t<surveyResultList>" + concernsid + "</surveyResultList>\n" +
                    "\t\t\t\t<surveyResultList>" + selectid + "</surveyResultList>\n" +
                    "\t\t\t\t<surveyResultList>" + rangeid + "</surveyResultList>\n" +
                    "\t\t\t\t<surveyResultList>" + sensitivityid + "</surveyResultList>\n" +
                    "\t\t\t\t<surveyResultList>" + typesid + "</surveyResultList>\n" +
                    "\t\t\t\t<surveyResultList>" + statusid + "</surveyResultList>\n";
            hashMap.put("list", str);
            result = httpUtils.putParam(hashMap, Urls.UPDATEMEMBERINFO);
            return result;
        }

        //通知进度改变
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //doInBackground执行完成之后执行结果处理  可以更新UI
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cancelPD();
            if (result.equals("error")) {
                ErrorUtils.showErrorMsg(MyProfileActivity.this, "404");
                return;
            }
            UpdateXmlPull(result);
        }
    }

    class MyAsy extends AsyncTask<Object, Integer, String> {
        public String flag;

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
            hashMap.put("accountId", sp.getValue(Constant.ACCOUNTID));
            result = httpUtils.putParam(hashMap, Urls.GETMEMBERINFO);
            return result;
        }

        //通知进度改变
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //doInBackground执行完成之后执行结果处理  可以更新UI
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("error")) {
                ErrorUtils.showErrorMsg(MyProfileActivity.this, "404");
                return;
            }
            jsonPull(result, getFlag());
        }
    }

    class MyAsy1 extends AsyncTask<Object, Object, String> {

        public String flag; // 0/00--Chain信息     1/11--城市下属区域信息

        // 2/22--柜台信息      3/33--城市信息      4/44--国籍
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
            if (currentFlag.equals("0") || currentFlag.equals("00")) {
                result = httpUtils.putParam(hashMap, Urls.GETCHAINLIST);
            } else if (currentFlag.equals("1") || currentFlag.equals("11")) {
                hashMap.put("chainId", chainId);
                result = httpUtils.putParam(hashMap, Urls.GETAREALIST);
            } else if (currentFlag.equals("2") || currentFlag.equals("22")) {
                hashMap.put("chainId", chainId);
                hashMap.put("regionId", regionId);
                result = httpUtils.putParam(hashMap, Urls.GETCOUNTERLIST);
            } else if (currentFlag.equals("3") || currentFlag.equals("33")) {
                result = httpUtils.putParam(hashMap, Urls.GETCITYLIST);
            } else if (currentFlag.equals("4") || currentFlag.equals("44")) {
                result = httpUtils.putParam(hashMap, Urls.GETCOUNTRYLIST);
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
            if (result.equals("error")) {
                ErrorUtils.showErrorMsg(MyProfileActivity.this, "404");
                return;
            }
            String currentFlag = getFlag();
            if (currentFlag.equals("0") || currentFlag.equals("00")) {
                ChainXmlPull(result, currentFlag);
            } else if (currentFlag.equals("1") || currentFlag.equals("11")) {
                AreaXmlPull(result, currentFlag);
            } else if (currentFlag.equals("2") || currentFlag.equals("22")) {
                CounterXmlPull(result, currentFlag);
            } else if (currentFlag.equals("3") || currentFlag.equals("33")) {
                CityXmlPull(result, currentFlag);
            } else if (currentFlag.equals("4") || currentFlag.equals("44")) {
                CountryXmlPull(result, currentFlag);
            }
        }
    }

    private void UpdateXmlPull(String result) {
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
                                ErrorUtils.showErrorMsg(MyProfileActivity.this, code);
                                return;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        break;
                }
                eventType = parser.next();
            }
            sp.putValue(Constant.LASTNAME, lastName);
            sp.putValue(Constant.FIRSTNAME, firstName);
            if (per == 100) {
                iv_orange_right.setVisibility(View.VISIBLE);
            } else {
                iv_orange_right.setVisibility(View.GONE);
            }
            if (per == 100) {
                DialogUtils.ProfileDlg(this, "100%  Completed", "Thank you! You have completed 100% of your profile.\n" +
                        "Award of " + bonus + " bonus points is limited only to the 1st completion of 100% of your profile.\n" +
                        "Bonus points will be credited to your account within 48 hours. ", 1, 0);
            } else {
                DialogUtils.ProfileDlg(this, String.valueOf(per) + "%  Completed", "Your profile has been updated successfully. Thank you.\n" +
                        "Complete 100% of your profile to receive a ONE-OFF " + bonus + " bonus points.", 2, 0);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void jsonPull(String result, String flag) {
        //解析返回数据
        JSONObject jsonobject = null;
        try {
            jsonobject = XML.toJSONObject(result);
            JSONObject jsonobj = jsonobject.getJSONObject("soap:Envelope")
                    .getJSONObject("soap:Body")
                    .getJSONObject("ns2:getMemberInfoResponse")
                    .getJSONObject("return");
            String jsonresult = jsonobj.toString();
            Gson gson = new Gson();
            Type type = new TypeToken<MemberInfoReturnBean>() {
            }.getType();

            MemberInfoReturnBean memberinfo = gson.fromJson(jsonresult, type);
            Object obj = null;

            // 没返回surveyResultList字段情况下
            try {
                obj = jsonobj.getJSONObject("memberInfoPO").get("surveyResultList");
            } catch (Exception e) {
                obj = "";
            }

            String objresult = obj.toString();
            String[] surveyresult = new String[]{""};
            if (objresult.contains("[")) {
                surveyresult = objresult.substring(1, objresult.length() - 1).split(",");
            } else {
                surveyresult[0] = objresult;
            }

            if (!TextUtils.isEmpty(memberinfo.memberInfoPO.firstName)) {
                etFirstName.setText(memberinfo.memberInfoPO.firstName);
            }

            if (!TextUtils.isEmpty(memberinfo.memberInfoPO.lastName)) {
                etLastName.setText(memberinfo.memberInfoPO.lastName);
            }
            if (!TextUtils.isEmpty(memberinfo.memberInfoPO.address)) {
                etAddress.setText(memberinfo.memberInfoPO.address);
            }

            if (!TextUtils.isEmpty(memberinfo.memberInfoPO.cityId)) {
                cityId = memberinfo.memberInfoPO.cityId;
            }

            if (!TextUtils.isEmpty(memberinfo.memberInfoPO.countryId)) {
                countryId = memberinfo.memberInfoPO.countryId;
            }

            if (!TextUtils.isEmpty(memberinfo.memberInfoPO.postcode)) {
                etPostalCode.setText(memberinfo.memberInfoPO.postcode);
            }

            if (!TextUtils.isEmpty(memberinfo.memberInfoPO.retailerType)) {
                retailerTypeid = memberinfo.memberInfoPO.retailerType;
            }

            if (!TextUtils.isEmpty(memberinfo.memberInfoPO.area)) {
                areaid = memberinfo.memberInfoPO.area;
            }

            if (!TextUtils.isEmpty(memberinfo.memberInfoPO.storeLocation)) {
                favorCounterId = memberinfo.memberInfoPO.storeLocation;
            }

            if (!TextUtils.isEmpty(memberinfo.memberInfoPO.sexId)) {
                if (memberinfo.memberInfoPO.sexId.equals("0")) {
                    tvSex.setText("Female");
                } else {
                    tvSex.setText("Male");
                }
            } else {
                tvSex.setText("gender");
            }
            String email = memberinfo.memberInfoPO.email;
            if (!TextUtils.isEmpty(email)) {
                String str[] = email.split("@");
                etEmail.setText(str[0]);
                etEmailRight.setText(str[1]);
            } else {
                etEmail.setText(getResources().getString(R.string.app_name));
                etEmailRight.setText("yahoo.com");
            }

            String birthday = memberinfo.memberInfoPO.birthday;
            if (!TextUtils.isEmpty(birthday)) {
                String str[] = birthday.split(" ");
                String bd = str[0];
                String str1[] = bd.split("-");
                String year = str1[0];
                String month = str1[1];
                String day = str1[2];
//                tvBirthday.setText(month + "/" + day + "/" + year);
                tvBirthday.setText(day + "/" + month + "/" + year);
            }
            for (int j = 0; j < memberinfo.memberInfoPO.allSurveyList.size(); j++) {
                Log.e("=========", memberinfo.memberInfoPO.allSurveyList.get(j).surveyQuestionName);
                if (memberinfo.memberInfoPO.allSurveyList.get(j).surveyQuestionName.equals("Occupation")) {
                    if (occupationlist.size() <= 0) {
                        for (int i = 0; i < memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.size(); i++) {
                            StringBean db = new StringBean();
                            db.id = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerId;
                            db.text = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerDesc;
                            occupationlist.add(db);
                        }
                    }
                } else if (memberinfo.memberInfoPO.allSurveyList.get(j).surveyQuestionName.equals("Skin Concerns")) {
                    if (concernslist.size() <= 0) {
                        for (int i = 0; i < memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.size(); i++) {
                            StringBean db = new StringBean();
                            db.id = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerId;
                            db.text = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerDesc;
                            concernslist.add(db);
                        }
                    }
                } else if (memberinfo.memberInfoPO.allSurveyList.get(j).surveyQuestionName.equals("How do you know about us")) {
                    if (selectlist.size() <= 0) {
                        for (int i = 0; i < memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.size(); i++) {
                            StringBean db = new StringBean();
                            db.id = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerId;
                            db.text = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerDesc;
                            selectlist.add(db);
                        }
                    }
                } else if (memberinfo.memberInfoPO.allSurveyList.get(j).surveyQuestionName.equals("Income Range")) {
                    if (rangelist.size() <= 0) {
                        for (int i = 0; i < memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.size(); i++) {
                            StringBean db = new StringBean();
                            db.id = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerId;
                            db.text = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerDesc;
                            rangelist.add(db);
                        }
                    }
                } else if (memberinfo.memberInfoPO.allSurveyList.get(j).surveyQuestionName.equals("Marital Status")) {
                    if (statuslist.size() <= 0) {
                        for (int i = 0; i < memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.size(); i++) {
                            StringBean db = new StringBean();
                            db.id = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerId;
                            db.text = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerDesc;
                            statuslist.add(db);
                        }
                    }
                } else if (memberinfo.memberInfoPO.allSurveyList.get(j).surveyQuestionName.equals("Skin Sensitivity")) {
                    if (sensitivitylist.size() <= 0) {
                        for (int i = 0; i < memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.size(); i++) {
                            StringBean db = new StringBean();
                            db.id = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerId;
                            db.text = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerDesc;
                            sensitivitylist.add(db);
                        }
                    }
                } else if (memberinfo.memberInfoPO.allSurveyList.get(j).surveyQuestionName.equals("Skin types")) {
                    if (typeslist.size() <= 0) {
                        for (int i = 0; i < memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.size(); i++) {
                            StringBean db = new StringBean();
                            db.id = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerId;
                            db.text = memberinfo.memberInfoPO.allSurveyList.get(j).surveyAnswerList.get(i).answerDesc;
                            typeslist.add(db);
                        }
                    }
                }
            }

            for (int j = 0; j < surveyresult.length; j++) {
                for (int i = 0; i < occupationlist.size(); i++) {
                    if (occupationlist.get(i).id.equals(surveyresult[j])) {
                        occupation = occupationlist.get(i).text;
                        occupationid = occupationlist.get(i).id;
                        break;
                    }
                }
                for (int i = 0; i < concernslist.size(); i++) {
                    if (concernslist.get(i).id.equals(surveyresult[j])) {
                        concerns = concernslist.get(i).text;
                        concernsid = concernslist.get(i).id;
                        break;
                    }
                }
                for (int i = 0; i < selectlist.size(); i++) {
                    if (selectlist.get(i).id.equals(surveyresult[j])) {
                        select = selectlist.get(i).text;
                        selectid = selectlist.get(i).id;
                        break;
                    }
                }
                for (int i = 0; i < rangelist.size(); i++) {
                    if (rangelist.get(i).id.equals(surveyresult[j])) {
                        range = rangelist.get(i).text;
                        rangeid = rangelist.get(i).id;
                        break;
                    }
                }
                for (int i = 0; i < sensitivitylist.size(); i++) {
                    if (sensitivitylist.get(i).id.equals(surveyresult[j])) {
                        sensitivity = sensitivitylist.get(i).text;
                        sensitivityid = sensitivitylist.get(i).id;
                        break;
                    }
                }
                for (int i = 0; i < typeslist.size(); i++) {
                    if (typeslist.get(i).id.equals(surveyresult[j])) {
                        types = typeslist.get(i).text;
                        typesid = typeslist.get(i).id;
                        break;
                    }
                }
                for (int i = 0; i < statuslist.size(); i++) {
                    if (statuslist.get(i).id.equals(surveyresult[j])) {
                        status = statuslist.get(i).text;
                        statusid = statuslist.get(i).id;
                        break;
                    }
                }

            }

            if (!TextUtils.isEmpty(occupation)) {
                tv_occupation.setText(occupation);
            }

            if (!TextUtils.isEmpty(concerns)) {
                tvConcerns.setText(concerns);
            }

            if (!TextUtils.isEmpty(select)) {
                tvSeclect.setText(select);
            }

            if (!TextUtils.isEmpty(range)) {
                tvRange.setText(range);
            }

            if (!TextUtils.isEmpty(sensitivity)) {
                tvSensitivity.setText(sensitivity);
            }

            if (!TextUtils.isEmpty(types)) {
                tvTyle.setText(types);
            }
            if (!TextUtils.isEmpty(status)) {
                tvMtatus.setText(status);
            }
            CompleteNum();

            switch (flag) {
//                case "7":
//                    showPopWindow(0, 0, etPostalCode, postalcodelist, 7);
//                    break;
                case "9":
                    showPopWindow(0, 0, tv_occupation, occupationlist, 9);
                    break;
                case "10":
                    showPopWindow(0, 0, tvMtatus, statuslist, 10);
                    break;
                case "11":
                    showPopWindow(0, 0, tvRange, rangelist, 11);
                    break;
                case "13":
                    showPopWindow(0, 0, tvTyle, typeslist, 13);
                    break;
                case "14":
                    showPopWindow(0, 0, tvConcerns, concernslist, 14);
                    break;
                case "15":
                    showPopWindow(0, 0, tvSensitivity, sensitivitylist, 15);
                    break;
                case "16":
                    showPopWindow(0, 0, tvSeclect, selectlist, 16);
                    break;
                case "17":
                    showPopWindow(0, 0, tvRetailer, chainlist, 17);
                    break;
                case "18":
                    showPopWindow(0, 0, tvArea, arealist, 18);
                    break;
                case "19":
                    showPopWindow(0, 0, tvLocation, counterlist, 19);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
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
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(MyProfileActivity.this, code);
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
            if (flag.equals("0")) {
                showPopWindow(0, 0, tvRetailer, chainlist, 17);
            } else {
                for (int i = 0; i < chainlist.size(); i++) {
                    if (retailerTypeid.equals(chainlist.get(i).id)) {
                        tvRetailer.setText(chainlist.get(i).text);
                        break;
                    }
                }
                CompleteNum();
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
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(MyProfileActivity.this, code);
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
            // 1 点击后显示  11 不显示
            if (flag.equals("1")) {
                showPopWindow(0, 0, tvArea, arealist, 18);
            } else {
                for (int i = 0; i < arealist.size(); i++) {
                    if (areaid.equals(arealist.get(i).id)) {
                        tvArea.setText(arealist.get(i).text);
                        break;
                    }
                }
                CompleteNum();
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
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(MyProfileActivity.this, code);
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
            for (int i = 0; i < counterlist.size(); i++) {
                if (favorCounterId.equals(counterlist.get(i).id)) {
                    tvLocation.setText(counterlist.get(i).text);
                    break;
                }
            }
            CompleteNum();
            // 2 点击后显示  22 不显示
            if (flag.equals("2")) {
                showPopWindow(0, 0, tvLocation, counterlist, 19);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CityXmlPull(String result, String flag) {
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
                        citylist.clear();
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(MyProfileActivity.this, code);
                                return;
                            }
                        }
                        if (name.equals("cityList")) {
                            db = new StringBean();
                        } else if (db != null) {
                            if (name.equals("cityId")) {
                                db.id = parser.nextText();// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("cityName")) {
                                db.text = parser.nextText();
                            } else if (name.equalsIgnoreCase("counterCode")) {
                                db.counterCode = parser.nextText();
                            } else if (name.equalsIgnoreCase("cityCode")) {
                                db.cityCode = parser.nextText();
                            } else if (name.equalsIgnoreCase("postCode")) {
                                db.postCode = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equals("cityList") && db != null) {
                            citylist.add(db);
                            db = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            for (int i = 0; i < citylist.size(); i++) {
                StringBean db1 = new StringBean();
                db1.text = citylist.get(i).postCode;
                postalcodelist.add(db1);
                if (cityId.equals(citylist.get(i).id)) {
                    tvCity.setText(citylist.get(i).text);
                }
            }
            CompleteNum();
            // 3 点击后显示  33 不显示
            if (flag.equals("3")) {
                showPopWindow(0, 0, tvCity, citylist, 8);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CountryXmlPull(String result, String flag) {
        //解析返回数据
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();
            StringBean db = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 只要不是文档结束事件
                switch (eventType) {
                    // 文档开始事件,可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        countrylist = new ArrayList<StringBean>();
                        break;
                    //开始读取标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();// 获取解析器当前指向的元素的名称
                        if (name.equals("exitCode")) {
                            String code = parser.nextText();
                            if (!code.equals("0")) {
                                ErrorUtils.showErrorMsg(MyProfileActivity.this, code);
                                return;
                            }
                        }
                        if (name.equals("countryList")) {
                            db = new StringBean();
                        } else if (db != null) {
                            if (name.equals("countryId")) {
                                db.id = parser.nextText();// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("countryName")) {
                                db.text = parser.nextText();
                            } else if (name.equalsIgnoreCase("countryMobileCode")) {
                                db.countryMobileCode = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        //读完一个countryList，可以将其添加到集合类中
                        if (parser.getName().equals("countryList") && db != null) {
                            countrylist.add(db);
                            db = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            for (int i = 0; i < countrylist.size(); i++) {
                if (countryId.equals(countrylist.get(i).id)) {
                    tvNationality.setText(countrylist.get(i).text);
                    break;
                }
            }
            CompleteNum();
            // 4 点击后显示  44 不显示
            if (flag.equals("4")) {
                showPopWindow(0, 0, tvNationality, countrylist, 12);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
