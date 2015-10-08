package com.edenred.android.apps.avenesg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangwm on 2015/8/20.
 */
public class MemberInfoReturnBean implements Serializable {
    public static final long serialVersionUID = 1L;
    public String exitCode;
    public MemberInfoPO memberInfoPO;

    public static class MemberInfoPO {
        public String lastName;
        public String birthday;
        public String accountId;
        public String accountBalance;
        public String cityId;
        public String countryId;
        public String willExpiringNextMon;
        public String expiringPointsDate;
        public String pointsEarned;
        public String postcode;
        public String memberInfoRatio;
        public String sexId;
        public String email;
        public String address;
        public String pointsExpired;
        public String mobileCode;
        public String mobileNumber;
        public String firstName;
        public String pointsRedemed;
        public String retailerType;
        public String area;
        public String storeLocation;
        public List<AllSurveyList> allSurveyList;

        public static class AllSurveyList {
            public String surveyQuestionName;
            public String surveyQuestionId;
            public List<SurveyAnswerList> surveyAnswerList;

            public static class SurveyAnswerList {
                public String answerDesc;
                public String answerId;
            }
        }
    }
}
