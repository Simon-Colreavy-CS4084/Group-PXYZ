package com.example.recipes.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;


import com.example.recipes.MyApplication;
import com.example.recipes.data.user_info;
import com.google.gson.Gson;

import java.util.Set;


   
public final class LattePreference {

       
    private static final SharedPreferences PREFERENCES =
            PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
    private static final String APP_PREFERENCES_KEY = "recipes";

    private static SharedPreferences getAppPreference() {
        return PREFERENCES;
    }

    public static void setAppProfile(String val) {
        getAppPreference()
                .edit()
                .putString(APP_PREFERENCES_KEY, val)
                .apply();
    }

    public static String getAppProfile() {
        return getAppPreference().getString(APP_PREFERENCES_KEY, null);
    }


    public static void removeAppProfile() {
        getAppPreference()
                .edit()
                .remove(APP_PREFERENCES_KEY)
                .apply();
    }

    public static void clearAppPreferences() {
        getAppPreference()
                .edit()
                .clear()
                .apply();
    }

    public static void setAppFlag(String key, boolean flag) {
        getAppPreference()
                .edit()
                .putBoolean(key, flag)
                .apply();
    }

    public static boolean getAppFlag(String key) {
        return getAppPreference()
                .getBoolean(key, false);
    }

    public static boolean getAppFlag(String key, boolean flag) {
        return getAppPreference()
                .getBoolean(key, flag);
    }

    public static void addCustomAppProfile(String key, String val) {
        if (TextUtils.isEmpty(val)) {
            val = "";
        }
        getAppPreference()
                .edit()
                .putString(key, val)
                .apply();
    }

    public static void setAppString(String key, String val) {
        if (TextUtils.isEmpty(val)) {
            val = "";
        }
        getAppPreference()
                .edit()
                .putString(key, val)
                .apply();
    }

    public static String getAppString(String key) {
        return getAppPreference().getString(key, "");
    }

    public static long getAppLong(String key) {
        return getAppPreference().getLong(key, -1);
    }

    public static void setAppLong(String key, long val) {
        getAppPreference()
                .edit()
                .putLong(key, val)
                .apply();
    }

    public static int getAppInt(String key) {
        return getAppPreference().getInt(key, -1);
    }

    public static int getAppInt(String key, int defValue) {
        return getAppPreference().getInt(key, defValue);
    }

    public static void setAppInt(String key, int val) {
        getAppPreference()
                .edit()
                .putInt(key, val)
                .apply();
    }

    public static String getCustomAppProfile(String key) {
        return getAppPreference().getString(key, "");
    }

    public static Set<String> getAppStringSet(String key) {
        return getAppPreference().getStringSet(key, null);
    }

    public static void setAppStringSet(String key, Set<String> val) {
        getAppPreference()
                .edit().putStringSet(key, val)
                .apply();
    }


    public static void setAppUserInfo(user_info userInfo) {
        if (userInfo != null) {
            Gson gson = new Gson();
            String userStr = gson.toJson(userInfo);
            setAppString("user_info", userStr);
        } else {
            setAppString("user_info", "");
        }
    }

    public static user_info getAppUserInfo() {
        String userStr = getAppPreference().getString("user_info", "");
        if (TextUtils.isEmpty(userStr)) {
            return null;
        }
        Gson gson = new Gson();
        user_info userInfo = gson.fromJson(userStr, user_info.class);
        return userInfo;

    }


}
