package com.edenred.android.apps.avenesg.bean;

import java.io.Serializable;

/**
 * Created by zhaoxin on 2015/8/12.
 * 调查信息
 */
public class SurveyAnswerBean implements Serializable{
    public static final long serialVersionUID = 1L;
    public String answerId;//调查答案唯一标识
    public String answerDesc;//答案描述
}