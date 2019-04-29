package org.jeecgframework.jwt.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.util.PasswordUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.jwt.def.JwtConstants;
import org.jeecgframework.jwt.model.TokenModel;
import org.jeecgframework.jwt.service.TokenManager;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.jeecgframework.p3.core.common.utils.AjaxJson;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeecg.ask.utils.LstConstants;
import com.jeecg.lhs.entity.LhSAccountEntity;
import com.jeecg.lhs.entity.LhSUserEntity;
import com.jeecg.lhs.service.LhSAccountService;
import com.jeecg.lhs.service.LhSUserService;
import com.jeecg.lhs.utils.AES128Util;


/**
 * 获取和删除token的请求地址， 
 * 在Restful设计中其实就对应着登录和退出登录的资源映射
 * 
 * @author scott
 * @date 2015/7/30.
 */
@Api(value = "token", description = "鉴权token接口", tags = "tokenAPI")
@Controller
@RequestMapping("/tokens")
public class TokenController {
	private static final Logger logger = LoggerFactory.getLogger(TokenController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private LhSUserService lhSUserService;
	@Autowired
	private LhSAccountService lhSAccountService;
	
//	@ApiOperation(value = "获取TOKEN")
//	@RequestMapping(method = RequestMethod.POST)
//	@ResponseBody
//	public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
//		logger.info("获取TOKEN[{}]" , username);
//		// 验证
//		if (StringUtils.isEmpty(username)) {
//			return new ResponseEntity("用户账号不能为空!", HttpStatus.NOT_FOUND);
//		}
//		// 验证
//		if (StringUtils.isEmpty(username)) {
//			return new ResponseEntity("用户密码不能为空!", HttpStatus.NOT_FOUND);
//		}
//		Assert.notNull(username, "username can not be empty");
//		Assert.notNull(password, "password can not be empty");
//
//		TSUser user = userService.checkUserExits(username, password);
//		if (user == null) {
//			// 提示用户名或密码错误
//			logger.info("获取TOKEN,户账号密码错误[{}]" , username);
//			return new ResponseEntity("用户账号密码错误!", HttpStatus.NOT_FOUND);
//		}
//		// 生成一个token，保存用户登录状态
//		String token = tokenManager.createToken(user);
//		return new ResponseEntity(token, HttpStatus.OK);
//	}
	
	@ApiOperation(value = "获取TOKEN")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson login(@RequestParam String username, @RequestParam String password, @RequestParam String xcxId) {
		logger.info("获取TOKEN[{}]" , username);
		AjaxJson j = new AjaxJson();

		try{
			LhSAccountEntity lhSAccount = lhSAccountService.getByAppId(xcxId);
			String encryptPass = AES128Util.decrypt(password, lhSAccount.getAesKey() ,lhSAccount.getIvKey());
			String password2=PasswordUtil.encrypt(username, encryptPass, PasswordUtil.getStaticSalt());
			LhSUserEntity lhSUser = new LhSUserEntity();
			lhSUser.setUsername(username);
			lhSUser.setPassword(null);
			lhSUser.setXcxid(xcxId);
			MiniDaoPage<LhSUserEntity> list = lhSUserService.getAll(lhSUser, 1, 10);
			List<LhSUserEntity> lhSUserList = list.getResults();
			
			Map<String,Object> attributes=new HashMap<String,Object>();
			lhSUser=null;
			if(lhSUserList.size()>0){
				for(LhSUserEntity user:lhSUserList){
					if(user.getPassword().equals(password2)){
						lhSUser=user;
						break;
					}
				}
				if(lhSUser==null){
					// 提示用户名或密码错误
					logger.info("获取TOKEN,户账号密码错误[{}]" , username);
					attributes.put("register", 1); // 1、用户名或密码错误
					j.setAttributes(attributes);
					j.setSuccess(false);

				}
				if(lhSUser.getStatus()==LstConstants.BLACK_LIST){
					attributes.put("register", 3); // 3、用户名被锁定
					j.setAttributes(attributes);
					j.setSuccess(false);
				}else{
					String roleCode="";
					if(lhSUser.getRoleCode()!=null && lhSUser.getRoleCode()!=""){
						roleCode = lhSUser.getRoleCode();	
					}else{
						roleCode="create";
					}
					attributes.put("login_code", lhSUser.getId());
					attributes.put("role_code", roleCode);
					attributes.put("status", lhSUser.getStatus());
					attributes.put("register", 2);// 2、正常用户登录
					// 生成一个token，保存用户登录状态
					String token = tokenManager.createToken2(username);
					attributes.put("token", token);
					j.setSuccess(true);
					j.setAttributes(attributes);
				}
			}else{
				attributes.put("register", 0); // 0、用户名没有注册
				logger.info("获取TOKEN,户账号密码错误[{}]" , username);
				j.setSuccess(false);
				j.setAttributes(attributes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;

	}
	
	@ApiOperation(value = "验证TOKEN")
	@RequestMapping(value = "/check",method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson check(@RequestParam String token) {
		logger.info("验证TOKEN[{}]" );
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		Claims claims = null;
		try{
			if(token!=null){
				claims = Jwts.parser().setSigningKey(JwtConstants.JWT_SECRET).parseClaimsJws(token).getBody();
				Object username = claims.getId();
				if (!oConvertUtils.isEmpty(username)) {
					TokenModel model = tokenManager.getToken(token,username.toString());
					if (tokenManager.checkToken(model)) {
						j.setSuccess(true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;

	}

	@ApiOperation(value = "销毁TOKEN")
	@RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseMessage<?> logout(@ApiParam(name = "username", value = "用户账号", required = true) @PathVariable("username") String username) {
		logger.info("deleteToken[{}]" , username);
		// 验证
		if (StringUtils.isEmpty(username)) {
			return Result.error("用户账号，不能为空!");
		}
		try {
			tokenManager.deleteToken(username);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("销毁TOKEN失败");
		}
		return Result.success();
	}

}
