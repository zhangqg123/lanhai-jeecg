<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="runningList" checkbox="false" pagination="true" fitColumns="false" title="流程列表" actionUrl="activitiController.do?runningProcessDataGrid" idField="id" fit="true" queryMode="group" queryBuilder="true">
    <t:dgCol title="编号"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="流程定义"  field="processDefinitionId" queryMode="group"  width="120"></t:dgCol>
    <t:dgCol title="流程实例"  field="processInstanceId" queryMode="group"  width="120"></t:dgCol>
    <t:dgCol title="activityId"  field="activityId" queryMode="group"  width="120"></t:dgCol>
	<t:dgCol title="操作" field="opt" width="200"></t:dgCol>
    <t:dgFunOpt funname="viewHistory(processInstanceId)" title="历史" urlclass="ace_button"  urlfont="fa-database"></t:dgFunOpt>
  </t:datagrid>
  </div>
</div>

<script type="text/javascript">
		//查看流程历史
		function viewHistory(processInstanceId){
			var url = "";
			var title = "流程历史";
			url = "activitiController.do?viewProcessInstanceHistory&processInstanceId="+processInstanceId+"&isIframe"
			addOneTab(title, url);
		}
</script>