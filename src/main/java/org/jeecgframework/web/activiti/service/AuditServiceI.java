package org.jeecgframework.web.activiti.service;

import java.util.Map;

import org.jeecgframework.web.activiti.entity.Audit;
import org.jeecgframework.web.activiti.entity.Leave;
import org.jeecgframework.web.activiti.util.Variable;

import com.jeecg.zwzx.entity.WorkApplyEntity;

public interface AuditServiceI {
	public String auditWorkFlowStart(WorkApplyEntity entity);
	public Audit getAudit(Long id);
	public void reApplyAudit(String taskId,Audit audit,Variable var);
	public String workList(String userId);
	public String reApply(String taskId, String applyId, String formId, String status);
	public void completeTask(String taskId, String businessKey,
			String deptLeaderPass, String reply);
	public WorkApplyEntity getWorkApplyByProcess(String processInstanceId);
}
