<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="waitingClaimTask" checkbox="false" pagination="true" fitColumns="false" title="任务列表" actionUrl="activitiController.do?waitingClaimTaskDataGrid" idField="id" fit="true" queryMode="group" queryBuilder="true">
    <t:dgCol title="任务编号"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="任务名称"  field="name" queryMode="group"  width="120"></t:dgCol>
    <t:dgCol title="流程定义"  field="processDefinitionId" queryMode="group"  width="120"></t:dgCol>
	<t:dgCol title="操作" field="opt" width="200"></t:dgCol>
    <t:dgFunOpt funname="claimTask(id)" title="领取任务" urlclass="ace_button"  urlfont="fa-database"></t:dgFunOpt>
  </t:datagrid>
  </div>
</div>

<script type="text/javascript">
		//查看流程历史
		function claimTask(taskId){
			confirm('activitiController.do?claimTask&taskId='+taskId,'确定签收吗？','waitingClaimTask');
		}
</script>