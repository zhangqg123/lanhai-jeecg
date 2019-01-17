package org.jeecgframework.web.activiti.service.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.HttpClientUtil;
import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.jeecgframework.web.activiti.dao.LeaveDao;
import org.jeecgframework.web.activiti.entity.Audit;
import org.jeecgframework.web.activiti.entity.Leave;
import org.jeecgframework.web.activiti.service.AuditServiceI;
import org.jeecgframework.web.activiti.service.LeaveServiceI;
import org.jeecgframework.web.activiti.util.Variable;
import org.jeecgframework.web.activiti.util.WXTemplate;
import org.jeecgframework.web.activiti.util.WXTemplateData;
import org.jeecgframework.web.black.service.TsBlackListServiceI;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.json.JSONException;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.jeecg.zwzx.entity.WorkApplyEntity;
import com.jeecg.zwzx.entity.WorkGuideEntity;
import com.jeecg.zwzx.entity.WorkMenuEntity;
import com.jeecg.zwzx.entity.WorkUserEntity;
import com.jeecg.zwzx.service.WorkApplyService;
import com.jeecg.zwzx.service.WorkGuideService;
import com.jeecg.zwzx.service.WorkMenuService;
import com.jeecg.zwzx.service.WorkUserService;

@Service("auditService")
@Transactional
public class AuditServiceImpl extends CommonServiceImpl implements AuditServiceI {
	
	@Autowired
	private WorkApplyService workApplyService;
	
	@Autowired
	private WorkGuideService workGuideService;
	@Autowired
	private WorkMenuService workMenuService;
	@Autowired
	private WorkUserService workUserService;
	
	@Autowired
    private IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
    protected TaskService taskService;
	
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String jscode2sessionUrl="https://api.weixin.qq.com/sns/jscode2session";
	private static final String SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send";
    private final String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private String appId="wx1dd61973b1e47f1b";
	private String secret="a6dc6459e8aa7c10a4c7415d5b7890d5";

	private String templateId="OZy8F0P10K3RAsgYEy5a_LebF9WjMeLDYF5hbq70YK0";
	
