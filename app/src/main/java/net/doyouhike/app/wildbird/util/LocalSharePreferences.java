package net.doyouhike.app.wildbird.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LocalSharePreferences {


    public static final String NM_PREFERENCE_FILE = "wildbird";
    public static final String KEY_USER_INFO = "param1";
    public static final String KEY_LOCK_LOCATION_TIME = "param2";
    public static final String KEY_LOCK_LOCATION = "param3";
    public static final String KEY_LOCK_TIME = "param4";

    public static void commit(Context context, String key, String name) {
        if (null == context)
            return;
        SharedPreferences sp = context.getSharedPreferences(NM_PREFERENCE_FILE, 0);
        Editor edit = sp.edit();
        edit.putString(key, name);
        edit.commit();
    }

    public static void commit(Context context, String key, int value) {
        if (null == context)
            return;
        SharedPreferences sp = context.getSharedPreferences(NM_PREFERENCE_FILE, 0);
        Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void put(Context context, String key, Object value) {
        if (null == context)
            return;
        SharedPreferences sp = context.getSharedPreferences(NM_PREFERENCE_FILE, 0);
        Editor edit = sp.edit();
        if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (boolean) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof String) {
            edit.putString(key, (String) value);
        }
        edit.commit();
    }

    public static String getValue(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(NM_PREFERENCE_FILE, 0);
        String name = sp.getString(key, "");
        return name;
    }

    public static boolean getValue(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(NM_PREFERENCE_FILE, 0);
        boolean value = sp.getBoolean(key, defaultValue);
        return value;
    }

    public static <T> T getValue(Context context, String key, T defaultValue) {

        SharedPreferences sp = context.getSharedPreferences(NM_PREFERENCE_FILE, 0);

        if (defaultValue instanceof Integer) {

            return (T) (Integer) sp.getInt(key, (Integer) defaultValue);

        } else if (defaultValue instanceof String) {

            return (T) sp.getString(key, (String) defaultValue);

        }else if (defaultValue instanceof Long) {

            return (T) (Long)sp.getLong(key, (Long) defaultValue);
        }

        return defaultValue;
    }


    public static int getIntValue(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(NM_PREFERENCE_FILE, 0);

        return sp.getInt(key, 0);
    }


    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NM_PREFERENCE_FILE, 0);
        Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }
}
