package org.jeecgframework.web.activiti.service;

import org.jeecgframework.web.activiti.entity.Leave;
import org.jeecgframework.web.activiti.util.Variable;

public interface LeaveServiceI {
	public void leaveWorkFlowStart(Leave entity);
	public Leave getLeave(Long id);
	public void reApplyLeave(String taskId,Leave leave,Variable var);
}
