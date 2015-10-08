package com.edenred.android.apps.avenesg.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * 
 * SharedPreferences帮助类
 * 
 *
 */
@SuppressLint("CommitPrefEdits")
public class SharedPreferencesHelper {

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    @SuppressWarnings("deprecation")
    public SharedPreferencesHelper(Context context, String filename) {
        sp = context.getSharedPreferences(filename, Context.MODE_WORLD_WRITEABLE);
    }

    public void remove(String KEY) {
        editor = sp.edit();
        editor.remove(KEY);
        editor.commit();
    }

    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    public void putValue(String key, String value) {
        editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key) {
        return sp.getString(key, "");
    }

    public void putBooleanValue(String key, boolean value) {
        editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBooleanValue(String key) {
        return sp.getBoolean(key, false);
    }

    public int getIntValue(String key) {
        return sp.getInt(key, 1);
    }

    public void putIntValue(String key, int value) {
        editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

}
