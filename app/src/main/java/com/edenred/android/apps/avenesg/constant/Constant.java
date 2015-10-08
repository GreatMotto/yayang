package com.edenred.android.apps.avenesg.constant;

/**
 * Created by zhaoxin on 2015/7/14.
 */
public class Constant {

    public final static String AVENE = "avene";

    public final static String CACHE_JSON_DATA_PATH = "/avene/json/";// 缓存json格式数据的路径
    public final static String CACHE_IMAGE_PATH = "/avene/cache";// 缓存图片的路径
    public final static String CRASH_ERROR_FILE_PATH = "/avene/crash/";// 保存报错信息文件的路径
    public final static String CRASH_PIC_PATH = "/avene/pic/";// 保存上传图片的路径

    public final static String UPLOAD_PICTURE_PATH = "/avene/upload/";// 保存着上传图片的路径


    public static final String TESTEMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";// 邮箱验证

    public static final String TTFNAME="font/FtraBk.ttf";
    public static final String ITTFNAME = "font/FtraBkI.ttf";
    // 上次登录用户名SHAREDPREFERENCES
    public static final String SP_LOGIN_NAME = "login";// sp文件名
    public static final String ISLOGIN = "islogin";// 是否登录
    public static final String FLAG = "flag";// 跳转键
    public static final String ACCOUNTID = "accountid";// 会员ID
    public static final String PHONE="phone";//登录手机号
    public static final String AREACODE="areacode";//登录手机号区号
    public static final String FIRSTNAME = "firstname";// firstname
    public static final String LASTNAME="lastname";//lastname
    public static final String SEX="sex";//sex
    public static final String EMAIL="email";//email
    public static final String PASSWORD="password";//password
    public static final String ACCOUNTBALANCE="accountbalance";//可用积分
    public static final String EARNED="earned";//赢取积分
    public static final String REDEMEED="redemeed";//兑换积分
    public static final String EXPIRED="expired";//过期积分
    public static final String WILLEXPIRINGNEXTMON="willexpiringnextmon";//下个月即将过期积分
    public static final String EXPIRINGPOINTSDATE = "expiringpointsdate";//积分过期日期
    public static final String COUNTRYLIST="countrylist";//存储国家列表
    public static final String ISCOUNTRY="iscountry";//判断是否存储国家列表
    public static final String SHOPCARLIST="shopcarlist";//购物车列表
    public static final String ISSHOPCARLIST="isshopcarlist";//判断购物车列表是否为空

    //侧滑菜单字段
    public static final String[] Menu = {"My Profile", "My Points", "Rewards Catalogue", "Scan to Register Purchase"
            , "News and Promotions", "FAQ", "Products", "Contact Us", "Settings",
            "Terms and Conditions", "Change Password", "Logout"};

    //侧滑菜单图标
    public static final int[] Drawable = {com.edenred.android.apps.avenesg.R.mipmap.myacc_ico, com.edenred.android.apps.avenesg.R.mipmap.my_points_ico, com.edenred.android.apps.avenesg.R.mipmap.reward_ico, com.edenred.android.apps.avenesg.R.mipmap.scantoregister_ico
            , com.edenred.android.apps.avenesg.R.mipmap.newsfeed_ico, com.edenred.android.apps.avenesg.R.mipmap.faq_ico, com.edenred.android.apps.avenesg.R.mipmap.products, com.edenred.android.apps.avenesg.R.mipmap.contact_us_ico,
            com.edenred.android.apps.avenesg.R.mipmap.settings_ico, com.edenred.android.apps.avenesg.R.mipmap.term, com.edenred.android.apps.avenesg.R.mipmap.password, com.edenred.android.apps.avenesg.R.mipmap.logout_icon
    };

    //注册向导页面图标
    public static final int[] RegisterImage = {com.edenred.android.apps.avenesg.R.mipmap.step_1, com.edenred.android.apps.avenesg.R.mipmap.step_2, com.edenred.android.apps.avenesg.R.mipmap.trans_details,
            com.edenred.android.apps.avenesg.R.mipmap.personal_info, com.edenred.android.apps.avenesg.R.mipmap.verified
    };

    // 婚姻状况
    public static final String[] MaritalStatus = {"Yes", "No"};

    // 验证手机号码正则表达式
    public static final String CheckSingaporeMobile = "^([89])\\d{7}$";

    // 过滤器数据
    public static final String[] POINT = {"5000", "3000"};
    public static final String[] SHOP = {"Oily&Acene-prone Skin", "Essentials", "Hydration"};

    // 性别
    public static final String[] Gender = {"Female", "Male"};

    // FAQ名称字段
    public static final String[] FAQName = {
            "General Questions",
            "Points and Membership",
            "Redemption"
    };
    // FAQ条目字段
    public static final String[] [] FAQItem = {
            {"What is Avène club?",
            "How can I join Avène club?",
            "What are the perks and privileges that Avène club",
            "How do I update my personal particulars?"},
            {
            "How does the Avène Club Program work?",
            "How long does it take for the Avène Points to be updated in your system?",
            "How do I check the Avène Club Points?",
            "Will my Avène Club points be expired?",
            "Is there an annual membership fee?",
            "Is the membership transferable?"},
            {
            "what should I do if I have problems with scanning of barcode and capturing of receipts?",
            "If I have receipts that were given to me,can I use it to earn Avène Points?",
            "I have placed a redemption order via Avène mobile app,when the Avène products be ready for collection?",
            "Where and how do I collect my redemption rewards?",
            "May I cancel my redemption?"}};
    public static final String[] ImageUrls = {
            "http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg",
            "http://pic20.nipic.com/20120405/7784776_092128535162_2.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2686196000,3974420269&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=3841157212,2135341815&fm=21&gp=0.jpg",
            "http://pica.nipic.com/2007-11-09/2007119124413448_2.jpg",
            "http://pica.nipic.com/2007-11-08/2007118171913119_2.jpg",
    };
}
