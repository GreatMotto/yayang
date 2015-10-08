package com.edenred.android.apps.avenesg.utils;

import android.content.Context;
import android.widget.Toast;

public class ErrorUtils {

    public static void showErrorMsg(Context context, String error) {
        String errorMsg = "";
//        String[] errorMsgSplits = error.split("_");
//        String errorMsgSplit = errorMsgSplits[0];
//        int errorCodeInt = Integer.valueOf(error);
//        switch (errorCodeInt) {
//            case 0:
//                errorMsg = "成功";
//                break;
//            case -1:
//                errorMsg = "securityKey输入错误";
//                break;
//            case -2:
//                errorMsg = "未知错误";
//                break;
//            case -17:
//                errorMsg = "积分类型错误";
//                break;
//            case -18:
//                errorMsg = "日期格式错误";
//                break;
//            case -10:
//                errorMsg = "通过手机号匹配不到会员";
//                break;
//            case -11:
//                errorMsg = "通过手机号匹配到多个会员";
//                break;
//            case -12:
//                errorMsg = "短信发送失败";
//                break;
//            case -13:
//                errorMsg = "当天发送超过2次限制";
//                break;
//            case -14:
//                errorMsg="产品条形码不存在或已被注册";
//                break;
//            case 9:
//                errorMsg = "手机号重复";
//                break;
//            case 10:
//                errorMsg = "用户已收藏";
//                break;
//            case 11:
//                errorMsg = "原密码错误";
//                break;
//            case 12:
//                errorMsg = "此帐号已被禁用";
//                break;
//            case 13:
//                errorMsg = "此帐号未绑定";
//                break;
//            case 14:
//                errorMsg = "已签到";
//                break;
//            case 404:
//                errorMsg = "Network error";
//                break;
//        }

        if(error.equals("404"))
        {
            errorMsg = "Network error";
        }else
        {
            errorMsg = "error";
        }
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();

    }
}
