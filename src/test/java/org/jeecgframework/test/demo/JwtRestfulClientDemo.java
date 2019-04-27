package org.jeecgframework.test.demo;

import org.jeecgframework.jwt.util.JwtHttpUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * jeecg jwt
 * 接口客户端调用demo
 * @author qinfeng
 *
 */
public class JwtRestfulClientDemo {
	
	public static String getToken(String userName,String password,String xcxId){
		String url = "http://localhost:8080/jeecg/rest/tokens?username="+userName+"&password="+password+"&xcxId="+xcxId;
		System.out.println(" url : "+ url);
		String token= JwtHttpUtil.httpRequest(url, "POST", null);
		return token;
	}
	
	
	//获取黑名单列表
	public static JSONObject getBlackList(String token){
		String url = "http://localhost:8080/jeecg/rest/jeecg/lhSBlacklist";
		JSONObject resp= JwtHttpUtil.httpRequest(url, "GET", null,token);
		return resp;
	}
	
	//创建黑名单
	public static JSONObject createBlackList(String token,String json){
		String url = "http://localhost:8080/jeecg/rest/tsBlackListController";
		JSONObject resp= JwtHttpUtil.httpRequest(url, "POST", json,token);
		return resp;
	}
	
	
	//更新黑名单
	public static JSONObject updateBlackList(String token,String json){
		String url = "http://localhost:8080/jeecg/rest/tsBlackListController";
		JSONObject resp= JwtHttpUtil.httpRequest(url, "PUT", json,token);
		return resp;
	}
	
	
	//删除黑名单
	public static JSONObject deleteBlackList(String token,String id){
		String url = "http://localhost:8080/jeecg/rest/tsBlackListController/"+id;
		JSONObject resp= JwtHttpUtil.httpRequest(url, "DELETE", null,token);
		return resp;
	}
	
	//查询黑名单
	public static JSONObject getBlackList(String token,String id){
		String url = "http://localhost:8080/jeecg/rest/tsBlackListController/"+id;
		JSONObject resp= JwtHttpUtil.httpRequest(url, "GET", null,token);
		return resp;
	}
	
	
	public static void main(String[] args) {
		String token = getToken("15699582810","ad9662f9cc0de458707d487c1ee17170","wx8917dfc0cdb6bf7f");
		System.out.println(" token : "+ token);
//		String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJpbnRlcmZhY2V1c2VyIiwic3ViIjoiaW50ZXJmYWNldXNlciIsImlhdCI6MTU1NjEzNjI1M30.01NdtL4p5TocR22oFA5vf9CQSIduMyKq2qOiIUziVDw";
		
		//添加黑名单
	//	JSONObject jsonObject=new JSONObject();
	//	jsonObject.put("ip","192.168.1.211");
	//	System.out.println("======添加黑名单======="+createBlackList(token,jsonObject.toJSONString()));
		//更新黑名单
//		JSONObject jsonObject=new JSONObject();
//		jsonObject.put("id","402881e75f91017e015f91023f7c0001");
//		jsonObject.put("ip","192.168.0.111");
//		System.out.println("======更新黑名单======="+updateBlackList(token,jsonObject.toJSONString()));
		//删除黑名单
//		System.out.println("======删除黑名单======="+deleteBlackList(token,"402881e75f91017e015f91023f7c0001"));
		//查询黑名单
//		System.out.println("======查询黑名单======="+getBlackList(token,"402881e75f94a099015f94afe9700003"));
		//获取黑名单列表
//		System.out.println("======获取黑名单列表======="+getBlackList(token));
	}

}
