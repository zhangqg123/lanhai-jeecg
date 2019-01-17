package org.jeecgframework.web.activiti.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.web.activiti.entity.Audit;
import org.jeecgframework.web.activiti.entity.Leave;
import org.jeecgframework.web.activiti.service.AuditServiceI;
import org.jeecgframework.web.activiti.service.LeaveServiceI;
import org.jeecgframework.web.activiti.util.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jeecg.zwzx.entity.Material;
import com.jeecg.zwzx.entity.WorkApplyEntity;
import com.jeecg.zwzx.entity.WorkGuideEntity;
import com.jeecg.zwzx.entity.WorkMenuEntity;
import com.jeecg.zwzx.service.WorkApplyService;
import com.jeecg.zwzx.service.WorkGuideService;
import com.jeecg.zwzx.service.WorkMenuService;


@Controller
@RequestMapping("/auditController")
public class AuditController extends BaseController{
	
	@Autowired
	private WorkApplyService workApplyService;

	@Autowired
	private LeaveServiceI leaveService;
	
	@Autowired
	private AuditServiceI auditService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
    protected TaskService taskService;

	private static final Logger logger = Logger.getLogger(AuditController.class);
	
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
		workApply.setDealPersion(userId);
		workApply.setId(applyId);
		
		//请假流程启动
		String ret=auditService.auditWorkFlowStart(workApply);
		logger.info("audit flow start.."+ret);

		Map<String,Object> attributes=new HashMap<String,Object>();
		attributes.put("status", 1);
		j.setAttributes(attributes);
		
		String message = "流程启动成功";
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 完成任务表单选择
	 */
	@RequestMapping(params = "taskCompletePageSelect")
	public ModelAndView taskCompletePageSelect(@RequestParam("jspPage") String jspPage,
			@RequestParam("processInstanceId") String processInstanceId,
			@RequestParam("taskId") String taskId,HttpServletRequest request,Model model) {
			
			WorkApplyEntity workApply=auditService.getWorkApplyByProcess(processInstanceId);
			model.addAttribute("processInstanceId", processInstanceId);
			model.addAttribute("taskId", taskId);
			model.addAttribute("workApply",workApply);
			if(workApply.getMaterial()!=null&&workApply.getMaterial()!=""){
				JSONArray materialArray = JSONArray.fromObject(workApply.getMaterial());
				List<?> materialList = JSONArray.toList(materialArray, new Material(), new JsonConfig());
				model.addAttribute("materialList",materialList);
			}
			
			return new ModelAndView("jeecg/activiti/my/"+jspPage.substring(0, jspPage.lastIndexOf(".")));
	}
	
	/**
     * 完成任务
     * @param deploymentId 流程部署ID
     */
	@RequestMapping(params = "completeTask")
	@ResponseBody
	public AjaxJson completeTask(WorkApplyEntity workApply,String taskId,String businessKey,String deptLeaderPass,HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String reply=workApply.getReply();
		auditService.completeTask(taskId,businessKey,deptLeaderPass,reply);

		String message = "办理成功";
		j.setMsg(message);
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
