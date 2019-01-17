package org.jeecgframework.web.linksucai.oauth2.rule;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.web.linksucai.oauth2.qiye.pojo.AccessToken;
import org.jeecgframework.web.linksucai.oauth2.qiye.util.QiYeUtil;
import org.jeecgframework.web.linksucai.oauth2.qiye.util.Result;
import org.jeecgframework.web.linksucai.oauth2.qiye.util.WeiXinConstants;
import org.jeecgframework.web.linksucai.oauth2.util.OAuth2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeecg.qywx.account.dao.QywxAccountDao;
import com.jeecg.qywx.account.dao.QywxAgentDao;
import com.jeecg.qywx.account.entity.QywxAccount;
import com.jeecg.qywx.base.service.QywxGzuserinfoService;


/**
 * 模板消息接口
 * @author zhangdaihao
 *
 */
@Service
public class RemoteWeixinMethod implements RemoteWeixinMethodI {
//	@Autowired
//	private WeixinAccountServiceI weixinAccountService;
	@Autowired
	private QywxAgentDao qywxAgentDao;
	@Autowired
	private QywxAccountDao qywxAccountDao;
	@Autowired
	private QywxGzuserinfoService qywxGzuserinfoService;

	
	public String callQyAuthor2ReturnUrl(HttpServletRequest request,String agentId,String tagetUrl){
		/**通过Oauth2.0获取openid_end**/
		String qyUserId = ResourceUtil.getQyUserid(request);
		if(oConvertUtils.isEmpty(qyUserId)){
			qyUserId = ResourceUtil.getQyUserId();
		}
		if(StringUtil.isEmpty(qyUserId)){
			String accountId = qywxAgentDao.getAgent(agentId).getAccountId();
			QywxAccount account = qywxAccountDao.get(accountId);
			String code = request.getParameter("code");
//			LogUtil.info("-----author2.0--------code的值--------------"+code);
			//1.模式一：需要授权页面，则判断是否有code值,没有则跳转到授权地址
			if(StringUtil.isEmpty(code)){
//				LogUtil.info("-----------author2.0-------------targetURL的值-------------"+tagetUrl);
				String redirectURL = OAuth2Util.obtainWeixinOAuth2Url(tagetUrl,  account.getCorpid(),OAuth2Util.SNSAPI_BASE);
				return redirectURL;
			}
			// 2.模式一：用户已经关注微信公众账号，不需要授权页面，即可获取了code的值
			if (!"authdeny".equals(code)) {
				AccessToken accessToken = QiYeUtil.getAccessToken(account.getCorpid(), account.getSecret());
//				LogUtil.info("-----accessToken--------的值--------------"+accessToken.getToken());
				if (accessToken != null && accessToken.getToken() != null) {
					qyUserId = getMemberGuidByCode(accessToken.getToken(), code, Integer.parseInt(agentId));
				}
//				LogUtil.info("-----qyUserId--------的值--------------"+qyUserId);
				Map<String, String> userinfo = qywxGzuserinfoService.getGzuserinfo(qyUserId);
//				LogUtil.info("-----userinfo--------的值--------------"+userinfo);
				if(userinfo.get("userid")!=null){
					userinfo.put(WeiXinConstants.QY_AUTH, "auth");
					request.getSession().setAttribute(WeiXinConstants.QY_USERID, userinfo.get("userid"));
					request.getSession().setAttribute(WeiXinConstants.QY_USERINFO, userinfo);
				}
			}
		}
		return null;
	}
	
	public String getMemberGuidByCode(String token, String code, int agentId) {
//		System.out.println("code==" + code + "\ntoken=" + token + "\nagentid=" + agentId);
		Result<String> result = QiYeUtil.oAuth2GetUserByCode(token, code, agentId);
//		System.out.println("result=" + result);
		if (result.getErrcode() == "0") {
			if (result.getObj() != null) {
				// 此处可以通过微信授权用code还钱的Userid查询自己本地服务器中的数据
				return result.getObj();
			}
		}
		return "";
	}

	/**
	 * 方法描述:  获取网页授权凭证
	 * 作    者： Administrator
	 * 日    期： 2015年1月12日-下午10:13:49
	 * @param url
	 * @param oauth2CodePojo
	 * @return 
	 * 返回类型： Map<String,Object>
	 */
//	public  Map<String,Object> getOauth2AccessToken(Oauth2CodePojo oauth2CodePojo) {
//		String requestUrl = WeiXinOpenConstants.WEB_OAUTH_ACCESSTOKEN_URL;
//		requestUrl = requestUrl.replace("APPID", oauth2CodePojo.getAppId());
//		requestUrl = requestUrl.replace("SECRET", oauth2CodePojo.getAppSecret());
//		requestUrl = requestUrl.replace("CODE", oauth2CodePojo.getCode());
//		return callWeixinRemoteMethod(requestUrl, oauth2CodePojo);
//	}


	public RemoteWeixinMethod() {
		super();
		// TODO Auto-generated constructor stub
	}
}
