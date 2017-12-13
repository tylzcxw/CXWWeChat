package com.tylz.wechat.ui.activity;

import android.view.View;

import com.lqr.optionitemview.OptionItemView;
import com.tylz.wechat.R;
import com.tylz.wechat.app.AppConst;
import com.tylz.wechat.app.MyApp;
import com.tylz.wechat.model.cache.UserCache;
import com.tylz.wechat.ui.base.BaseActivity;
import com.tylz.wechat.ui.base.BasePresenter;
import com.tylz.wechat.widget.CustomDialog;

import butterknife.Bind;
import io.rong.imlib.RongIMClient;

/**
 * @author cxw
 * @date 2017/12/12
 * @des 设置界面
 */

public class SettingActivity extends BaseActivity {
    private View mExitView;

    @Bind(R.id.oivAbout)
    OptionItemView mOivAbout;
    @Bind(R.id.oivHelpFeedback)
    OptionItemView mOivHelpFeedback;
    @Bind(R.id.oivExit)
    OptionItemView mOivExit;
    private CustomDialog mExitDialog;
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
    @Override
    public void initListener() {
        mOivAbout.setOnClickListener(v -> jumpToActivity(AboutActivity.class));
        mOivHelpFeedback.setOnClickListener(v1 -> jumpToWebViewActivity(AppConst.WeChatUrl.HELP_FEED_BACK));
        mOivExit.setOnClickListener(v -> {
            if (mExitView == null) {
                mExitView = View.inflate(this, R.layout.dialog_exit, null);
                mExitDialog = new CustomDialog(this, mExitView, R.style.MyDialog);
                mExitView.findViewById(R.id.tvExitAccount).setOnClickListener(v1 -> {
                    RongIMClient.getInstance().logout();
                    UserCache.clear();
                    mExitDialog.dismiss();
                    MyApp.exit();
                    jumpToActivityAndClearTask(LoginActivity.class);
                });
                mExitView.findViewById(R.id.tvExitApp).setOnClickListener(v1 -> {
                    RongIMClient.getInstance().disconnect();
                    mExitDialog.dismiss();
                    MyApp.exit();
                });
            }
            mExitDialog.show();
        });
    }

}
