package com.tylz.wechat.ui.activity;

import com.tylz.wechat.R;
import com.tylz.wechat.ui.base.BaseActivity;
import com.tylz.wechat.ui.base.BasePresenter;

/**
 * @author cxw
 * @date 2017/12/12
 * @des 关于界面
 */

public class AboutActivity extends BaseActivity {
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
