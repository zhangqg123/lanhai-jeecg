package org.jeecgframework.web.linksucai.oauth2.qiye.util;


import org.jeecgframework.web.linksucai.oauth2.qiye.enums.EnumMethod;
import org.jeecgframework.web.linksucai.oauth2.qiye.pojo.AccessToken;
import org.jeecgframework.web.linksucai.oauth2.qiye.pojo.WXjsTicket;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * 公众平台通用接口工具类
 * 
 */
public class WechatAccessToken {
	// 获取微信公众号：access_token的接口地址（GET） 限2000（次/天）
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 获取企业号access_token
	public final static String company_access_token_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=CORPID&corpsecret=CORPSECRET";
	 //	获得微信jssdk 票据jsapi_ticket
	public static String JSAPIURL = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESS_TOKEN";	

	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret, int type) {
		AccessToken accessToken = null;
		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		if (type == 1) {
			requestUrl = company_access_token_url.replace("CORPID", appid).replace("CORPSECRET", appsecret);
			System.err.println(requestUrl);
		}
		JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, EnumMethod.GET.name(), null);
		if(jsonObject==null){
			jsonObject = WeixinUtil.httpRequest(requestUrl, EnumMethod.GET.name(), null);
		}
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// 获取token失败
			}
		}
		return accessToken;
	}
	
	public static WXjsTicket getWXjsTicket(String accessToken) {
		WXjsTicket wXjsTicket = null;
		String requestUrl= JSAPIURL.replace("ACCESS_TOKEN", accessToken);
		// 发起GET请求获取凭证
		JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);
		System.out.println("CommonUtil.java 调用了一次getWXjsTicket接口");
		if (null != jsonObject) {
			try {
				wXjsTicket = new WXjsTicket();
				wXjsTicket.setJsTicket(jsonObject.getString("ticket"));
				wXjsTicket.setJsTicketExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				wXjsTicket = null;
				// 获取wXjsTicket失败
//				log.error("获取wXjsTicket失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return wXjsTicket;
	}

}