package org.jeecgframework.web.activiti.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.jeecgframework.core.util.HttpClientUtil;
import org.jeecgframework.web.activiti.service.TemplateMsg;
import org.jeecgframework.web.activiti.util.WXTemplate;
import org.jeecgframework.web.activiti.util.WXTemplateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateMsgImpl implements TemplateMsg {
	// private static final String SEND_API =
	// "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send";
	// @Autowired
	// private WeixinTokenCache weixinTokenCache;

	// 获取requestUrl
	// public String getRequestUrl() {
	// // 获取accessToken
	// String accessToken=getAccessToken();
	// // requestUrl
	// String requestUrl = SEND_API + "?access_token="+accessToken;
	// return requestUrl;
	// }

	/**
	 * 获取accessToken
	 * 
	 * @return
	 */
	// public String getAccessToken(){
	// String tmpurl =
	// "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//
	// String url = tmpurl.replace("APPID", "wx8917dfc0cdb6bf7f");
	// url = url.replace("APPSECRET", "51b3188118f40db7d673c661c95a0095");
	// JSONObject resultJson =null;
	// String result = httpsRequest(url, "POST", null);
	// return (String) resultJson.get("access_token");
	//
	// }
	// public String httpsRequest(String requestUrl, String requestMethod,
	// String outputStr){
	// try {
	// URL url = new URL(requestUrl);
	// HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	// conn.setDoOutput(true);
	// conn.setDoInput(true);
	// conn.setUseCaches(false);
	// // 设置请求方式（GET/POST）
	// conn.setRequestMethod(requestMethod);
	// conn.setRequestProperty("content-type",
	// "application/x-www-form-urlencoded");
	// // 当outputStr不为null时向输出流写数据
	// if (null != outputStr) {
	// OutputStream outputStream = conn.getOutputStream();
	// // 注意编码格式
	// outputStream.write(outputStr.getBytes("UTF-8"));
	// outputStream.close();
	// }
	// // 从输入流读取返回内容
	// InputStream inputStream = conn.getInputStream();
	// InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
	// "utf-8");
	// BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	// String str = null;
	// StringBuffer buffer = new StringBuffer();
	// while ((str = bufferedReader.readLine()) != null) {
	// buffer.append(str);
	// }
	// // 释放资源
	// bufferedReader.close();
	// inputStreamReader.close();
	// inputStream.close();
	// inputStream = null;
	// conn.disconnect();
	// return buffer.toString();
	// } catch (ConnectException ce) {
	// System.out.println("连接超时：{}");
	// } catch (Exception e) {
	// System.out.println("https请求异常：{}");
	// }
	// return null;
	//
	// }

	public boolean sendTemplateMsg(String requestUrl, String openId,
			String templateId, String page, String formId,
			Map<String, WXTemplateData> data, String emphasisKeyword) {
		boolean result = false;
		WXTemplate template = new WXTemplate();
		template.setTouser(openId);
		template.setTemplate_id(templateId);
		template.setPage("index");
		template.setForm_id(formId);
		template.setData(data);
		template.setEmphasis_keyword("keyword1.DATA");

		String jsonMsg = JSONObject.fromObject(template).toString();
		// String
		// jsonMsg="{\"touser\":\""+openId+"\",\"template_id\":\""+templateId+"\",\"page\":\"index\",\"form_id\":\""+formId+"\",\"data\":{\"keyword1\":{\"color\":\"#173177\",\"value\":\"苹果\"},\"keyword2\":{\"color\":\"#173177\",\"value\":\"2000元\"}},\"emphasis_keyword\":\"keyword1.DATA\"}";
		System.out.println("发送模板消息：" + jsonMsg);
		// 发送模板消息
		String resp = HttpClientUtil.post(requestUrl, jsonMsg, null);
		System.out.println("模板消息返回：" + resp);

		return result;
	}

}