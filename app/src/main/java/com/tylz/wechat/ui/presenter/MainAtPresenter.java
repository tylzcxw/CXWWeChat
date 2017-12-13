package com.tylz.wechat.ui.presenter;

import com.tylz.wechat.R;
import com.tylz.wechat.app.AppConst;
import com.tylz.wechat.app.MyApp;
import com.tylz.wechat.db.DBManager;
import com.tylz.wechat.manager.BroadcastManager;
import com.tylz.wechat.model.cache.UserCache;
import com.tylz.wechat.ui.base.BaseActivity;
import com.tylz.wechat.ui.base.BasePresenter;
import com.tylz.wechat.ui.view.IMainAtView;
import com.tylz.wechat.util.LogUtils;
import com.tylz.wechat.util.UIUtils;

import io.rong.imlib.RongIMClient;

/**
 * @author cxw
 * @date 2017/12/12
 * @des TODO
 */

public class MainAtPresenter extends BasePresenter<IMainAtView> {
    public MainAtPresenter(BaseActivity context) {
        super(context);
        connect(UserCache.getToken());
        //同步所有用户信息
        DBManager.getInstance().getAllUserInfo();
    }

    /**
     * 建立与融云服务器的连接
     * @param token
     */
    private void connect(String token) {
        if(UIUtils.getContext().getApplicationInfo().packageName.equals(MyApp.getCurProcessName(UIUtils.getContext()))){
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    LogUtils.e("--onTokenIncorrect");
                }
                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    LogUtils.e("--onSuccess---" + userid);
                    BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                }
                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtils.e("--onError" + errorCode);
                    UIUtils.showToast(UIUtils.getString(R.string.disconnect_server));
                }
            });
        }
    }
}
