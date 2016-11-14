package cn.com.dareway.dwlibrary.dwutils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2016/11/14.
 */

public class LogUtils {
    private static final String LOG_TAG = "NetUtils";

    // Show log
    public static void log(String msg) {
        if (NetUtils.DEBUG && !TextUtils.isEmpty(msg))
            Log.d(LOG_TAG, msg);
    }

    public static void log(String fromat, Object... strs) {
        if (NetUtils.DEBUG && !TextUtils.isEmpty(fromat) && strs != null)
            Log.d(LOG_TAG, String.format(fromat, strs));
    }



    public static void log(String msg, Throwable tr) {
        if (NetUtils.DEBUG && !TextUtils.isEmpty(msg))
            Log.d(LOG_TAG, msg, tr);
    }



}
