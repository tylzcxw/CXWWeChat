package com.tylz.wechat.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cxw
 * @date 2017/12/7
 * @des 正则工具类
 */

public class RegularUtils {
    public static boolean isMobile(String phoneNumber){
        String MOBLIE_PHONE_PATTERN =   "^((13[0-9])|(15[0-9])|(18[0-9])|(14[7])|(17[0|6|7|8]))\\d{8}$";
        Pattern pattern = Pattern.compile(MOBLIE_PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
