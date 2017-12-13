package com.tylz.wechat.model.cache;

import com.tylz.wechat.app.AppConst;
import com.tylz.wechat.db.DBManager;
import com.tylz.wechat.util.SPUtils;
import com.tylz.wechat.util.UIUtils;

/**
 * @author cxw
 * @date 2017/12/5
 * @des TODO
 */

public class UserCache {

    public static String getId() {
        return SPUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.ID, "");
    }

    public static String getPhone() {
        return SPUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.PHONE, "");
    }

    public static String getToken() {
        return SPUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.TOKEN, "");
    }

    public static void save(String id, String account, String token) {
        SPUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.ID, id);
        SPUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.PHONE, account);
        SPUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.TOKEN, token);
    }

    public static void clear() {
        SPUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.ID);
        SPUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.PHONE);
        SPUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.TOKEN);
        DBManager.getInstance().deleteAllUserInfo();
    }
}
