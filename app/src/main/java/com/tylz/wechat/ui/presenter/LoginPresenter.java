package com.tylz.wechat.ui.presenter;

import android.text.TextUtils;

import com.tylz.wechat.R;
import com.tylz.wechat.api.ApiRetrofit;
import com.tylz.wechat.app.AppConst;
import com.tylz.wechat.model.cache.UserCache;
import com.tylz.wechat.model.exception.ServerException;
import com.tylz.wechat.ui.activity.MainActivity;
import com.tylz.wechat.ui.base.BaseActivity;
import com.tylz.wechat.ui.base.BasePresenter;
import com.tylz.wechat.ui.view.ILoginAtView;
import com.tylz.wechat.util.LogUtils;
import com.tylz.wechat.util.UIUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author cxw
 * @date 2017/12/11
 * @des TODO
 */

public class LoginPresenter extends BasePresenter<ILoginAtView> {
    public LoginPresenter(BaseActivity context) {
        super(context);
    }
    public void login(){
//        UserCache.save("13652304622", "13652304622", "CiAio8OhhF+IiHzbF56NYkBXBB5zTei1T+6f6jn0ihKTtHR+7gdko9OtLCN+NvG2NvvuQcUlPIi1hAHAkf3I/rLoRhHXX3O0");
//        mContext.jumpToActivityAndClearTask(MainActivity.class);
//        mContext.finish();
        String phone = getView().getEtPhone().getText().toString().trim();
        String pwd = getView().getEtPwd().getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            UIUtils.showToast(UIUtils.getString(R.string.phone_not_empty));
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            UIUtils.showToast(UIUtils.getString(R.string.password_not_empty));
            return;
        }
        mContext.showWaitingDialog(UIUtils.getString(R.string.please_wait));
        ApiRetrofit.getInstance().login(AppConst.REGION, phone, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginResponse -> {
                    int code = loginResponse.getCode();
                    mContext.hideWaitingDialog();
                    if(code == 200){
                        UserCache.save(loginResponse.getResult().getId(), phone, loginResponse.getResult().getToken());
                        mContext.jumpToActivityAndClearTask(MainActivity.class);
                        mContext.finish();
                    }else{
                        loginError(new ServerException(UIUtils.getString(R.string.login_error) + code));
                    }
                },this::loginError);
    }

    private void loginError(Throwable throwable) {
        LogUtils.e(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());
        mContext.hideWaitingDialog();
    }
}