	/**
	 * 启动请假流程
	 * @param leave
	 */
	public String auditWorkFlowStart(WorkApplyEntity entity){
		String formId=entity.getFormId();
		String resCode=entity.getResCode();
		Map<String,String>params=new HashMap<String, String>();
		params.put("js_code",entity.getResCode());
		params.put("grant_type","authorization_code");
		params.put("appid", appId);
		params.put("secret", secret);					
		String result=HttpClientUtil.postParams(jscode2sessionUrl,params);
		JSONObject json = null;
		String openId = null;
		try {
			json = JSONObject.fromObject(result);
			openId=(String) json.get("openid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		WorkApplyEntity workApply = workApplyService.get(entity.getId());
		if(workApply.getApplyStatus()!=null&&workApply.getApplyStatus()>0){
			return "already";
		}else{
//			Serializable t = super.save(entity);
			workApply.setApplyStatus(1);
	        String management = workApply.getManagement();
	        String username = workApplyService.getUserName(management);
	        String businessKey = workApply.getId().toString();
	        ProcessInstance processInstance = null;
	        try {
	            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
	            identityService.setAuthenticatedUserId(workApply.getDealPersion());
	
	            Map<String, Object> variables = new HashMap<String, Object>();
	    		variables.put("username", username);
	            processInstance = runtimeService.startProcessInstanceByKey("zwzx", businessKey, variables);
	            String processInstanceId = processInstance.getId();
	            workApply.setProcessInstanceId(processInstanceId);
	            workApply.setFormId(formId);
	            workApply.setResCode(resCode);
	            workApply.setOpenId(openId);
	            workApplyService.update(workApply);
	            logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[]{"leave", businessKey, processInstanceId, variables});
	        } finally {
	            identityService.setAuthenticatedUserId(null);
	        }
			return "success";
		}
		
	}
	
	@Transactional(readOnly = true)
	public Audit getAudit(Long id){
		return super.getEntity(Audit.class, id);
	}
	
	public void reApplyAudit(String taskId,Audit audit,Variable var){

		String reason = audit.getReason();
//		Leave tmpLeave = leaveDao.getLeave(new Long(leave.getId()));
		Audit tmpAudit=super.getEntity(Audit.class, audit.getId());
		tmpAudit.setReason(reason);
//		leaveDao.save(tmpLeave);
		Serializable t = super.save(tmpAudit);
		Map<String, Object> variables = var.getVariableMap();
        taskService.complete(taskId, variables);
	}

	@Override
	public String workList(String userId) {
//		TaskService taskService = processEngine.getTaskService();
		TaskQuery query = taskService.createTaskQuery();
        List<Task> tasks = query.taskAssignee(userId).list();
		
		StringBuffer rows = new StringBuffer();
		List tempList=new ArrayList();
		
		for(Task t : tasks){
			WorkApplyEntity tmpWorkApply = new WorkApplyEntity();
			tmpWorkApply.setProcessInstanceId(t.getProcessInstanceId());
			MiniDaoPage<WorkApplyEntity> workApplyList = workApplyService.getAll(tmpWorkApply, 1, 10);
			WorkApplyEntity workApply = workApplyList.getResults().get(0);
			Map tempMap=new HashMap();
			tempMap.put("name", t.getName());
			tempMap.put("taskId", t.getId());
			tempMap.put("applyId", workApply.getId());
			tempMap.put("guideId", workApply.getGuideId());
			tempMap.put("management", workApply.getManagement());
			tempMap.put("applyStatus", workApply.getApplyStatus());			
			tempMap.put("processInstanceId", t.getProcessInstanceId());
			tempList.add(tempMap);
//			rows.append("{'name':'"+t.getName() +"','description':'"+t.getDescription()+"','id':'"+t.getId()+"','processDefinitionId':'"+t.getProcessDefinitionId()+"','processInstanceId':'"+t.getProcessInstanceId()+"'},");
		}
//		String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
		return JSONArray.toJSONString(tempList);
	}

	@Override
	public String reApply(String taskId, String applyId, String formId, String status) {
		WorkApplyEntity workApply = workApplyService.get(applyId);
		workApply.setApplyStatus(7);
		boolean reApply=false;
		if(status.equals("reApply")){
			reApply=true;
			workApply.setApplyStatus(1);
		}
		workApply.setFormId(formId);
		workApplyService.update(workApply);
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("reApply", reApply);
		taskService.complete(taskId, variables);
		return "success";
	}

	@Override
	public void completeTask(String taskId, String businessKey,
			String deptLeaderPass,String reply) {
		boolean auditPass=true;
		if(deptLeaderPass.equals("true")||deptLeaderPass.equals("false")){
			auditPass=Boolean.parseBoolean(deptLeaderPass);
		}
		Map<String, Object> variables = new HashMap<String, Object>();		
		variables.put("deptLeaderPass", auditPass);
		taskService.complete(taskId, variables);
		WorkApplyEntity workApply = workApplyService.get(businessKey);
		
		if(auditPass){
			workApply.setApplyStatus(2);
			if(deptLeaderPass.equals("abort")){
				workApply.setApplyStatus(8); //8, 废弃
			}
		}else{
			workApply.setApplyStatus(6);
		}
		workApply.setReply(reply);
		sendTemplateMessage(workApply);
		workApplyService.update(workApply);
	}

	private void sendTemplateMessage(WorkApplyEntity workApply) {
		String openId=workApply.getOpenId();
		if(openId!=null){
	    	Map<String, WXTemplateData> map = new HashMap<String, WXTemplateData>();
	    	Integer applyStatus = workApply.getApplyStatus();
	    	String displayStatus = null;
	    	if(applyStatus==2){
	    		displayStatus="预审通过";
	    	}
	    	if(applyStatus==6){
	    		displayStatus="预审未通过";
	    	}
	    	if(applyStatus==8){
	    		displayStatus="申报废弃";
	    	}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	        String auditDate = sdf.format(new Date());  
	        map.put("keyword1", new WXTemplateData(displayStatus,"#173177"));
	        map.put("keyword2", new WXTemplateData(workApply.getApplySubject(),"#173177"));
	        map.put("keyword3", new WXTemplateData(auditDate,"#173177"));
	        map.put("keyword4", new WXTemplateData(workApply.getReply(),"#173177"));
			String accessToken=getAccessToken(appId,secret);
			String requestUrl = SEND_URL + "?access_token="+accessToken;
			
			WXTemplate template = new WXTemplate();
			template.setTouser(openId);
			template.setTemplate_id(templateId);
			template.setPage("pages/home/myproject");
			template.setForm_id(workApply.getFormId());
			template.setData(map);
			template.setEmphasis_keyword("keyword1.DATA");
	
			String jsonMsg = JSONObject.fromObject(template).toString();
//			System.out.println("发送模板消息：" + jsonMsg);
			// 发送模板消息
			String resp = HttpClientUtil.post(requestUrl, jsonMsg, null);
//			System.out.println("模板消息返回：" + resp);
		}
	}
	
    public  String getAccessToken(String appId ,String appSecret){
        
        String url = accessTokenUrl.replace("APPID", appId);
        url = url.replace("APPSECRET", appSecret);
        JSONObject resultJson =null;
        String result = HttpClientUtil.post(url, "", null);
         try {
             resultJson = JSONObject.fromObject(result);
//             System.out.println("access token 返回值：" + resultJson);
         } catch (JSONException e) {
             e.printStackTrace();
         }
         
         return (String) resultJson.get("access_token");

    }

	@Override
	public WorkApplyEntity getWorkApplyByProcess(String processInstanceId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
		
		String businessKey = processInstance.getBusinessKey();

		WorkApplyEntity workApply = workApplyService.get(businessKey);
		WorkGuideEntity workGuide=workGuideService.get(workApply.getGuideId());
		WorkMenuEntity workMenu=workMenuService.get(workApply.getManagement());
		WorkUserEntity workUser = workUserService.get(workApply.getDealPersion());
		workApply.setGuideName(workGuide.getGuideName());
		workApply.setManagementName(workMenu.getName());
		workApply.setPersonName(workUser.getRealname());
		return workApply;
	}
	
}
