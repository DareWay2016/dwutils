package cn.com.dareway.dwlibrary.netutils.okhttp;

import java.util.List;

/**
 * Created by myn on 2016/10/14.
 */

public abstract class  HttpCallBack<T> implements ProgressListener {


     public abstract  void onFailure(String reason);

     public abstract void onSuccess(T response, int code);
     public  void onHeader(List<String> headers){}

     public void dispatchProgress(long current, long count) {
          onProgress(current, count);
     }

     public void onProgress(long current, long count) {
     }

}
