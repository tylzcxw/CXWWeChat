package com.tylz.wechat.ui.presenter;

import android.net.Uri;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.tylz.wechat.R;
import com.tylz.wechat.api.ApiRetrofit;
import com.tylz.wechat.db.DBManager;
import com.tylz.wechat.db.model.Friend;
import com.tylz.wechat.model.cache.UserCache;
import com.tylz.wechat.model.response.GetUserInfoByIdResponse;
import com.tylz.wechat.ui.base.BaseActivity;
import com.tylz.wechat.ui.base.BasePresenter;
import com.tylz.wechat.ui.view.IMeFgView;
import com.tylz.wechat.util.LogUtils;
import com.tylz.wechat.util.UIUtils;

import io.rong.imlib.model.UserInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author cxw
 * @date 2017/12/12
 * @des TODO
 */

public class MeFgPresenter extends BasePresenter<IMeFgView> {
    private UserInfo mUserInfo;
    private boolean isFirst = true;
    public MeFgPresenter(BaseActivity context) {
        super(context);
    }

    public void loadUserInfo() {
        mUserInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
        if(mUserInfo == null || isFirst){
            isFirst = false;
            ApiRetrofit.getInstance().getUserInfoById(UserCache.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getUserInfoByIdResponse -> {
                        if(getUserInfoByIdResponse != null && getUserInfoByIdResponse.getCode() == 200){
                            GetUserInfoByIdResponse.ResultEntity result = getUserInfoByIdResponse.getResult();

                            mUserInfo = new UserInfo(UserCache.getId(), result.getNickname(), Uri.parse(result.getPortraitUri()));
                            if (TextUtils.isEmpty(mUserInfo.getPortraitUri().toString())) {
                                mUserInfo.setPortraitUri(Uri.parse(DBManager.getInstance().getPortraitUri(mUserInfo)));
                            }
                            DBManager.getInstance().saveOrUpdateFriend(new Friend(mUserInfo.getUserId(), mUserInfo.getName(), mUserInfo.getPortraitUri().toString()));
                            fillView();
                        }
                    },this::loadError);
        }else{
            fillView();
        }
    }

    private void fillView() {
        if(mUserInfo != null){
            Glide.with(mContext).load(mUserInfo.getPortraitUri()).centerCrop().into(getView().getIvHeader());
            getView().getTvAccount().setText(UIUtils.getString(R.string.my_chat_account, mUserInfo.getUserId()));
            getView().getTvName().setText(mUserInfo.getName());
        }
    }

    private void loadError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }
}
