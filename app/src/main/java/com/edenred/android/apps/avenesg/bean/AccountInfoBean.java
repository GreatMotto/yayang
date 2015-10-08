package com.edenred.android.apps.avenesg.bean;

import java.io.Serializable;

/**
 * Created by wangwm on 2015/8/10.
 * 会员信息
 */
public class AccountInfoBean implements Serializable{

    private static final long serialVersionUID = 1L;
    public String accountId;//会员唯一标识
    public String firstName;//会员姓名
    public String lastName;//会员姓名
    public String accountBalance;//可用积分，可以使用的积分
    public String pointsEarned;//赢取积分，所有获得的积分
    public String pointsRedemed;//兑换积分，所有兑换消耗的积分
    public String pointsExpired;//过期积分，所有过期的积分
    public String willExpiringNextMon;//下个月即将过期的积分
    public String expiringPointsDate;//积分过期日期
    public String activationStatus;//激活状态   1-已激活   2-未激活

}
