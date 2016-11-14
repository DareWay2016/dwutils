package cn.com.dareway.dwlibrary.netutils.okhttp;


import cn.com.dareway.dwlibrary.netutils.factory.HttpFactory;
import cn.com.dareway.dwlibrary.netutils.factory.NetHttpClient;

/**
 * Created by myn on 2016/10/17.
 */

public class OkHttpFactory implements HttpFactory {

    private NetHttpClient client;

    @Override
    public NetHttpClient createHttpClient() {

        return new OkClient();
    }

    @Override
    public NetHttpClient createHttpsClient() {
        return new OkHttpsClient();
    }


}
