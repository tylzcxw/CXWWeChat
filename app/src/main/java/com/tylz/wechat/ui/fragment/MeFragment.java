package com.tylz.wechat.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lqr.optionitemview.OptionItemView;
import com.tylz.wechat.R;
import com.tylz.wechat.app.AppConst;
import com.tylz.wechat.manager.BroadcastManager;
import com.tylz.wechat.ui.activity.MainActivity;
import com.tylz.wechat.ui.activity.SettingActivity;
import com.tylz.wechat.ui.base.BaseFragment;
import com.tylz.wechat.ui.presenter.MeFgPresenter;
import com.tylz.wechat.ui.view.IMeFgView;
import com.tylz.wechat.util.UIUtils;
import com.tylz.wechat.widget.CustomDialog;

import butterknife.Bind;
import io.rong.imlib.model.UserInfo;

/**
 * @author cxw
 * @date 2017/12/12
 * @des 我界面
 */

public class MeFragment extends BaseFragment<IMeFgView,MeFgPresenter> implements IMeFgView{
    private CustomDialog mQrCardDialog;

    @Bind(R.id.llMyInfo)
    LinearLayout mLlMyInfo;
    @Bind(R.id.ivHeader)
    ImageView mIvHeader;
    @Bind(R.id.tvName)
    TextView mTvName;
    @Bind(R.id.tvAccount)
    TextView mTvAccount;
    @Bind(R.id.ivQRCordCard)
    ImageView mIvQRCordCard;

    @Bind(R.id.oivAlbum)
    OptionItemView mOivAlbum;
    @Bind(R.id.oivCollection)
    OptionItemView mOivCollection;
    @Bind(R.id.oivWallet)
    OptionItemView mOivWallet;
    @Bind(R.id.oivCardPaket)
    OptionItemView mOivCardPaket;

    @Bind(R.id.oivSetting)
    OptionItemView mOivSetting;
    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_me;
    }

    @Override
    protected MeFgPresenter createPresenter() {
        return new MeFgPresenter((MainActivity)getActivity());
    }

    @Override
    public void init() {
        registerBR();
    }

    @Override
    public void initView(View rootView) {
        mIvQRCordCard.setOnClickListener(v -> showQRCard());
        mOivAlbum.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.MY_JIAN_SHU,"天涯浪子"));
        mOivCollection.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.MY_CSDN,"天涯浪子"));
        mOivWallet.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.MY_OSCHINA,"天涯浪子"));
        mOivCardPaket.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.MY_GITHUB,"天涯浪子"));
        mOivSetting.setOnClickListener(v ->((MainActivity)getActivity()).jumpToActivity(SettingActivity.class));
    }

    private void showQRCard() {
        if(mQrCardDialog == null){
            View  qrCardView = View.inflate(getActivity(), R.layout.include_qrcode_card, null);
            ImageView ivHeader = (ImageView) qrCardView.findViewById(R.id.ivHeader);
            TextView tvName = (TextView) qrCardView.findViewById(R.id.tvName);
            ImageView ivCard = (ImageView) qrCardView.findViewById(R.id.ivCard);
            TextView tvTip = (TextView) qrCardView.findViewById(R.id.tvTip);
            tvTip.setText(UIUtils.getString(R.string.qr_code_card_tip));
            UserInfo userInfo = mPresenter.getUserInfo();
            if(userInfo != null){
                Glide.with(getActivity()).load(userInfo.getPortraitUri()).centerCrop().into(ivHeader);
            }
        }
    }

    @Override
    public void initData() {
        mPresenter.loadUserInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBR();
    }

    private void registerBR() {
        BroadcastManager.getInstance(getActivity()).register(AppConst.CHANGE_INFO_FOR_ME, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.loadUserInfo();
            }
        });
    }

    private void unregisterBR() {
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.CHANGE_INFO_FOR_ME);
    }

    @Override
    public ImageView getIvHeader() {
        return mIvHeader;
    }

    @Override
    public TextView getTvName() {
        return mTvName;
    }

    @Override
    public TextView getTvAccount() {
        return mTvAccount;
    }
}
