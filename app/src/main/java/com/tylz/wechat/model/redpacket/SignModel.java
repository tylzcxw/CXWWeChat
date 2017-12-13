package com.tylz.wechat.model.redpacket;

/**
 * @author cxw
 * @date 2017/12/5
 * @des 云账户签名模型
 */

public class SignModel {
    public String partner;

    public String user_id;

    public String timestamp;

    public String sign;

    public String reg_hongbao_user;

    @Override
    public String toString() {
        return "SignModel{" +
                "partner='" + partner + '\'' +
                ", user_id='" + user_id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                ", reg_hongbao_user='" + reg_hongbao_user + '\'' +
                '}';
    }
}
