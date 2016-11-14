package cn.com.dareway.dwlibrary.netutils.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.com.dareway.dwlibrary.netutils.CertTool;
import cn.com.dareway.dwlibrary.netutils.GsonResolver;
import cn.com.dareway.dwlibrary.netutils.Util;
import cn.com.dareway.dwlibrary.netutils.factory.CookieCallBack;
import cn.com.dareway.dwlibrary.netutils.factory.NetHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by myn on 2016/10/17.
 * <p>
 * 使用OkHttp实现的用来进行http请求的客户端
 * 使用OkHttp的版本为 okhttp:3.3.1
 */

public class OkClient {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client;
    protected static GsonResolver mResolver; //用来对返回的字符串进行JSon转换
    private static int mBufferSize = 2048;
    public static boolean DEBUG = true;
    protected static int connectTimeOut = 20, writeTimeOut = 20, readTimeOut = 20;
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Object ret;
    private static ArrayList<Call> calls;
    private static InputStream cert = null;
    private static CookieJar cookieJar;
    private static Map<String, String> headers;

    public static void cancelCall() {
        if (calls != null) {
            for (Call c :
                    calls) {
                if (c != null) {
                    c.cancel();
                }
            }

            calls.clear();
        }
    }


    public static void init(InputStream certInputStream, int connectTime, int writeTime, int readTime) {
        cert = certInputStream;
        calls = new ArrayList<>();
        client = null;

        connectTimeOut = connectTime;
        writeTimeOut = writeTime;
        readTimeOut = readTime;
        mResolver = new GsonResolver();
        if (client == null) {
            OkHttpClient.Builder httpsBuilder = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    ResponseBody body = new ForwardResponseBody(response.body());
                    return response.newBuilder()
                            .body(body)
                            .build();
                }
            });
            if (cookieJar != null) {
                httpsBuilder.cookieJar(cookieJar);
            }
            if (connectTimeOut > 0) {
                httpsBuilder.connectTimeout(connectTimeOut, TimeUnit.SECONDS);
            }
            if (readTimeOut > 0) {
                httpsBuilder.readTimeout(readTimeOut, TimeUnit.SECONDS);
            }
            if (writeTimeOut > 0) {
                httpsBuilder.writeTimeout(writeTimeOut, TimeUnit.SECONDS);
            }


            if (cert != null) {

                client = CertTool.setCertificates(httpsBuilder, cert);
            } else {
                client = httpsBuilder.build();
            }
        }

    }

    public static void setHeader(Map<String, String> header) {
        headers = header;
    }

    public static void init(OkHttpClient httpClient) {
        client = httpClient;
    }

    public static NetHttpClient setCertInputStream(InputStream certInputStream) {
        return null;
    }

    /**
     * Post 异步请求 提交键值对
     *
     * @param url        请求地址
     * @param headerName 获取的header的name
     * @param paras      请求参数
     * @param callBack   回调接口
     * @param <T>        如需返回String，可将泛型T指定为String类型
     */


    public static <T> Call doHttpPost(String url, String headerName, HashMap<String, String> paras, HttpCallBack<T> callBack) {

        if (!Util.isNotNull(callBack)) {
            //不能为空
            return null;
        }
        final HttpCallBack<T> resCallBack = callBack;
        FormBody.Builder builder = new FormBody.Builder();


        for (String key : paras.keySet()) {

            builder.add(key, paras.get(key));
        }
        RequestBody formBody = builder.build();
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(formBody);
        if (headers != null && headers.size() > 0) {
            Set<String> headerKeys = headers.keySet();
            for (String key : headerKeys
                    ) {
                requestBuilder = requestBuilder.addHeader(key, headers.get(key));
            }
        }
        Request request = requestBuilder.build();


        return AsyncRequset(request, headerName, callBack);
    }


    /**
     * Post 异步请求 json字符串
     *
     * @param url
     * @param headerName 获取的header的name
     * @param json
     * @param callBack
     * @param <T>
     */


    public static <T> Call doHttpPost(String url, String headerName, String json, HttpCallBack<T> callBack) {

        if (!Util.isNotNull(callBack)) {
            //不能为空
            return null;
        }
        final HttpCallBack<T> resCallBack = callBack;

        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);
        if (headers != null && headers.size() > 0) {
            Set<String> headerKeys = headers.keySet();
            for (String key : headerKeys
                    ) {
                requestBuilder = requestBuilder.addHeader(key, headers.get(key));
            }
        }
        Request request = requestBuilder.build();
        return AsyncRequset(request, headerName, resCallBack);

    }

    /**
     * 设置cookies
     * @param cookie
     * @param callback
     */
    public static void setCookieJar(final List<CookieEntity> cookie, final CookieCallBack callback) {
        cookieJar = new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                List<CookieEntity> ces = new ArrayList<>();
                for (Cookie c : cookies) {
                    CookieEntity ce = new CookieEntity(c.name(), c.value(), c.expiresAt(),
                            c.domain(), c.path(), c.secure(), c.httpOnly(), c.hostOnly());
                    ces.add(ce);
                }
                callback.getCookies(ces);

            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = new ArrayList<>();
                if (cookie != null && cookie.size() > 0) {
                    for (CookieEntity ce : cookie
                            ) {

                       /*
                       String name, String value, long expiresAt, String domain, String path,
      boolean secure, boolean httpOnly, boolean hostOnly
                        */
                        Cookie.Builder b = new Cookie.Builder();
                        if (ce.getName() != null && !"".equals(ce.getName())) {
                            b = b.name(ce.getName());
                        }
                        if (ce.getValue() != null && !"".equals(ce.getValue())) {
                            b = b.value(ce.getValue());
                        }
                        if (ce.getExpiresAt() > 0) {
                            b = b.expiresAt(ce.getExpiresAt());
                        }
                        if (ce.getDomain() != null && !"".equals(ce.getDomain())) {
                            b = b.domain(ce.getDomain());
                        }
                        if (ce.getPath() != null && !"".equals(ce.getPath())) {
                            b = b.path(ce.getPath());
                        }
                        if (ce.isSecure() == true) {
                            b = b.secure();
                        }
                        if (ce.isHostOnly() == true) {
                            b = b.hostOnlyDomain(ce.getDomain());
                        }
                        if (ce.isHttpOnly() == true) {
                            b = b.httpOnly();
                        }
                        Cookie c = b.build();
                        cookies.add(c);
                    }
                }
                return cookies;
            }
        };
        init(cert, connectTimeOut, writeTimeOut, readTimeOut);
    }

    /**
     * Get 异步请求
     *
     * @param url
     * @param headerName 获取的header的name
     * @param callBack
     * @param <T>
     */

    public static <T> Call doHttpGet(String url, String headerName, HttpCallBack<T> callBack) {

        if (!Util.isNotNull(callBack)) {
            //不能为空
            return null;
        }

        final HttpCallBack<T> resCallBack = callBack;

        Request.Builder requestBuilder = new Request.Builder()
                .url(url);
        if (headers != null && headers.size() > 0) {
            Set<String> headerKeys = headers.keySet();
            for (String key : headerKeys
                    ) {
                requestBuilder = requestBuilder.addHeader(key, headers.get(key));
            }
        }
        Request request = requestBuilder.build();
        return AsyncRequset(request, headerName, resCallBack);
    }


    /**
     * 异步上传文件 以提交Form表单的形式上传文件
     *
     * @param url        上传文件的接口地址url 如upload.php
     * @param headerName 获取的header的name
     * @param key        Form表单中的name属相 <inputtype="file" name="pic"></>
     * @param file       需要上传的文件
     * @param callBack   回调接口
     * @param <T>        泛型参数
     */

    public static <T> Call uploadAsync(String url, String headerName, String key, File file, HttpCallBack<T> callBack) {

        //  uploadAsync(url, null, callback, new IOParam(key, file));
        IOParam ioParam = new IOParam(key, file);
        RequestBody body = createMultipartBody(new IOParam[]{ioParam});

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        // In this we proxy the ForwardRequestBody to support Progress
        builder.post(new ForwardRequestBody(body));

        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            Set<String> headerKeys = headers.keySet();
            for (String keys : headerKeys
                    ) {
                requestBuilder = requestBuilder.addHeader(keys, headers.get(keys));
            }
        }
        Request request = requestBuilder.build();
        if (callBack != null) {
            RequestBody requestBody = request.body();
            if (requestBody instanceof ForwardRequestBody) {
                ((ForwardRequestBody) (requestBody)).setListener(callBack);
            }
        } else {

            return null;
        }

        return AsyncRequset(request, headerName, callBack);

    }


    /**
     * 异步下载文件
     *
     * @param url        下载的文件的url地址
     * @param headerName 获取的header的name
     * @param fileStr    下载到本地的路径
     * @param callBack   回调接口
     */


    public static <T> Call downloadAsync(String url, final String headerName, String fileStr, final HttpCallBack<File> callBack) {
        Call call = null;
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.get();
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            Set<String> headerKeys = headers.keySet();
            for (String keys : headerKeys
                    ) {
                requestBuilder = requestBuilder.addHeader(keys, headers.get(keys));
            }
        }
        final Request request = requestBuilder.build();
        final File file = new File(fileStr);
        try {
            // On before crete stream, we need make new file
            Util.makeFile(file);

            final FileOutputStream out = new FileOutputStream(file);
            if (!Util.isNotNull(callBack)) {
                //不能为空
                return null;
            }

            if (client == null) {
                init(cert, connectTimeOut, writeTimeOut, readTimeOut);
            }
            call = client.newCall(request);
            calls.add(call);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    if (call.isCanceled()) {
                        calls.remove(call);
                    } else {

                        call.cancel();
                        calls.remove(call);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailure("下载失败！");
                            }
                        });
                    }


                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    call.cancel();
                    calls.remove(call);
                    if (headerName != null && !"".equals(headerName)) {
                        callBack.onHeader(response.headers(headerName));
                    }
                    InputStream in = null;
                    byte[] buf = new byte[mBufferSize];
                    try {
                        Util.log("onResponse:Code:%d Stream:" + response.code());

                        ResponseBody body = response.body();
                        bindResponseProgressCallback(request.body(), body, callBack);

                        in = body.byteStream();
                        int size;
                        while ((size = in.read(buf)) != -1) {
                            out.write(buf, 0, size);
                            out.flush();
                        }
                        // On success
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(file, response.code());
                            }
                        });

                    } catch (Exception e) {
                        //   Util.log("onResponse Failure:" + response.request().toString());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailure("下载失败！");
                            }
                        });

                    } finally {
                        okhttp3.internal.Util.closeQuietly(in);
                        okhttp3.internal.Util.closeQuietly(out);
                    }
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            callBack.onFailure("下载失败！");
        }
        return call;
    }


    public static void connectTimeOut(int time) {
        connectTimeOut = time;

    }

    public static void writerTimeOut(int time) {
        writeTimeOut = time;

    }

    public static void readTimeOut(int time) {
        readTimeOut = time;

    }


    /**
     * 发起异步请求
     *
     * @param request  http请求
     * @param callBack 请求后的回调接口
     * @param <T>
     */
    private static <T> Call AsyncRequset(Request request, final String headerName, final HttpCallBack<T> callBack) {
        if (client == null) {
            init(cert, connectTimeOut, writeTimeOut, readTimeOut);
        }


        Call call = client.newCall(request);
        if (calls == null) {
            calls = new ArrayList<>();
        }
        calls.add(call);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                if (call.isCanceled()) {
                    Util.log("onFailure" + e.toString());
                    calls.remove(call);
                } else {
                    call.cancel();
                    calls.remove(call);
                    Util.log("onFailure" + e.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailure("请求失败！");
                        }
                    });
                }

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                call.cancel();
                calls.remove(call);
                Util.log("onResponse");
                if (headerName != null && !"".equals(headerName)) {
                    callBack.onHeader(response.headers(headerName));
                }
                try {
                    ret = null;

                    if (response.isSuccessful()) {

                        final String string = response.body().string();
                        final boolean haveValue = !TextUtils.isEmpty(string);

                        if (haveValue) {

                            ret = mResolver.analysis(string, callBack.getClass());
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess((T) ret, response.code());
                            }
                        });


                    } else {
                        throw new IOException("Unexpected code " + response);
                    }

                } catch (Exception e) {

                }
            }
        });
        return call;
    }


    /**
     * 创建用来上传文件的Http请求主体body
     *
     * @param IOParams 一个键值对的数组，IOParam 为键值对
     * @return
     */

    private static RequestBody createMultipartBody(IOParam[] IOParams) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        //builder = buildMultipartBody(builder);

        if (IOParams != null && IOParams.length > 0) {
            for (IOParam param : IOParams) {
                if (param.key != null && param.file != null) {
                    String fileName = param.file.getName();
                    RequestBody fileBody = RequestBody.create(MediaType.parse(Util.getFileMimeType(fileName)), param.file);
                    builder.addFormDataPart(param.key, fileName, fileBody);
                    Util.log("buildMultiFileParam: key: " + param.key + " value: " + fileName);
                } else {
                    Util.log("NetUtils", "buildMultiFileParam: key: "
                            + (param.key != null ? param.key : "null")
                            + " file: "
                            + (param.file != null ? param.file.getName() : "null"));
                }
            }
        }
        return builder.build();
    }


    /**
     * 设置用来检测下载进度的回调接口
     *
     * @param requestBody
     * @param responseBody
     * @param callback
     */
    private static void bindResponseProgressCallback(RequestBody requestBody, ResponseBody responseBody, HttpCallBack<?> callback) {
        if (requestBody instanceof ForwardRequestBody) {
            if (((ForwardRequestBody) requestBody).getListener() != null) {
                return;
            }
        }
        if (responseBody instanceof ForwardResponseBody) {
            ((ForwardResponseBody) responseBody).setListener(callback);
        }
    }
}
