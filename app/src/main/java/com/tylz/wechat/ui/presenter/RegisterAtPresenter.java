package com.tylz.wechat.ui.presenter;

import android.text.TextUtils;

import com.tylz.wechat.R;
import com.tylz.wechat.api.ApiRetrofit;
import com.tylz.wechat.app.AppConst;
import com.tylz.wechat.model.cache.UserCache;
import com.tylz.wechat.model.exception.ServerException;
import com.tylz.wechat.model.response.LoginResponse;
import com.tylz.wechat.model.response.RegisterResponse;
import com.tylz.wechat.model.response.VerifyCodeResponse;
import com.tylz.wechat.ui.activity.LoginActivity;
import com.tylz.wechat.ui.activity.MainActivity;
import com.tylz.wechat.ui.base.BaseActivity;
import com.tylz.wechat.ui.base.BasePresenter;
import com.tylz.wechat.ui.view.IRegisterAtView;
import com.tylz.wechat.util.LogUtils;
import com.tylz.wechat.util.RegularUtils;
import com.tylz.wechat.util.UIUtils;

import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author cxw
 * @date 2017/12/7
 * @des TODO
 */

public class RegisterAtPresenter extends BasePresenter<IRegisterAtView> {
    int time = 0;
    private Timer mTimer;
    private Subscription mSubscription;

    public RegisterAtPresenter(BaseActivity context) {
        super(context);
    }

    public void sendCode() {
        String phone = getView().getEtPhone().getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            UIUtils.showToast(UIUtils.getString(R.string.phone_not_empty));
            return;
        }
        if (!RegularUtils.isMobile(phone)) {
            UIUtils.showToast(UIUtils.getString(R.string.phone_format_error));
            return;
        }
        mContext.showWaitingDialog(UIUtils.getString(R.string.please_wait));
        ApiRetrofit.getInstance().checkPhoneAvailable(AppConst.REGION, phone)
                .subscribeOn(Schedulers.io())
//                .flatMap(new Func1<CheckPhoneResponse, Observable<SendCodeResponse>>() {
//                    @Override
//                    public Observable<SendCodeResponse> call(CheckPhoneResponse checkPhoneResponse) {
//                        int code = checkPhoneResponse.getCode();
//                        if(code == 200){
//                            return  ApiRetrofit.getInstance().sendCode(AppConst.REGION,phone);
//                        }else{
//                            return Observable.error(new ServerException(UIUtils.getString(R.string.phone_not_available)));
//                        }
//                    }
//                })
                .flatMap(checkPhoneResponse -> {
                    int code = checkPhoneResponse.getCode();
                    if (code == 200) {
                        return ApiRetrofit.getInstance().sendCode(AppConst.REGION, phone);
                    } else {
                        return Observable.error(new ServerException(UIUtils.getString(R.string.phone_not_available)));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sendCodeResponse -> {
                    mContext.hideWaitingDialog();
                    int code = sendCodeResponse.getCode();
                    if (code == 200) {
                        changeSendCodeBtn();
                    } else {
                        sendCodeError(new ServerException(UIUtils.getString(R.string.send_code_error)));
                    }
                }, this::sendCodeError);

    }

    private void changeSendCodeBtn() {
        /*
            开始一分钟倒计时
            每一秒钟执行一次Task
         */
//        mSubscription = Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                time = 60;
//                TimerTask timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        subscriber.onNext(--time);
//                    }
//                };
//                mTimer = new Timer();
//                //每隔一秒执行一次task
//                mTimer.schedule(timerTask, 0, 1000);
//            }
//        })
        Observable.create((Observable.OnSubscribe<Integer>) subscriber -> {
            time = 60;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    subscriber.onNext(--time);
                }
            };
            mTimer = new Timer();
            //每隔一秒执行一次task
            mTimer.schedule(timerTask, 0, 1000);
        })
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe(time -> {
             if (getView().getBtnSendCode() != null) {
                 if (time >= 0) {
                     getView().getBtnSendCode().setEnabled(false);
                     getView().getBtnSendCode().setText(time + "");
                 }else{
                     getView().getBtnSendCode().setEnabled(true);
                     getView().getBtnSendCode().setText(UIUtils.getString(R.string.send_code_btn_normal_tip));
                 }
             }else{
                 mTimer.cancel();
             }
         },throwable -> LogUtils.e(throwable.getLocalizedMessage()) );

    }

    private void sendCodeError(Throwable throwable) {
        mContext.hideWaitingDialog();
        LogUtils.e(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getMessage());
    }

    public void register() {
        String phone = getView().getEtPhone().getText().toString().trim();
        String password = getView().getEtPwd().getText().toString().trim();
        String nickName = getView().getEtNickName().getText().toString().trim();
        String code = getView().getEtVerifyCode().getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            UIUtils.showToast(UIUtils.getString(R.string.phone_not_empty));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            UIUtils.showToast(UIUtils.getString(R.string.password_not_empty));
            return;
        }
        if (TextUtils.isEmpty(nickName)) {
            UIUtils.showToast(UIUtils.getString(R.string.nickname_not_empty));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            UIUtils.showToast(UIUtils.getString(R.string.vertify_code_not_empty));
            return;
        }
        ApiRetrofit.getInstance().verifyCode(AppConst.REGION,phone,code)
                .flatMap(new Func1<VerifyCodeResponse, Observable<RegisterResponse>>() {
                    @Override
                    public Observable<RegisterResponse> call(VerifyCodeResponse verifyCodeResponse) {
                        int _code = verifyCodeResponse.getCode();
                        if(_code == 200){
                            return ApiRetrofit.getInstance().register(nickName,password,verifyCodeResponse.getResult().getVerification_token());
                        }else{
                            return Observable.error(new ServerException(UIUtils.getString(R.string.vertify_code_error) + _code));
                        }
                    }
                })
                .flatMap(new Func1<RegisterResponse, Observable<LoginResponse>>() {
                    @Override
                    public Observable<LoginResponse> call(RegisterResponse registerResponse) {
                        int _code = registerResponse.getCode();
                        if(_code == 200){
                            return ApiRetrofit.getInstance().login(AppConst.REGION, phone, password);
                        }else{
                            return Observable.error(new ServerException(UIUtils.getString(R.string.register_error) + _code));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginResponse -> {
                    int loginResponseCode = loginResponse.getCode();
                    if(loginResponseCode == 200){
                        UserCache.save(loginResponse.getResult().getId(), phone, loginResponse.getResult().getToken());
                        mContext.jumpToActivityAndClearTask(MainActivity.class);
                        mContext.finish();
                    }else{
                        UIUtils.showToast(UIUtils.getString(R.string.login_error));
                        mContext.jumpToActivity(LoginActivity.class);
                    }

                },this::sendCodeError);
    }
    public void unsubscribe(){
        if(mSubscription != null){
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }
}
