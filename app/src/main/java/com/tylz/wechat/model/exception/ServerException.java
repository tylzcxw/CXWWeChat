package com.tylz.wechat.model.exception;

import com.tylz.wechat.R;
import com.tylz.wechat.util.UIUtils;

/**
 * @author cxw
 * @date 2017/12/11
 * @des 服务器异常
 */

public class ServerException extends Exception {
    public ServerException(int errorCode) {
        this(UIUtils.getString(R.string.error_code) + errorCode);
    }

    public ServerException(String message) {
        super(message);
    }
}
