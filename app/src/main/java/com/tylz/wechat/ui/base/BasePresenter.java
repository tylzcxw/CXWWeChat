package com.tylz.wechat.ui.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * @author cxw
 * @date 2017/12/5
 * @des TODO
 */

public class BasePresenter<V> {
    /*以下是网络请求接口*/
    public BaseActivity mContext;
    protected Reference<V> mViewRef;
    public BasePresenter(BaseActivity context){
        mContext = context;
    }
    public void attachView(V view){
        mViewRef = new WeakReference<V>(view);
    }
    public boolean isViewAttached(){
        return mViewRef != null && mViewRef.get()!= null;
    }
    public void detachView(){
        if(mViewRef != null){
            mViewRef.clear();
            mViewRef = null;
        }
    }
    public V getView(){
        return mViewRef != null ? mViewRef.get():null;
    }
}
