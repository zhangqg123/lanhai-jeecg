package org.jeecgframework.core.interceptors;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.PayUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.util.SignatureUtil;
import org.jeecgframework.web.system.manager.ClientManager;
import org.jeecgframework.web.system.pojo.base.Client;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * API接口签名校验机制 <br/>
 * 描述：{ 拦截 /api/**的请求，匹配请求header中的参数X-JEECG-SIGN，是否与服务器签名一致 }
 * date: 2017-4-1 <br/>
 * @author dangzhenghui  
 *
 */
public class SignInterceptor implements HandlerInterceptor {
	@Resource
	private ClientManager clientManager;
	
    private static final String SIGN_KEY = "26F72780372E84B6CFAED6F7B19139CC47B1912B6CAED753";
    private static final String APPID = "wx5c51671623808ac0";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
    	StringBuffer url = request.getRequestURL();
        JSONObject j=new JSONObject();
        try {
//    		String sign= request.getHeader("bos_token");
        	String sign=request.getParameter("sign");
        	String status=request.getParameter("status");
			Map<String,String>param=new HashMap<String, String>();
			Enumeration penum=(Enumeration) request.getParameterNames();
			while(penum.hasMoreElements()){
				String pKey=(String) penum.nextElement();
				String value=request.getParameter(pKey);
				//sign和uploadFile不参与 值为空也不参与
				if(!pKey.equals("sign")&&!pKey.equals("uploadFile")&&!pKey.equals("realname")
						&&!pKey.equals("pageNumber")&&!pKey.equals("pageSize")&&StringUtils.isNotBlank(value)){
					param.put(pKey,value);
				}
			}
			String key =null;
			if(status!=null){
				key=APPID;
			}else{
				key=SIGN_KEY;
			}
			String validateSign=PayUtil.createSign(param, key);
			if(StringUtils.isBlank(sign)||!sign.equals(validateSign)){
				throw new BusinessException("签名验证失败");
			}
        } catch (BusinessException e) {
            j.put("success","false");
            j.put("msg",e.getMessage());
            response.getWriter().print(j.toJSONString());
            return false;

        }


       return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
