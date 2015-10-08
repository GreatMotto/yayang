package com.edenred.android.apps.avenesg;

import android.app.Activity;
import android.app.Application;

import com.edenred.android.apps.avenesg.bean.DialogBean;
import com.edenred.android.apps.avenesg.bean.ProductBean;
import com.edenred.android.apps.avenesg.bean.RegisterBean;
import com.edenred.android.apps.avenesg.constant.Constant;
import com.edenred.android.apps.avenesg.utils.SharedPreferencesHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhaoxin on 2015/7/14.
 */
public class AveneApplication extends Application {

    public static AveneApplication instance;
    private List<Activity> acys = new LinkedList<Activity>();
    private SharedPreferencesHelper sp;
    public RegisterBean memberinfo=new RegisterBean();//注册个人信息
    public ProductBean productbean=new ProductBean();//注册产品信息
    public String registerUniqueCode;//扫码code
    public String purchaseCode;//产品code
    public String PurchaseCounterId;//柜台ID
    public DialogBean dialogBean=new DialogBean();

    public AveneApplication() {
        instance = this;
    }

    public static synchronized AveneApplication getInstance() {
        if (instance == null) {
            instance = new AveneApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);

        ImageLoader.getInstance().init(configuration);
    }

    public SharedPreferencesHelper getSp() {
        if (sp == null) {
            sp = new SharedPreferencesHelper(this, Constant.SP_LOGIN_NAME);
        }
        return sp;
    }

    public void setSp(SharedPreferencesHelper sp) {
        this.sp = sp;
    }

    public void addActivity(Activity acy) {
        acys.add(acy);
    }

    // 完全退出应用
    public void exit() {
        for (Activity ac : acys) {
            ac.finish();
        }
        System.exit(0);
    }

    //清除所有Activity
    public void finishActivity() {
        for (Activity ac : acys) {
            ac.finish();
        }
    }

}
