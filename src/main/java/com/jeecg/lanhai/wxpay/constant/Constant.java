package com.jeecg.lanhai.wxpay.constant;

/**
 * Created by Hyman on 2017/2/27.
 */
public class Constant {

    public static final String DOMAIN = "https://hunchun.zqgzht.com";

    public static final String APP_ID = "wxcf299b6e12f042be";

//    public static final String APP_SECRET = "3fe4c5cee957d802e6dd2d70fe239706";

    public static final String APP_KEY = "zhangqingguo19691001zhanghuiting";

    public static final String MCH_ID = "1536705781";  //商户号

    public static final String URL_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String URL_NOTIFY = Constant.DOMAIN + "/wxpay/views/payInfo.jsp";

    public static final String TIME_FORMAT = "yyyyMMddHHmmss";

    public static final int TIME_EXPIRE = 2;  //单位是day

}
