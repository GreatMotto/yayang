package com.edenred.android.apps.avenesg.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by zhaoxin on 2015/8/12.
 * 个人会员信息
 */
public class MemberInfoBean implements Serializable{
    private static final long serialVersionUID = 1L;
    public String accountId;//会员唯一标识
    public String mobileCode;//手机号国家前缀
    public String mobileNumber;//会员手机号
    public String firstName;//会员姓名
    public String lastName;//会员姓名
    public String accountBalance;//可用积分，可以使用的积分
    public String pointsEarned;//赢取积分，所有获得的积分
    public String pointsRedemed;//兑换积分，所有兑换消耗的积分
    public String pointsExpired;//过期积分，所有过期的积分
    public String willExpiringNextMon;//下个月即将过期的积分
    public String sexId;//姓别  0女  1男
    public String email;//邮箱
    public String birthday;//生日，格式：yyyy-mm-dd
    public String address;//地址
    public String postcode;//邮编
    public String cityId;//城市
    public  String memberInfoRatio;//会员信息填写比例
    public Set<String> surveyResultList;//会员信息调查结果
    public List<SurveyAnswerBean> allSurveyList;//会员信息调查列表
}

