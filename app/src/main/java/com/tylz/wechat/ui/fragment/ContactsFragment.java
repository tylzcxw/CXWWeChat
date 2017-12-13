package com.tylz.wechat.ui.fragment;

import com.tylz.wechat.R;
import com.tylz.wechat.ui.activity.MainActivity;
import com.tylz.wechat.ui.base.BaseFragment;
import com.tylz.wechat.ui.presenter.ContactsFgPresenter;
import com.tylz.wechat.ui.view.IContactsFgView;

/**
 * @author cxw
 * @date 2017/12/12
 * @des 通讯录界面
 */

public class ContactsFragment extends BaseFragment<IContactsFgView,ContactsFgPresenter> implements IContactsFgView {
    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected ContactsFgPresenter createPresenter() {
        return new ContactsFgPresenter((MainActivity)getActivity());
    }

    public void showQuickIndexBar(boolean b) {
    }
}
