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
import java.util.concurrent.TimeUnit;

import cn.com.dareway.dwlibrary.netutils.GsonResolver;
import cn.com.dareway.dwlibrary.netutils.Util;
import cn.com.dareway.dwlibrary.netutils.factory.NetHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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

public class OkClient implements NetHttpClient {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected OkHttpClient client;
    protected GsonResolver mResolver; //用来对返回的字符串进行JSon转换
    private int mBufferSize = 2048;
    public static boolean DEBUG = true;
    protected int connectTimeOut = 20;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Object ret;
    private ArrayList<Call> calls;

    public void  getCalls (ArrayList<Call> calls){
            this.calls=calls;
    }


    public OkClient init() {
        calls=new ArrayList<>();
        mResolver = new GsonResolver();
        client = new OkHttpClient.Builder().connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        ResponseBody body = new ForwardResponseBody(response.body());
                        return response.newBuilder()
                                .body(body)
                                .build();
                    }
                }).build();

        return this;
    }

    @Override
    public NetHttpClient setCertInputStream(InputStream certInputStream) {
        return null;
    }

    /**
     * Post 异步请求 提交键值对
     *
     * @param url      请求地址
     * @param paras    请求参数
     * @param callBack 回调接口
     * @param <T>      如需返回String，可将泛型T指定为String类型
     */

    @Override
    public <T> Call doHttpPost(String url, HashMap<String, String> paras, HttpCallBack<T> callBack) {

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
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();


       return AsyncRequset(request, callBack);
    }


    /**
     * Post 异步请求 json字符串
     *
     * @param url
     * @param json
     * @param callBack
     * @param <T>
     */

    @Override
    public <T> Call doHttpPost(String url, String json, HttpCallBack<T> callBack) {

        if (!Util.isNotNull(callBack)) {
            //不能为空
            return null;
        }
        final HttpCallBack<T> resCallBack = callBack;

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
      return   AsyncRequset(request, resCallBack);

    }

    /**
     * Get 异步请求
     *
     * @param url
     * @param callBack
     * @param <T>
     */
    @Override
    public <T> Call doHttpGet(String url, HttpCallBack<T> callBack) {

        if (!Util.isNotNull(callBack)) {
            //不能为空
            return null;
        }

        final HttpCallBack<T> resCallBack = callBack;

        Request request = new Request.Builder().url(url).build();
        return AsyncRequset(request, resCallBack);
    }


    /**
     * 异步上传文件 以提交Form表单的形式上传文件
     *
     * @param url      上传文件的接口地址url 如upload.php
     * @param key      Form表单中的name属相 <inputtype="file" name="pic"></>
     * @param file     需要上传的文件
     * @param callBack 回调接口
     * @param <T>      泛型参数
     */
    @Override
    public <T> Call uploadAsync(String url, String key, File file, HttpCallBack<T> callBack) {

        //  uploadAsync(url, null, callback, new IOParam(key, file));
        IOParam ioParam = new IOParam(key, file);
        RequestBody body = createMultipartBody(new IOParam[]{ioParam});

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        // In this we proxy the ForwardRequestBody to support Progress
        builder.post(new ForwardRequestBody(body));

        Request request = builder.build();
        if (callBack != null) {
            RequestBody requestBody = request.body();
            if (requestBody instanceof ForwardRequestBody) {
                ((ForwardRequestBody) (requestBody)).setListener(callBack);
            }
        } else {

            return null;
        }

        return AsyncRequset(request, callBack);

    }


    /**
     * 异步下载文件
     *
     * @param url      下载的文件的url地址
     * @param fileStr  下载到本地的路径
     * @param callBack 回调接口
     */

    @Override
    public <T> Call downloadAsync(String url, String fileStr, final HttpCallBack<File> callBack) {
        Call call = null;
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.get();
        final Request request = builder.build();
        final File file = new File(fileStr);
        try {
            // On before crete stream, we need make new file
            Util.makeFile(file);

            final FileOutputStream out = new FileOutputStream(file);
            if (!Util.isNotNull(callBack)) {
                //不能为空
                return null;
            }

            call = client.newCall(request);
            calls.add(call);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    if(call.isCanceled())
                    {
                        calls.remove(call);
                    }else {

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

    @Override
    public OkClient connectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        return this;
    }


    /**
     * 发起异步请求
     *
     * @param request  http请求
     * @param callBack 请求后的回调接口
     * @param <T>
     */
    private <T> Call AsyncRequset(Request request, final HttpCallBack<T> callBack) {

        Call call = client.newCall(request);
        calls.add(call);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                    if(call.isCanceled()) {
                        Util.log("onFailure" + e.toString());
                        calls.remove(call);
                    }else {
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

    private RequestBody createMultipartBody(IOParam[] IOParams) {
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
    private void bindResponseProgressCallback(RequestBody requestBody, ResponseBody responseBody, HttpCallBack<?> callback) {
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
