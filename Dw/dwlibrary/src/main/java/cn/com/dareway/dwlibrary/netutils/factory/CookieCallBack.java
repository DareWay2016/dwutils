package cn.com.dareway.dwlibrary.netutils.factory;

import java.util.List;

import cn.com.dareway.dwlibrary.netutils.okhttp.CookieEntity;

/**
 * Created by Administrator on 2016/11/14.
 */

public interface CookieCallBack {
   void getCookies(List<CookieEntity> entities);
}
