package cn.com.dareway.dwlibrary.netutils.factory;

/**
 * Created by myn on 2016/10/17.
 */

public interface  HttpFactory {
    NetHttpClient createHttpClient();
    NetHttpClient createHttpsClient();
}
