package org.jeecgframework.web.activiti.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.activiti.dao.LeaveDao;
import org.jeecgframework.web.activiti.entity.Leave;
import org.jeecgframework.web.activiti.service.LeaveServiceI;
import org.jeecgframework.web.activiti.util.Variable;
import org.jeecgframework.web.black.service.TsBlackListServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("leaveService")
@Transactional
public class LeaveServiceImpl extends CommonServiceImpl implements LeaveServiceI {
	
	@Autowired
	private LeaveDao leaveDao;
	
	@Autowired
    private IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
    protected TaskService taskService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 启动请假流程
	 * @param leave
	 */
	public void leaveWorkFlowStart(Leave entity){
        leaveDao.save(entity);
        logger.debug("save entity: {}", entity);
        
        String businessKey = entity.getId().toString();
        ProcessInstance processInstance = null;
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(entity.getUserId());

            Map<String, Object> variables = new HashMap<String, Object>();
            processInstance = runtimeService.startProcessInstanceByKey("leave", businessKey, variables);
            String processInstanceId = processInstance.getId();
            entity.setProcessInstanceId(processInstanceId);
            logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[]{"leave", businessKey, processInstanceId, variables});
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
		
	}
	
	@Transactional(readOnly = true)
	public Leave getLeave(Long id){
		return leaveDao.getLeave(id);
	}
	
	public void reApplyLeave(String taskId,Leave leave,Variable var){

		String reason = leave.getReason();
//		Leave tmpLeave = leaveDao.getLeave(new Long(leave.getId()));
		Leave tmpLeave=super.getEntity(Leave.class, leave.getId());
		tmpLeave.setReason(reason);
//		leaveDao.save(tmpLeave);
		Serializable t = super.save(tmpLeave);
		Map<String, Object> variables = var.getVariableMap();
        taskService.complete(taskId, variables);
	}
	
}
