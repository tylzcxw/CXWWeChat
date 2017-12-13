package com.tylz.wechat.ui.view;

import android.widget.Button;
import android.widget.EditText;

/**
 * @author cxw
 * @date 2017/12/7
 * @des TODO
 */

public interface IRegisterAtView {
    EditText getEtNickName();

    EditText getEtPhone();

    EditText getEtPwd();

    EditText getEtVerifyCode();

    Button getBtnSendCode();
}
