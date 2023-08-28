package com.example.chaoshan.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chaoshan.MyApplication;


public class SharedPreferenceUtil {


    private static final String SHAREDPREFERENCES_NAME = "my_sp";
    private static final String SP_USER_NAME = "userName";
    private static final String SP_USER_PWD = "userPwd";
    private static final String SP_USER_ID = "userNid";
    private static final String SP_USER_GENDER = "userGender";

    public static SharedPreferences getAppSp() {
        return MyApplication.getInstance().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }


    public static String getUserName() {
        return getAppSp().getString(SP_USER_NAME, "");
    }

    public static void setUserName(String name) {
        getAppSp().edit().putString(SP_USER_NAME, name).apply();
    }



    public static String getPwd() {
        return getAppSp().getString(SP_USER_PWD, "");
    }

    public static void setPwd(String pwd) {
        getAppSp().edit().putString(SP_USER_PWD, pwd).apply();
    }


    public static void setUserId(String path) {
        getAppSp().edit().putString(SP_USER_ID, path).apply();
    }

    public static String getUserId() {
        return getAppSp().getString(SP_USER_ID, "");
    }


    public static void setUserGender(int path) {
        getAppSp().edit().putInt(SP_USER_GENDER, path).apply();
    }


    public static int getUserGender() {
        return getAppSp().getInt(SP_USER_GENDER, 0);
    }

}
