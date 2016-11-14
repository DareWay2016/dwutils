package cn.com.dareway.dwlibrary.dwutils;


import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by myn on 2016/10/14.
 */

public class NetUtils {

    public static boolean DEBUG = true;
    /**
     * @param context
     * @return true if current network is connected , otherwise false
     */
    public static boolean checkCurrentAviNetwork(Context context) {
        if (context == null) {
            throw new NullPointerException("Invalid context object");
        }
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // need to check mobile !=null, because no mobile network data in PAD
        if (wifi.isConnected() || (mobile != null && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

}
