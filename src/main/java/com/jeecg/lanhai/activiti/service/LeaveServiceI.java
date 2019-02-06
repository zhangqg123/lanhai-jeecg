package com.jeecg.lanhai.activiti.service;

import com.jeecg.lanhai.activiti.entity.Leave;
import com.jeecg.lanhai.activiti.util.Variable;


public interface LeaveServiceI {
	public void leaveWorkFlowStart(Leave entity);
	public Leave getLeave(Long id);
	public void reApplyLeave(String taskId,Leave leave,Variable var);
}
