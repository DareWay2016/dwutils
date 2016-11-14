package cn.com.dareway.dw;

import android.app.Application;

import java.io.InputStream;

import cn.com.dareway.dwlibrary.netutils.okhttp.OkClient;

/**
 * Created by Administrator on 2016/11/14.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InputStream cert=null;

        OkClient.init(cert,30,30,30);


    }
}
