package com.edenred.android.apps.avenesg.bean;

import java.io.Serializable;

/**
 * Created by wangwm on 2015/8/12.
 * 会员信息
 */
public class NotifyMessageBean implements Serializable{

    private static final long serialVersionUID = 1L;
    public String messageId;//消息唯一标识，根据该字段返回消息已读状态
    public String messageTitle;//推送的标题
    public String sendMessageDate;//yyyy-mm-dd 推送日期
    public String messageContent;//推送的内容
    public String messageImageUrl;//消息中显示图片的路径（news feed等使用）

}
