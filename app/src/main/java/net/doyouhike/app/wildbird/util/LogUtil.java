package net.doyouhike.app.wildbird.util;

import android.text.TextUtils;
import android.util.Log;

import net.doyouhike.app.wildbird.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 13-11-5.
 * Log
 */
public class LogUtil {
    private static final String TAG=LogUtil.class.getSimpleName();
    public static final boolean releaseStatus = BuildConfig.DEBUG;

    public static void d(String title, String content) {
        if (TextUtils.isEmpty(content))
            return;
        if (releaseStatus) {
            Log.d(TAG+" "+title , content);
        }
    }
    public static void d(String content) {
        if (TextUtils.isEmpty(content))
            return;
        if (releaseStatus) {
            Log.d(TAG, content);
        }
    }

    public static void dCurrentTime(String title) {
        if (releaseStatus) {
            long currentTime = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sdf.format(new Date(currentTime));

            Log.d(TAG+" " + title, date);
        }
    }
}
