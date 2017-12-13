package com.tylz.wechat.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.tylz.wechat.R;
import com.tylz.wechat.ui.base.BaseActivity;
import com.tylz.wechat.ui.base.BasePresenter;
import com.tylz.wechat.widget.ProgressWebView;

import butterknife.Bind;

/**
 * @author cxw
 * @date 2017/12/6
 * @des 内置浏览器界面
 */

public class WebViewActivity extends BaseActivity {
    private Intent mIntent;
    private Bundle mExtras;
    private String mUrl;
    private String mTitle;
    private boolean isLoading = false;
    @Bind(R.id.ibToolbarMore)
    ImageButton mIbToolbarMore;
    @Bind(R.id.webview)
    public ProgressWebView mWebView;

    @Override
    public void init() {
        super.init();
        //得到url
        try{
            mIntent = getIntent();
            mExtras = mIntent.getExtras();
            if(mExtras == null){
                finish();
                return;
            }
            mUrl = mExtras.getString("url");
            if(TextUtils.isEmpty(mUrl)){
                finish();
                return;
            }
            mTitle = mExtras.getString("title");
        }catch (Exception e){
            e.printStackTrace();
            finish();
            return;
        }
    }

    @Override
    public void initView() {
        super.initView();
        mIbToolbarMore.setVisibility(View.VISIBLE);
        //设置webView -- http://blog.csdn.net/kevinscsdn/article/details/52241334
        WebSettings settings = mWebView.getSettings();
        /*
        在API18以上已废弃。不建议调整线程优先级，未来版本不会支持这样做。
        设置绘制线程的优先级。不像其他设置，同一进程中只需调用一次，默认值NORMAL
         */
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        /*
        设置WebView是否支持多窗口。
        如果设置为true，主程序要实现onCreateWindow(WebView, boolean, boolean, Message)，默认false。
         */
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        /*
           让JavaScript自动打开窗口，默认false。适用于JavaScript方法window.open()。
         */
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        /*
        设置最小的字号，默认为8
         */
        settings.setMinimumFontSize(settings.getMinimumLogicalFontSize() + 8);
        /*
        是否允许访问文件，默认允许。
        注意，这里只是允许或禁止对文件系统的访问，Assets 和 resources 文件使用file:///android_asset和file:///android_res仍是可访问的。
        */
        settings.setAllowFileAccess(false);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(mUrl);
        setToolbarTitle(TextUtils.isEmpty(mTitle) ? mWebView.getTitle() : mTitle);
    }

    @Override
    public void initListener() {
        super.initListener();
        mIbToolbarMore.setOnClickListener(v->showShare());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        isLoading = false;
        //如果当前浏览器可以后退，则后退上一个页面
        //这里可以自己实现一个回退栈来回退
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else{
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWebView != null){
            mWebView.removeAllViews();
            try{
                mWebView.destroy();
            }catch (Exception e){
                e.printStackTrace();
            }
            mWebView = null;
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_webview;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //在自己浏览器中跳转
            mWebView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            isLoading = true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            isLoading = false;
        }
    }

    /**
     * 分享 todo
     */
    private void showShare(){

    }
}
