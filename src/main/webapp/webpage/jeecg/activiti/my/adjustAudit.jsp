<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>补充申请材料</title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" dialog="true" layout="table" action="auditController.do?reApplyTask">
	<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td align="right" width="15%" nowrap><label class="Validform_label"> 调整申请原因: </label></td>
			<td class="value" width="85%">
				<input id="id" name="id" type="hidden" value="${audit.id}">			
				<input id="reason" class="inputxt" name="reason" value="${audit.reason}" datatype="s2-100" />
				<span class="Validform_checktip">范围在2~10位字符</span>
			</td>
		</tr>
		<tr>
			<td align="right" width="15%" nowrap><label class="Validform_label"> 重新申请: </label></td>
			<td class="value" width="85%">
				<input id="taskId" name="taskId" type="hidden" value="${taskId}">			
				<input id="keys" name="keys" type="hidden" value="reApply"  />	
				<input id="values" name="values" class="inputxt"  value="true" datatype="s2-20">
				<input id="types" name="types" type="hidden" value="B">
				<span class="Validform_checktip">范围在2~10位字符</span>
			</td>
		</tr>
	</table>
</t:formvalid>
</body>