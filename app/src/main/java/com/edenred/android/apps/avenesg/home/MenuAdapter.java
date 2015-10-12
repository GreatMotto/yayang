package com.edenred.android.apps.avenesg.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edenred.android.apps.avenesg.AveneApplication;
import com.edenred.android.apps.avenesg.BaseActivity;
import com.edenred.android.apps.avenesg.catalogue.CatalogueActivity;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.constant.Urls;
import com.edenred.android.apps.avenesg.login.LoginActivity;
import com.edenred.android.apps.avenesg.login.SubmitEANCodeActivity;
import com.edenred.android.apps.avenesg.points.MyPointActivity;
import com.edenred.android.apps.avenesg.product.ProductsActivity;
import com.edenred.android.apps.avenesg.profile.MyProfileActivity;
import com.edenred.android.apps.avenesg.promotion.NewsAndPromotionsActivity;
import com.edenred.android.apps.avenesg.settings.ChangePwActivity;
import com.edenred.android.apps.avenesg.settings.ContactUsActivity;
import com.edenred.android.apps.avenesg.settings.SettingsActivity;
import com.edenred.android.apps.avenesg.settings.WebViewActivity;
import com.edenred.android.apps.avenesg.utils.FontManager;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.edenred.android.apps.avenesg.utils.ViewHolder;

/**
 * Created by zhaoxin on 2015/7/16.
 * 侧滑菜单适配器
 */
public class MenuAdapter extends BaseAdapter {

    Context mContext;
    private boolean[] is_select = new boolean[Constant.Menu.length];

    public MenuAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return Constant.Menu.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, com.edenred.android.apps.avenesg.R.layout.item_menu, null);
        }
        TextView tv_item = ViewHolder.get(convertView, com.edenred.android.apps.avenesg.R.id.tv_item);
        FontManager.applyFont(mContext, convertView, Constant.TTFNAME);

        Drawable db = mContext.getResources().getDrawable(Constant.Drawable[position]);
        db.setBounds(0, 0, db.getMinimumWidth(), db.getMinimumHeight());
        tv_item.setCompoundDrawables(db, null, null, null);
        tv_item.setCompoundDrawablePadding(16);
        tv_item.setText(Constant.Menu[position]);

        if (is_select[position]) {
            tv_item.setBackgroundColor(mContext.getResources().getColor(com.edenred.android.apps.avenesg.R.color.select_bg));
            is_select[position] = false;
        } else {
            tv_item.setBackgroundColor(mContext.getResources().getColor(com.edenred.android.apps.avenesg.R.color.grey_dark));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_select[position] = !is_select[position];
                notifyDataSetChanged();
//                HomeActivity.instanceHomeAc.toggleMenu();//关闭侧滑栏
                //侧滑栏点击跳转
                switch (position) {
                    case 0://My Profile
                        ((BaseActivity) mContext).gotoOtherActivity(MyProfileActivity.class);
                        break;
                    case 1://My Points
                        ((BaseActivity) mContext).goto1OtherActivity(MyPointActivity.class, 0);
                        break;
                    case 2://Rewards catalogue
                        ((BaseActivity) mContext).goto1AnotherActivity(CatalogueActivity.class, 8);
                        break;
                    case 3://Scan to Register Purchase
                        ((BaseActivity) mContext).goto2OtherActivity(SubmitEANCodeActivity.class, 1, 8);
                        break;
                    case 4://News and Promotions
                        ((BaseActivity) mContext).goto1AnotherActivity(NewsAndPromotionsActivity.class, 8);
                        break;
                    case 5://FAQ
                        Intent intent1 = new Intent(mContext, WebViewActivity.class);
                        intent1.putExtra("flag", "FAQ");
                        intent1.putExtra("url", Urls.IPANDPORT + "/AveneAPP/webservice/FAQ");
                        ((BaseActivity) mContext).startActivity(intent1);
                        break;
                    case 6://Eau Thermale Avène Products
                        ((BaseActivity) mContext).gotoOtherActivity(ProductsActivity.class);
                        break;
                    case 7://Contract Us
                        ((BaseActivity) mContext).gotoOtherActivity(ContactUsActivity.class);
                        break;
                    case 8://Settings
                        ((BaseActivity) mContext).gotoOtherActivity(SettingsActivity.class);
                        break;
                    case 9://Terms and Condition
                        Intent intent2 = new Intent(mContext, WebViewActivity.class);
                        intent2.putExtra("flag", "Terms and Conditions");
                        intent2.putExtra("url", Urls.IPANDPORT + "/AveneAPP/webservice/Terms_and_Conditions");
                        ((BaseActivity) mContext).startActivity(intent2);
                        break;
                    case 10://Change Password
                        ((BaseActivity) mContext).gotoOtherActivity(ChangePwActivity.class);
                        break;
                    case 11://Logout
                        SharedPreferencesHelper sp = AveneApplication.getInstance().getSp();
                        sp.putBooleanValue(Constant.ISLOGIN, false);
                        sp.putValue(Constant.AREACODE, "");
                        sp.putValue(Constant.PHONE, "");
                        sp.putValue(Constant.PASSWORD, "");
                        ((BaseActivity) mContext).gotoOtherActivity(LoginActivity.class);
                        ((BaseActivity) mContext).onBackPressed();
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        });
        return convertView;
    }
}
