package com.tylz.wechat.api.redpacket;

import com.tylz.wechat.model.redpacket.SignModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author cxw
 * @date 2017/12/5
 * @des 云账户获取demo签名接口
 */

public interface SignService {
    @GET("api/demo-sign/")
    Call<SignModel> getSignInfo(@Query("uid") String userId,@Query("token") String token);
}
