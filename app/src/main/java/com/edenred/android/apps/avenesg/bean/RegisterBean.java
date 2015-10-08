package com.edenred.android.apps.avenesg.bean;

import java.io.Serializable;

/**
 * Created by zhaoxin on 2015/8/26.
 * 保存注册有关字段
 */
public class RegisterBean implements Serializable{

    private static final long serialVersionUID = 1L;
    public String sexId;//性别
    public String mobileCode;//手机号国家前缀
    public String mobileNumber;//会员手机号
    public String firstName;//会员姓名
    public String lastName;//会员姓名
    public String email;//邮箱
    public String password;//密码
    public String date;//日期
}
