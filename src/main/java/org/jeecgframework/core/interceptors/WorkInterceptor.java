package org.jeecgframework.core.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.jeecg.zwzx.utils.ContextHolderUtils;

import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.util.SignatureUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

/**
 * API接口签名校验机制 <br/>
 * 描述：{ 拦截 /api/**的请求，匹配请求header中的参数X-JEECG-SIGN，是否与服务器签名一致 }
 * date: 2017-4-1 <br/>
 * @author dangzhenghui  
 *
 */
public class WorkInterceptor implements HandlerInterceptor {
    private static final String SIGN_KEY = "26F72780372E84B6CFAED6F7B19139CC47B1912B6CAED753";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
    	String id = request.getParameter("id");
        JSONObject j=new JSONObject();
    	if(id!=null){
        	HttpSession session = ContextHolderUtils.getSession();
        	String wuId=(String) session.getAttribute("wuId");
        	if(id.equals(wuId)){
        		return true;
        	}
    	}
        j.put("success","false");
        response.getWriter().print(j.toJSONString());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
