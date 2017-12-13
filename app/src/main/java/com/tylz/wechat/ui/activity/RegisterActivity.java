package com.tylz.wechat.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tylz.wechat.R;
import com.tylz.wechat.ui.base.BaseActivity;
import com.tylz.wechat.ui.presenter.RegisterAtPresenter;
import com.tylz.wechat.ui.view.IRegisterAtView;
import com.tylz.wechat.util.UIUtils;

import butterknife.Bind;

/**
 * @author cxw
 * @date 2017/12/7
 * @des TODO
 */

public class RegisterActivity extends BaseActivity<IRegisterAtView, RegisterAtPresenter> implements IRegisterAtView {
    @Bind(R.id.etNick)
    EditText mEtNick;
    @Bind(R.id.vLineNick)
    View mVLineNick;

    @Bind(R.id.etPhone)
    EditText mEtPhone;
    @Bind(R.id.vLinePhone)
    View mVLinePhone;

    @Bind(R.id.etPwd)
    EditText mEtPwd;
    @Bind(R.id.ivSeePwd)
    ImageView mIvSeePwd;
    @Bind(R.id.vLinePwd)
    View mVLinePwd;

    @Bind(R.id.etVerifyCode)
    EditText mEtVerifyCode;
    @Bind(R.id.btnSendCode)
    Button mBtnSendCode;
    @Bind(R.id.vLineVertifyCode)
    View mVLineVertifyCode;

    @Bind(R.id.btnRegister)
    Button mBtnRegister;
    TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mBtnRegister.setEnabled(canRegister());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private boolean canRegister() {
        int nickNameLength = mEtNick.getText().toString().trim().length();
        int pwdLength = mEtPwd.getText().toString().trim().length();
        int phoneLength = mEtPhone.getText().toString().trim().length();
        int codeLength = mEtVerifyCode.getText().toString().trim().length();
        if (nickNameLength > 0 && pwdLength > 0 && phoneLength > 0 && codeLength > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void initListener() {
        mEtNick.addTextChangedListener(mWatcher);
        mEtPwd.addTextChangedListener(mWatcher);
        mEtPhone.addTextChangedListener(mWatcher);
        mEtVerifyCode.addTextChangedListener(mWatcher);
        mEtNick.setOnFocusChangeListener(new RegisterFocusChangeListener(mVLineNick));
        mEtPwd.setOnFocusChangeListener(new RegisterFocusChangeListener(mVLinePwd));
        mEtPhone.setOnFocusChangeListener(new RegisterFocusChangeListener(mVLinePhone));
        mEtVerifyCode.setOnFocusChangeListener(new RegisterFocusChangeListener(mVLineVertifyCode));
        mIvSeePwd.setOnClickListener(v->{
            if(mEtPwd.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()){
                mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }else{
                mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            mEtPwd.setSelection(mEtPwd.getText().toString().trim().length());
        });
        mBtnSendCode.setOnClickListener(v->{
            if(mBtnSendCode.isEnabled()){
                mPresenter.sendCode();
            }
        });
        mBtnRegister.setOnClickListener(v-> mPresenter.register());
    }

    /**
     * 输入框焦点改变事件
     */
    private class RegisterFocusChangeListener implements View.OnFocusChangeListener{
        private View mLineView;
        public RegisterFocusChangeListener(View lineView){
            mLineView = lineView;
        }
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                mLineView.setBackgroundColor(UIUtils.getColor(R.color.green0));
            } else {
                mLineView.setBackgroundColor(UIUtils.getColor(R.color.line));
            }
        }
    }
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected RegisterAtPresenter createPresenter() {
        return new RegisterAtPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public EditText getEtNickName() {
        return mEtNick;
    }

    @Override
    public EditText getEtPhone() {
        return mEtPhone;
    }

    @Override
    public EditText getEtPwd() {
        return mEtPwd;
    }

    @Override
    public EditText getEtVerifyCode() {
        return mEtVerifyCode;
    }

    @Override
    public Button getBtnSendCode() {
        return mBtnSendCode;
    }
}
