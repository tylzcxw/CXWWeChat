package com.tylz.wechat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.tylz.wechat.R;

/**
 * @author cxw
 * @date 2017/12/6
 * @des TODO
 */

public class ProgressWebView extends WebView {
    private ProgressBar mProgressBar;
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgressBar = new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,8,0,0));
        Drawable drawable = context.getResources().getDrawable(R.drawable.progressbar_webview);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);
        setWebChromeClient(new WebChromeClient());
        //是否可以缩放
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
    }
    public class WebChromeClient extends android.webkit.WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress == 100){
                mProgressBar.setVisibility(GONE);
            }else{
                if(mProgressBar.getVisibility() == GONE){
                    mProgressBar.setVisibility(VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams layoutParams = (LayoutParams) mProgressBar.getLayoutParams();
        layoutParams.x = l;
        layoutParams.y = t;
        mProgressBar.setLayoutParams(layoutParams);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
