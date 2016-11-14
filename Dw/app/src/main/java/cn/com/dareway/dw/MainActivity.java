package cn.com.dareway.dw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.dareway.dwlibrary.netutils.factory.CookieCallBack;
import cn.com.dareway.dwlibrary.netutils.httputils.CookieEntity;
import cn.com.dareway.dwlibrary.netutils.httputils.HttpCallBack;
import cn.com.dareway.dwlibrary.netutils.httputils.DWHttpClient;

public class MainActivity extends AppCompatActivity {

    private String TAG=MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HashMap <String,String> paras=new HashMap<>();
            paras.put("username","ggg");
        paras.put("password","123");
        DWHttpClient.setCookieJar(null, new CookieCallBack() {
            @Override
            public void getCookies(List<CookieEntity> entities) {
                for (CookieEntity c:entities
                     ) {
                    Log.d(TAG, "getCookies: "+c.getDomain()+";;"+c.getName()+";;"+c.getName());
                }
            }
        });
        Map<String,String> header=new HashMap<>();
        header.put("aa","cccc");
        DWHttpClient.setHeader(header);
        DWHttpClient.doHttpPost("http://10.1.83.78:8080/moas/common/login","aa", paras, new HttpCallBack<String>() {
            @Override
            public void onFailure(String reason) {
                Log.d(TAG, "onFailure: "+reason);
            }

            @Override
            public void onSuccess(String response, int code) {
                Log.d(TAG, "onSuccess: code::"+code+";;response::"+response);
            }

            @Override
            public void onHeader(List<String> headers) {
                for (String h:headers
                     ) {
                    Log.d(TAG, "onHeader: header::"+h);
                }
            }


        });


        DWHttpClient.doHttpPost("http://10.1.83.78:8080/moas/common/login",null, paras, new HttpCallBack<String>() {
            @Override
            public void onFailure(String reason) {
                Log.d(TAG, "onFailure: "+reason);
            }

            @Override
            public void onSuccess(String response, int code) {
                Log.d(TAG, "onSuccess: code::"+code+";;response::"+response);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            DWHttpClient.cancelCall();
        }
        return true;
    }
}
