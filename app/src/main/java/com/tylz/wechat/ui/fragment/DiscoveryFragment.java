package com.tylz.wechat.ui.fragment;

import com.tylz.wechat.R;
import com.tylz.wechat.ui.activity.MainActivity;
import com.tylz.wechat.ui.base.BaseFragment;
import com.tylz.wechat.ui.presenter.DiscoveryFgPresenter;
import com.tylz.wechat.ui.view.IDiscouveryFgView;

/**
 * @author cxw
 * @date 2017/12/12
 * @des 发现界面
 */

public class DiscoveryFragment extends BaseFragment<IDiscouveryFgView,DiscoveryFgPresenter> implements IDiscouveryFgView {
    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected DiscoveryFgPresenter createPresenter() {
        return new DiscoveryFgPresenter((MainActivity)getActivity());
    }
}
