package com.edenred.android.apps.avenesg.utils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2015/10/9.
 */
public class NumbersFormat {
    public static String thousand(String number) {
        int data = Integer.parseInt(number);
        DecimalFormat df = new DecimalFormat();
        return df.format(data);
    }
}
