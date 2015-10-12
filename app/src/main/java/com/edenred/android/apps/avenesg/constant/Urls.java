package com.edenred.android.apps.avenesg.constant;

public interface Urls {

    /* 服务器地址 */
    public final static String HOST = "http://211.152.32.174:8080/AveneSGWebservice/service/appService?wsdl";// 上海URL
//    public final static String HOST = "http://202.56.138.174:8080/AveneSGWebservice/service/appService?wsdl";// 新加坡URL

    /* 获取对话框数据 */
    public static final String GETDIALOGNAME = "http://211.152.32.174:8080/AveneAPP/webservice/dictionary/info";// 上海URL
//    public static final String GETDIALOGNAME = "http://202.56.138.174:8080/AveneAPP/webservice/dictionary/info";// 新加坡URL
//    public static final String GETDIALOGNAME = "http://10.58.168.85:8080/avene/webservice/dictionary/info";// 本地

    /*图片IP和端口号*/
    public static final String IPANDPORT = "http://211.152.32.174:8080";// 上海
//    public static final String IPANDPORT = "http://202.56.138.174:8080";// 新加坡

    /* 获取礼品分类列表 */
    public static final String REWARDCATEGORY = "getRewardCategoryList";

    /* 获取兑换礼品列表 */
    public static final String GETREDEEMGIFTLIST = "getRedeemGiftList";

    /* 获取产品分类列表 */
    public static final String PRODUCTCATEGORY = "getProductCategoryList";

    /* 获取产品列表 */
    public static final String GETPRODUCTLIST = "getProductList";

    /* 获取会员信息 */
    public static final String GETMEMBERINFO = "getMemberInfo";

    /* 获取国家信息 */
    public static final String GETCOUNTRYLIST = "getCountryList";

    /* 会员登录 */
    public static final String MEMBERLOGIN = "memberLogin";

    /* 获取积分信息 */
    public static final String GETPOINTLIST = "getPointList";

    /* 获取消息推送 */
    public static final String GETNOTIFYMESSAGELIST = "getNotifyMessageList";

    /* 重置密码 */
    public static final String SENDVERIFICATIONINFO = "sendVerificationInfo";

    /* 获取Chain信息 */
    public static final String GETCHAINLIST = "getChainList";

    /* 获取城市下属区域信息 */
    public static final String GETAREALIST = "getAreaList";

    /* 获取柜台信息 */
    public static final String GETCOUNTERLIST = "getCounterList";

    /* 保存兑换信息 */
    public static final String SAVEREDEMPTIONINFO = "saveRedemptionInfo";

    /* 修改密码 */
    public static final String MODIFYPASSWORD = "modifyPassword";

    /* 获取城市信息 */
    public static final String GETCITYLIST = "getCityList";

    /* Product Barcode验证接口 */
    public static final String BARCODEVALIDATION = "barcodeValidation";

    /* Purchase code验证接口 */
    public static final String PURCHASECODEVALIDATION = "purchaseCodeValidation";

    /* 保存或更新会员信息 */
    public static final String UPDATEMEMBERINFO = "updateMemberInfo";

    /* 保存交易信息 */
    public static final String SAVETRANSACTION = "saveTransaction";

    /* 保存或更新会员设置 */
    public static final String UPDATEUSERSETTING = "updateUserSetting";

    /* 验证手机号是否存在 */
    public static final String ISMEMBER = "isMember";

}
