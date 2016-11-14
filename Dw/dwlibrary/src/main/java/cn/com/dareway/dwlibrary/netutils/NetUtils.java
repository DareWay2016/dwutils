package cn.com.dareway.dwlibrary.netutils;


import android.content.Context;
import android.net.ConnectivityManager;

import cn.com.dareway.dwlibrary.netutils.factory.HttpFactory;
import cn.com.dareway.dwlibrary.netutils.okhttp.OkClient;
import cn.com.dareway.dwlibrary.netutils.okhttp.OkHttpFactory;
import cn.com.dareway.dwlibrary.netutils.okhttp.OkHttpsClient;

/**
 * Created by myn on 2016/10/14.
 */

public class NetUtils {

    public static boolean DEBUG = true;
    private static OkHttpFactory okHttpFactory; //使用OkHttp实现的请求客户端工厂类

    /**
     * 获取 http请求客户端
     * @return
     */
    public static OkClient getOkHttpClient(){

      return  (OkClient) getOkHttpFactory().createHttpClient();

    }

    /**
     * 获取 https 请求客户端
     * @return
     */
    public static OkHttpsClient getOkHttpsClient(){

        return (OkHttpsClient) getOkHttpFactory().createHttpsClient();
    }


    private static HttpFactory getOkHttpFactory(){

        if(okHttpFactory == null){

            okHttpFactory = new OkHttpFactory();
            return okHttpFactory;
        }else {
            return okHttpFactory;
        }

    }
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
