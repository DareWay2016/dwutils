package cn.com.dareway.dwlibrary.netutils.factory;


import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import cn.com.dareway.dwlibrary.netutils.httputils.HttpCallBack;
import okhttp3.Call;

/**
 * Created by myn on 2016/10/17.
 */

public interface NetHttpClient {

    <T> Call doHttpPost(final String url, HashMap<String,String> paras, HttpCallBack<T> callBack);
    <T> Call doHttpPost(final String url,String json,HttpCallBack<T> callBack);
    <T> Call doHttpGet(final String url,HttpCallBack<T> callBack);
    <T> Call uploadAsync(String url, String key, File file, HttpCallBack<T> callBack);
    <T> Call downloadAsync(String url, String fileStr, final HttpCallBack<File> callBack);
    NetHttpClient connectTimeOut(int connectTimeOut);
    NetHttpClient init();
    NetHttpClient setCertInputStream(InputStream certInputStream);//设置https请求的证书

}
