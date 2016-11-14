package cn.com.dareway.dw;

import android.app.Application;

import java.io.InputStream;

import cn.com.dareway.dwlibrary.netutils.httputils.DWHttpClient;

/**
 * Created by Administrator on 2016/11/14.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InputStream cert=null;

        DWHttpClient.init(cert,30,30,30);


    }
}
