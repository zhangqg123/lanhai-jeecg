package org.jeecgframework.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.web.activiti.entity.Audit;
import org.jeecgframework.web.activiti.entity.Leave;
import org.jeecgframework.web.activiti.service.AuditServiceI;
import org.jeecgframework.web.activiti.service.LeaveServiceI;
import org.jeecgframework.web.activiti.util.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jeecg.zwzx.entity.WorkApplyEntity;
import com.jeecg.zwzx.utils.PasswordUtil;


/**
 * @Description: 
 * @author liujinghua
 */
@Controller
@RequestMapping("/api/zwzx/applyAudit")
public class ApplyAuditController extends BaseController{
	
	@Autowired
	private LeaveServiceI leaveService;
	
	@Autowired
	private AuditServiceI auditService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
    protected TaskService taskService;

	private static final Logger logger = Logger.getLogger(ApplyAuditController.class);
	
	/**
     * 请假流程启动
     * @param deploymentId 流程部署ID
     */
	@RequestMapping(value = "/auditStart")
	@ResponseBody
	public AjaxJson auditStart(WorkApplyEntity workApply, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String userId = request.getHeader("login-code");
		String applyId=request.getParameter("applyId");
//		String formId=request.getParameter("formId");
//		String resCode=request.getParameter("resCode");
		workApply.setDealPersion(userId);
		workApply.setId(applyId);
//		workApply.setFormId(formId);
//		workApply.setResCode(resCode);
		//请假流程启动
		String ret=auditService.auditWorkFlowStart(workApply);
		Map<String,Object> attributes=new HashMap<String,Object>();
		attributes.put("status", 1);
		j.setAttributes(attributes);
		
		String message = "流程启动成功";
		j.setMsg(message);
		return j;
	}
	
	@RequestMapping(value = "/workList")
	public @ResponseBody String workList(@ModelAttribute WorkApplyEntity query, HttpServletRequest request, HttpServletResponse response ,
			@RequestParam(required = false, value = "pageNumber", defaultValue = "1") int pageNo,
			@RequestParam(required = false, value = "pageSize", defaultValue = "6") int pageSize) throws Exception {
		if(pageNo==0){
			pageNo=1;
		}
		AjaxJson j = new AjaxJson();
		String userId = request.getHeader("login-code");
//		String applyId=request.getParameter("applyId");
		
		String ret=auditService.workList(userId);
		return ret;
	}
	
	/**
     * 完成任务
     * @param deploymentId 流程部署ID
     */
	@RequestMapping(value = "/reApply")
	@ResponseBody
	public AjaxJson reApply(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		String taskId = request.getParameter("taskId");
		String formId = request.getParameter("formId");
		String applyId = request.getParameter("applyId");
		String status = request.getParameter("status");
		String ret=auditService.reApply(taskId,applyId,formId,status);
		j.setSuccess(false);
		if(ret=="success"){
			j.setSuccess(true);
		}
		return j;
	}
	/**
     * 完成任务
     * @param deploymentId 流程部署ID
     */
	@RequestMapping(params = "reApplyTask")
	@ResponseBody
	public AjaxJson reApplyTask(String taskId, Audit audit, Variable var, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		auditService.reApplyAudit(taskId,audit,var);
		
		//请假流程启动
		//leaveService.leaveWorkFlowStart(leave);
		
		String message = "办理成功";
		j.setMsg(message);
		return j;
	}

	
}
