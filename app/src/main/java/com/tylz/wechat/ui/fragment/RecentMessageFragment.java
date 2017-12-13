package com.tylz.wechat.ui.fragment;

import com.tylz.wechat.R;
import com.tylz.wechat.ui.activity.MainActivity;
import com.tylz.wechat.ui.base.BaseFragment;
import com.tylz.wechat.ui.presenter.RecentMessageFgPresenter;
import com.tylz.wechat.ui.view.IRecentMessageFgView;

/**
 * @author cxw
 * @date 2017/12/12
 * @des 最近回话列表界面
 */

public class RecentMessageFragment extends BaseFragment<IRecentMessageFgView,RecentMessageFgPresenter> implements IRecentMessageFgView {
    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_recent_message;
    }

    @Override
    protected RecentMessageFgPresenter createPresenter() {
        return new RecentMessageFgPresenter((MainActivity)getActivity());
    }
}
