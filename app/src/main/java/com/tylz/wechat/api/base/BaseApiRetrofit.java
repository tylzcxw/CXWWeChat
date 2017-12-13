package com.tylz.wechat.api.base;

import com.tylz.wechat.api.base.cache.SetCookieCache;
import com.tylz.wechat.api.base.persistence.SharedPrefsCookiePersistor;
import com.tylz.wechat.app.MyApp;
import com.tylz.wechat.util.LogUtils;
import com.tylz.wechat.util.NetUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author cxw
 * @date 2017/12/7
 * @des 配置Retrofit(配置网络缓存cache、配置持久cookie免登录)
 * @des http://www.jianshu.com/p/d21564dcb524
 */

public class BaseApiRetrofit {
    private final OkHttpClient mClient;

    public OkHttpClient getClient() {
        return mClient;
    }

    public BaseApiRetrofit() {
        /*Log信息拦截器*/
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        /*cache*/
        File httpCacheDir = new File(MyApp.getContext().getCacheDir(), "response");
        int cacheSize = 10 * 1024 * 1024;// 10MiB
        Cache cache = new Cache(httpCacheDir, cacheSize);
        /*cookie*/
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApp.getContext()));
        /*OkHttpClient*/
        mClient = new OkHttpClient.Builder()
                .addInterceptor(REWRITE_HEADER_CONTROL_INTERCEPTOR)
                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addInterceptor(new LoggingInterceptor())
                //.addInterceptor(loggingInterceptor)////设置 Debug Log 模式
                .cache(cache)
                .cookieJar(cookieJar)
                .build();
    }

    /*header配置*/
    Interceptor REWRITE_HEADER_CONTROL_INTERCEPTOR = chain -> {
        Request request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                //                .addHeader("Content-Type", "application/json; charset=utf-8")
//                .addHeader("Accept-Encoding", "gzip, deflate")
//                .addHeader("Connection", "keep-alive")
//                .addHeader("Accept", "*/*")
//                .addHeader("Cookie", "add cookies here")
                .build();
        return chain.proceed(request);
    };
    /*cache配置*/
    Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
        //通过CacheControl控制缓存数据
        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.maxAge(0, TimeUnit.SECONDS);//这个是控制缓存的最大生命时间
        cacheBuilder.maxStale(365, TimeUnit.DAYS);//这个是控制缓存的过时时间
        CacheControl cacheControl = cacheBuilder.build();
        //设置拦截器
        Request request = chain.request();
        // 无网络连接时请求从缓存中读取
        if (!NetUtils.isNetworkAvailable(MyApp.getContext())) {
            request = request.newBuilder().cacheControl(cacheControl).build();
        }
        // 响应内容处理
        // 在线时缓存0分钟
        // 离线时缓存4周
        Response originalResponse = chain.proceed(request);
        if (NetUtils.isNetworkAvailable(MyApp.getContext())) {
            int maxAge = 0; // read from cache
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .header("Cache-Control", "public ,max-age=" + maxAge).build();
        } else {
            int maxStale = 60 * 60 * 24 * 28;//tolerate 4-weeks stale
            return originalResponse.newBuilder().removeHeader("Prama")
                    .header("Cache-Control", "poublic, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    };

    class LoggingInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            Request request = chain.request();
            long t1 = System.nanoTime();//请求发起的时间
            LogUtils.d(String.format("发送请求 %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间
           /*
            这里不能直接使用response.body().string()的方式输出日志
            因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            个新的response给应用层处理*/
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            LogUtils.d(String.format("接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
                                      response.request().url(),
                                      responseBody.string(),
                                      (t2 - t1) / 1e6d,
                                      response.headers()));
            return response;
        }
    }
}
