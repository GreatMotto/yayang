package com.edenred.android.apps.avenesg.utils;

import java.text.DecimalFormat;

/**
 * Created by wangwm on 2015/10/10.
 */
public class NumbersFormat {
    public static String thousand(String number){
        return new DecimalFormat().format(Double.parseDouble(number));
    }

    public static String noThousand(String thousandnumber){
        return thousandnumber.replace(",", "");
    }
}
