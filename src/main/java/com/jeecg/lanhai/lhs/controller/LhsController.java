package com.jeecg.lanhai.lhs.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.jeecgframework.p3.core.common.utils.AjaxJson;
import org.jeecgframework.p3.core.page.SystemTools;
import org.jeecgframework.p3.core.util.plugin.ViewVelocity;
import org.jeecgframework.p3.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeecg.ask.entity.AskStatusEntity;
import com.jeecg.ask.entity.LhDsAskColumnEntity;
import com.jeecg.ask.entity.LhDsAskEntity;
import com.jeecg.ask.service.LhDsAskColumnService;
import com.jeecg.ask.service.LhDsAskService;
import com.jeecg.ask.utils.LstConstants;
import com.jeecg.lanhai.lhs.service.LhsService;

 /**
 * 描述：提问表
 * @author: www.jeecg.org
 * @since：2019年03月31日 15时06分00秒 星期日 
 * @version:1.0
 */
@Controller
@RequestMapping("/lhs/lhDsAsk")
public class LhsController extends BaseController{
  @Autowired
  private LhDsAskService lhDsAskService;
  @Autowired
  private LhDsAskColumnService lhDsAskColumnService;
	@Autowired
	private LhsService lhsService;
	private String xcxId="wx8917dfc0cdb6bf7f";

	/**
	 * 编辑
	 * @return
	 */
	@RequestMapping(params = "doAudit",method ={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public AjaxJson doAudit(@ModelAttribute LhDsAskEntity lhDsAsk){
		AjaxJson j = new AjaxJson();
		String sendType=null;
		try {
			Integer status = lhDsAsk.getAskStatus();
			if(lhDsAsk.getReply().equals("blacklist")){
				lhDsAskService.doBlack(lhDsAsk);
			}else{
				LhDsAskEntity oldlhAsk = lhDsAskService.get(lhDsAsk.getId());
				String openId = oldlhAsk.getAskOpenId();
				if(lhDsAsk.getReply().equals("pass")){
					if(status==LstConstants.CREATE_ASK){
						lhDsAsk.setAskStatus(LstConstants.AUDIT_ASK);
						sendType="提问审核通过";
					}
					if(status==LstConstants.ANSWER_ASK){
						lhDsAsk.setAskStatus(LstConstants.AUDIT_ANSWER);
					}
				}
				if(lhDsAsk.getReply().equals("deny")){
					if(status==LstConstants.CREATE_ASK){
						lhDsAsk.setAskStatus(LstConstants.ASK_DENY);
						sendType="提问审核未通过";
					}
					if(status==LstConstants.ANSWER_ASK){
						lhDsAsk.setAskStatus(LstConstants.ANSWER_DENY);
					}
				}
				
				lhDsAskService.update(lhDsAsk);
				
				j.setMsg("编辑成功");
				lhsService.sendWeChat(openId,xcxId,sendType);
			}
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("编辑失败");
		    e.printStackTrace();
		}
		return j;
	}

}

