<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="processList" checkbox="false" pagination="true" fitColumns="false" title="流程列表" actionUrl="activitiController.do?datagrid" idField="id" fit="true" queryMode="group" queryBuilder="true">
    <t:dgCol title="编号"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="jsp"  field="startPage"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="流程名称"  field="name" url="activitiController.do?resourceRead&processDefinitionId={processDefinitionId}&resourceType=image&isIframe" query="false" queryMode="group" width="120"></t:dgCol>
    <t:dgCol title="流程编码"  field="key" query="false" queryMode="group" width="120"></t:dgCol>
    <t:dgCol title="版本"  field="version" queryMode="group"  width="120"></t:dgCol>
    <t:dgCol title="是否挂起"  field="isSuspended" query="false" queryMode="group" width="120"></t:dgCol>
	<t:dgCol title="操作" field="opt" width="200"></t:dgCol>
<%--     <t:dgFunOpt funname="readProcessResouce(processDefinitionId)" title="流程图片" urlclass="ace_button"  urlfont="fa fa-book"></t:dgFunOpt> --%>
    <t:dgFunOpt funname="startProcess(startPage)" title="启动新流程" urlclass="ace_button"  urlfont="fa-database"></t:dgFunOpt>
  </t:datagrid>
  </div>
</div>

<script type="text/javascript">
		//查看流程xml或流程图片
		function readProcessResouce(processDefinitionId) {
			var url = "";
			var title = "";
			var resourceType = "image";
			if(resourceType == "image"){
				title = "查看流程图片";
				url = "activitiController.do?resourceRead&processDefinitionId="+processDefinitionId+"&resourceType=image&isIframe";
			}
			addOneTab(title, url);
		}
		function startProcess(startPage) {
			var	url = "activitiController.do?startPageSelect&startPage="+startPage;
			add("发起新流程", url, 'myprocessList');
		}

</script>