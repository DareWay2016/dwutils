package cn.com.dareway.dwlibrary.netutils.okhttp;


import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import cn.com.dareway.dwlibrary.netutils.CertTool;
import cn.com.dareway.dwlibrary.netutils.GsonResolver;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by myn on 2016/10/17.
 *
 * 用来进行https请求的client
 *
 */

public class OkHttpsClient extends OkClient {

    private InputStream certInputStream;

    public OkHttpsClient init(){
        mResolver = new GsonResolver();
        OkHttpClient.Builder httpsBuilder = new OkHttpClient.Builder().connectTimeout(connectTimeOut,TimeUnit.SECONDS)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        ResponseBody body = new ForwardResponseBody(response.body());
                        return response.newBuilder()
                                .body(body)
                                .build();
                    }
                });
        client = CertTool.setCertificates(httpsBuilder,certInputStream);

        return this;
    }

    /**
     * 设置证书
     * @param certInputStream
     */
    public OkHttpsClient setCertInputStream(InputStream certInputStream){

        this.certInputStream = certInputStream;
        return this;
    }

    @Override
    public OkHttpsClient connectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        return this;
    }


}
