package com.tylz.wechat.app.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import java.util.LinkedList;
import java.util.List;

/**
 * @author cxw
 * @date 2017/12/5
 * @des Application基类
 */

public class BaseApp extends MultiDexApplication {
    public static List<Activity> sActivities = new LinkedList<>();
    /**
     * 上下文
     */
    private static Context sContext;
    /**
     * 主线程
     */
    private static  Thread sMainThread;
    /**
     * 主线程id
     */
    private static long sMainThreadId;
    /**
     * 循环队列
     */
    private static Looper sMainThreadLooper;
    /**
     * 主线程Handler
     */
    private static Handler sMainHandler;

    public static Handler getMainHandler() {
        return sMainHandler;
    }

    public static void setMainHandler(Handler mainHandler) {
        sMainHandler = mainHandler;
    }

    @Override

    public void onCreate() {
        super.onCreate();
        /*
           对全局属性赋值
         */
        sContext = getApplicationContext();
        sMainThread = Thread.currentThread();
        sMainThreadId = android.os.Process.myTid();
        sMainHandler = new Handler();
    }

    /**
     * 完全退出
     * 一般用于“退出程序”功能
     */
    public static void exit(){
        for(Activity activity : sActivities){
            activity.finish();
        }
    }

    /**
     * 重启当前应用
     */
    public static void restart(){
        Intent intent = sContext.getPackageManager().getLaunchIntentForPackage(sContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sContext.startActivity(intent);
    }

    public static List<Activity> getActivities() {
        return sActivities;
    }

    public static void setActivities(List<Activity> activities) {
        sActivities = activities;
    }

    public static Context getContext() {
        return sContext;
    }

    public static void setContext(Context context) {
        sContext = context;
    }

    public static Thread getMainThread() {
        return sMainThread;
    }

    public static void setMainThread(Thread mainThread) {
        sMainThread = mainThread;
    }

    public static long getMainThreadId() {
        return sMainThreadId;
    }

    public static void setMainThreadId(long mainThreadId) {
        sMainThreadId = mainThreadId;
    }

    public static Looper getMainThreadLooper() {
        return sMainThreadLooper;
    }

    public static void setMainThreadLooper(Looper mainThreadLooper) {
        sMainThreadLooper = mainThreadLooper;
    }
}
