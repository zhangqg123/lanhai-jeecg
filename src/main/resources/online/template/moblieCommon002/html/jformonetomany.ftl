<div id="${sub}_area">
	<#if data['${sub}']?exists&&(data['${sub}']?size>0) >
	  <#--//update-begin--Author:Yandong  Date:20171227 for：TASK #2375 【online模板】通用移动模板002，有很多问题-->
	  <#list data['${sub}']  as subTableData>
	   <div name="items">
	     	<li>
				<div class="alert alert-info" role="alert">
					 ${field['${sub}'].head.content?if_exists?html}(<span name="index">${subTableData_index+1}</span>)
					  <button type="button" class="btn btn-default btn-sm active jeecgDetail" name="${sub}_delBtn" style="float:right;margin-top:-5px;">删除</button>
				</div>
			</li>
			<#--//update-end--Author:Yandong  Date:20171227 for：TASK #2375 【online模板】通用移动模板002，有很多问题-->
			<input type="hidden" name="${sub}[${subTableData_index}].id" id="${sub}[${subTableData_index}].id" value="${subTableData['id']?if_exists?html}"/>
			<#list field['${sub}'].hiddenFieldList as subTableField >
				<input type="hidden" name="${sub}[${subTableData_index}].${subTableField.field_name}" id="${sub}[${subTableData_index}].${subTableField.field_name}" value="${subTableData['${subTableField.field_name}']?if_exists?html}"/>
			</#list> 
			<#list field['${sub}'].fieldList as subTableField >
				<#if subTableField.show_type=='text'>
						<li id="${sub}[${subTableData_index}].${subTableField.field_name}" class="clearfix " typ="name" reqd="1">
							<label class="desc">${subTableField.content}:<#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
							<div class="content">
								<input 
									type="text" 
									maxlength="256" 
									class="ui-input-text xl input fld" 
									name="${sub}[${subTableData_index}].${subTableField.field_name}" 
									id="${sub}[${subTableData_index}].${subTableField.field_name}" 
									${subTableField.extend_json?if_exists}
									value="${subTableData['${subTableField.field_name}']?if_exists?html}" 
									<#if subTableField.operationCodesReadOnly?exists> readonly = "readonly"</#if>
									 <#-- update--begin--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
												<#if subTableField.field_must_input?if_exists?html != ''><#if subTableField.field_must_input == 'Y' || subTableField.is_null != 'Y'>ignore="checked"<#else>ignore="ignore"</#if><#elseif subTableField.is_null != "Y">ignore="checked"<#else>ignore="ignore"</#if>
												<#-- update--end--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
									<#if subTableField.field_valid_type?if_exists?html != ''>
										datatype="${subTableField.field_valid_type?if_exists?html}" 
										<#else>
											<#if subTableField.type == 'int'>
												datatype="n"  <#if subTableField.is_null == 'Y'>ignore="ignore" </#if>
												<#elseif subTableField.type=='double'>
												datatype="/^(-?\d+)(\.\d+)?$/" <#if subTableField.is_null == 'Y'>ignore="ignore" </#if>
												<#else>
												<#if subTableField.is_null != 'Y'>datatype="*"</#if>
											</#if>
									</#if>
								/>
							</div>
						</li>
				<#elseif subTableField.show_type=='password'>
							<li id="${sub}[${subTableData_index}].${subTableField.field_name}" class="clearfix " typ="password" reqd="1">
								<label class="desc">${subTableField.content}: <#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
								<div class="content">
									<input 
										type="text" 
										maxlength="256" 
										class="ui-input-text xl input fld" 
										name="${sub}[${subTableData_index}].${subTableField.field_name}" 
										id="${sub}[${subTableData_index}].${subTableField.field_name}" 
										${subTableField.extend_json?if_exists}
										value="${subTableData['${subTableField.field_name}']?if_exists?html}" 
										<#if subTableField.operationCodesReadOnly?exists> readonly = "readonly"</#if>
										 <#-- update--begin--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
												<#if subTableField.field_must_input?if_exists?html != ''><#if subTableField.field_must_input == 'Y' || subTableField.is_null != 'Y'>ignore="checked"<#else>ignore="ignore"</#if><#elseif subTableField.is_null != "Y">ignore="checked"<#else>ignore="ignore"</#if>
												<#-- update--end--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
										<#if subTableField.field_valid_type?if_exists?html != ''>
											datatype="${subTableField.field_valid_type?if_exists?html}" 
											<#else>
												<#if subTableField.is_null != 'Y'>datatype="*"</#if>
										</#if>
									/>
								</div>
							</li>
				<#elseif subTableField.show_type=='radio'>
							<li id="${sub}[${subTableData_index}].${subTableField.field_name}" class="clearfix " typ="radio" reqd="1">
								<label class="desc">${subTableField.content}:<#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
								<div class="content">
									<fieldset class="controlgroup">
										<@DictData name="${subTableField.dict_field?if_exists?html}" text="${subTableField.dict_text?if_exists?html}" tablename="${subTableField.dict_table?if_exists?html}" var="dataList">
											<#list dataList as dictdata> 
												<label <#if dictdata_index==0>class="first"</#if>>
													<input 
														value="${dictdata.typecode?if_exists?html}" 
														${subTableField.extend_json?if_exists} 
														name="${sub}[${subTableData_index}].${subTableField.field_name}" 
														type="radio" 
														<#if dictdata_index==0&&subTableField.is_null != 'Y'>datatype="*"</#if> 
														<#if subTableField.operationCodesReadOnly?if_exists>onclick="return false;"</#if>
														<#if dictdata.typecode?if_exists?html=="${subTableData['${subTableField.field_name}']?if_exists?html}"> checked="true"</#if> 
													/><label></label>${dictdata.typename?if_exists?html}
												</label>
											</#list> 
										</@DictData>
									</fieldset>
								</div>
							</li>
					<#elseif subTableField.show_type=='checkbox'>
							<li id="${sub}[${subTableData_index}].${subTableField.field_name}" class="clearfix " typ="radio" reqd="1">
								<label class="desc">${subTableField.content}:<#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
								<div class="content">
									<fieldset class="controlgroup">
										<#assign checkboxstr>${subTableData['${subTableField.field_name}']?if_exists?html}</#assign>
										<#assign checkboxlist=checkboxstr?split(",")>
										<@DictData name="${subTableField.dict_field?if_exists?html}" text="${subTableField.dict_text?if_exists?html}" tablename="${subTableField.dict_table?if_exists?html}" var="dataList">
											<#list dataList as dictdata> 
												<label <#if dictdata_index==0>class="first"</#if>>
													<input 
														value="${dictdata.typecode?if_exists?html}" 
														${subTableField.extend_json?if_exists} 
														name="${sub}[${subTableData_index}].${subTableField.field_name}" 
														type="checkbox" 
														<#if subTableField.operationCodesReadOnly?if_exists>onclick="return false;"</#if>
														<#if dictdata_index==0&&subTableField.is_null != 'Y'>datatype="*"</#if> 
														<#list checkboxlist as x >
															<#if dictdata.typecode?if_exists?html=="${x?if_exists?html}"> checked="true" </#if>
														</#list>
													/><label></label>${dictdata.typename?if_exists?html}
												</label>
											</#list> 
										</@DictData>
									</fieldset>
								</div>
							</li>
					<#elseif subTableField.show_type=='list'>
						<li id="${sub}[${subTableData_index}].${subTableField.field_name}" class="clearfix " typ="list">
							<label class="desc">${subTableField.content}:<#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
							<div class="content">
									<@DictData name="${subTableField.dict_field?if_exists?html}" text="${subTableField.dict_text?if_exists?html}" tablename="${subTableField.dict_table?if_exists?html}" var="dataList">
										<select 
											id="${sub}[${subTableData_index}].${subTableField.field_name}" 
											${subTableField.extend_json?if_exists} 
											name="${sub}[${subTableData_index}].${subTableField.field_name}" 
											<#if subTableField.operationCodesReadOnly?if_exists>
												onfocus="this.defOpt=this.selectedIndex" onchange="this.selectedIndex=this.defOpt;"</#if><#if subTableField.is_null != 'Y'>datatype="*"
											</#if> 
											 <#-- update--begin--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
												<#if subTableField.field_must_input?if_exists?html != ''><#if subTableField.field_must_input == 'Y' || subTableField.is_null != 'Y'>ignore="checked"<#else>ignore="ignore"</#if><#elseif subTableField.is_null != "Y">ignore="checked"<#else>ignore="ignore"</#if>
												<#-- update--end--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
											class="ui-input-select province fld"
											>
											<#list dataList as dictdata> 
												<option 
													value="${dictdata.typecode?if_exists?html}" 
													<#if dictdata.typecode?if_exists?html=="${subTableData['${subTableField.field_name}']?if_exists?html}">
														selected="selected"
													</#if>>${dictdata.typename?if_exists?html}
												</option> 
											</#list> 
										</select>
									</@DictData>
							</div>
						</li>
				<#elseif subTableField.show_type=='date'>
					<li id="${sub}[${subTableData_index}].${subTableField.field_name}" class="clearfix " typ="date" reqd="1">
							<label class="desc">${subTableField.content}: <#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
							<div class="content">
								<input 
									type="text" 
									maxlength="256" 
									class="ui-input-text xl input fld" 
									name="${sub}[${subTableData_index}].${subTableField.field_name}" 
									id="${sub}[${subTableData_index}].${subTableField.field_name}" 
									${subTableField.extend_json?if_exists}
									value="${subTableData['${subTableField.field_name}']?if_exists?html}" 
									onClick="WdatePicker({<#if subTableField.operationCodesReadOnly?if_exists> readonly = true</#if>})" 
									<#if subTableField.operationCodesReadOnly?exists> readonly = "readonly"</#if>
									 <#-- update--begin--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
									<#if subTableField.field_must_input?if_exists?html != ''><#if subTableField.field_must_input == 'Y' || subTableField.is_null != 'Y'>ignore="checked"<#else>ignore="ignore"</#if><#elseif subTableField.is_null != "Y">ignore="checked"<#else>ignore="ignore"</#if>
									<#-- update--end--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
									<#if subTableField.field_valid_type?if_exists?html != ''>
										datatype="${subTableField.field_valid_type?if_exists?html}" 
										<#else>
											<#if subTableField.is_null != 'Y'>datatype="*"</#if>
									</#if>
								/>
							</div>
						</li>
					<#elseif subTableField.show_type=='datetime'>
						<li id="${sub}[${subTableData_index}].${subTableField.field_name}" class="clearfix " typ="date" reqd="1">
							<label class="desc">${subTableField.content}: <#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
							<div class="content">
								<input 
									type="text" 
									maxlength="256" 
									class="ui-input-text xl input fld" 
									name="${sub}[${subTableData_index}].${subTableField.field_name}" 
									id="${sub}[${subTableData_index}].${subTableField.field_name}" 
									${subTableField.extend_json?if_exists}
									value="${subTableData['${subTableField.field_name}']?if_exists?html}" 
									onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'<#if subTableField.operationCodesReadOnly?if_exists> readonly = true</#if>})" 
									<#if subTableField.operationCodesReadOnly?exists> readonly = "readonly"</#if>
									 <#-- update--begin--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
									<#if subTableField.field_must_input?if_exists?html != ''><#if subTableField.field_must_input == 'Y' || subTableField.is_null != 'Y'>ignore="checked"<#else>ignore="ignore"</#if><#elseif subTableField.is_null != "Y">ignore="checked"<#else>ignore="ignore"</#if>
									<#-- update--end--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
									<#if subTableField.field_valid_type?if_exists?html != ''>
										datatype="${subTableField.field_valid_type?if_exists?html}" 
										<#else>
											<#if subTableField.is_null != 'Y'>datatype="*"</#if>
									</#if>
								/>
							</div>
						</li>
						<#-- update--begin--author:taoyan Date:20170707 for:TASK #2918 【bug】online样式，通用移动模板2一对多 -->
						<#else>
						<li id="${sub}[${subTableData_index}].${subTableField.field_name}" class="clearfix " typ="name" reqd="1">
							<label class="desc">${subTableField.content}:<#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
							<div class="content">
								<input 
									type="text" 
									maxlength="256" 
									class="ui-input-text xl input fld" 
									name="${sub}[${subTableData_index}].${subTableField.field_name}" 
									id="${sub}[${subTableData_index}].${subTableField.field_name}" 
									${subTableField.extend_json?if_exists}
									value="${subTableData['${subTableField.field_name}']?if_exists?html}" 
									<#if subTableField.operationCodesReadOnly?exists> readonly = "readonly"</#if>
									 <#-- update--begin--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
									<#if subTableField.field_must_input?if_exists?html != ''><#if subTableField.field_must_input == 'Y' || subTableField.is_null != 'Y'>ignore="checked"<#else>ignore="ignore"</#if><#elseif subTableField.is_null != "Y">ignore="checked"<#else>ignore="ignore"</#if>
									<#-- update--end--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
									<#if subTableField.field_valid_type?if_exists?html != ''>
										datatype="${subTableField.field_valid_type?if_exists?html}" 
										<#else>
											<#if subTableField.type == 'int'>
												datatype="n"  <#if subTableField.is_null == 'Y'>ignore="ignore" </#if>
												<#elseif subTableField.type=='double'>
												datatype="/^(-?\d+)(\.\d+)?$/" <#if subTableField.is_null == 'Y'>ignore="ignore" </#if>
												<#else>
												<#if subTableField.is_null != 'Y'>datatype="*"</#if>
											</#if>
									</#if>
								/>
							</div>
						</li>
					<#-- update--end--author:taoyan Date:20170707 for:TASK #2918 【bug】online样式，通用移动模板2一对多 -->
					</#if>
			 </#list>	
		<#--//update-begin--Author:Yandong  Date:20171227 for：TASK #2375 【online模板】通用移动模板002，有很多问题-->
			</div>
		</#list>
		<#--//update-end--Author:Yandong  Date:20171227 for：TASK #2375 【online模板】通用移动模板002，有很多问题-->
	 <#else>
		  <div name="items">
		  	<li>
				<div class="alert alert-info" role="alert">
					  ${field['${sub}'].head.content?if_exists?html}(<span name="index">1</span>)
					  <button type="button" class="btn btn-default btn-sm active" name="${sub}_delBtn" style="float:right;margin-top:-5px;">删除</button>
				</div>
		     </li>
			<input type="hidden" name="${sub}[0].id" id="${sub}[0].id" />
			<#list field['${sub}'].hiddenFieldList as subTableField >
				<input type="hidden" name="${sub}[0].${subTableField.field_name}" id="${sub}[0].${subTableField.field_name}"/>
			</#list> 
			<#list field['${sub}'].fieldList as subTableField >
				<#if subTableField.show_type=='text'>
						<li id="${sub}[0].${subTableField.field_name}" class="clearfix " typ="name" reqd="1">
							<label class="desc">${subTableField.content}:<#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
							<div class="content">
								<input 
									type="text" 
									maxlength="256" 
									class="ui-input-text xl input fld" 
									name="${sub}[0].${subTableField.field_name}" 
									id="${sub}[0].${subTableField.field_name}" 
									 <#-- update--begin--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
									<#if subTableField.field_must_input?if_exists?html != ''><#if subTableField.field_must_input == 'Y' || subTableField.is_null != 'Y'>ignore="checked"<#else>ignore="ignore"</#if><#elseif subTableField.is_null != "Y">ignore="checked"<#else>ignore="ignore"</#if>
									<#-- update--end--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
									${subTableField.extend_json?if_exists}
									<#if subTableField.field_valid_type?if_exists?html != ''>
										datatype="${subTableField.field_valid_type?if_exists?html}" 
										<#else>
											<#if subTableField.type == 'int'>
												datatype="n"  <#if subTableField.is_null == 'Y'>ignore="ignore" </#if>
												<#elseif subTableField.type=='double'>
												datatype="/^(-?\d+)(\.\d+)?$/" <#if subTableField.is_null == 'Y'>ignore="ignore" </#if>
												<#else>
												<#if subTableField.is_null != 'Y'>datatype="*"</#if>
											</#if>
									</#if>
								/>
							</div>
						</li>
				<#elseif subTableField.show_type=='password'>
							<li id="${sub}[0].${subTableField.field_name}" class="clearfix " typ="password" reqd="1">
								<label class="desc">${subTableField.content}: <#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
								<div class="content">
									<input 
										type="text" 
										maxlength="256" 
										class="ui-input-text xl input fld" 
										name="${sub}[0].${subTableField.field_name}" 
										id="${sub}[0].${subTableField.field_name}" 
										 <#-- update--begin--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
										<#if subTableField.field_must_input?if_exists?html != ''><#if subTableField.field_must_input == 'Y' || subTableField.is_null != 'Y'>ignore="checked"<#else>ignore="ignore"</#if><#elseif subTableField.is_null != "Y">ignore="checked"<#else>ignore="ignore"</#if>
										<#-- update--end--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
										${subTableField.extend_json?if_exists}
										<#if subTableField.operationCodesReadOnly?exists> readonly = "readonly"</#if>
										<#if subTableField.field_valid_type?if_exists?html != ''>
											datatype="${subTableField.field_valid_type?if_exists?html}" 
											<#else>
												<#if subTableField.is_null != 'Y'>datatype="*"</#if>
										</#if>
									/>
								</div>
							</li>
				<#elseif subTableField.show_type=='radio'>
							<li id="${subTableField.dict_field?if_exists?html}" class="clearfix " typ="radio" reqd="1">
								<label class="desc">${subTableField.content}: <#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
								<div class="content">
									<fieldset class="controlgroup">
										<@DictData name="${subTableField.dict_field?if_exists?html}" text="${subTableField.dict_text?if_exists?html}" tablename="${subTableField.dict_table?if_exists?html}" var="dataList">
											<#list dataList as dictdata> 
												<label <#if dictdata_index==0>class="first"</#if>>
													<input 
														value="${dictdata.typecode?if_exists?html}" 
														${subTableField.extend_json?if_exists} 
														name="${sub}[0].${subTableField.field_name}" 
														type="radio" 
														<#if dictdata_index==0&&subTableField.is_null != 'Y'>datatype="*"</#if> 
														<#if subTableField.operationCodesReadOnly?if_exists>onclick="return false;"</#if>
													/><label></label>${dictdata.typename?if_exists?html}
												</label>
											</#list> 
										</@DictData>
									</fieldset>
								</div>
							</li>
					<#elseif subTableField.show_type=='checkbox'>
							<li id="${subTableField.dict_field?if_exists?html}" class="clearfix " typ="radio" reqd="1">
								<label class="desc">${subTableField.content}: <#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
								<div class="content">
									<fieldset class="controlgroup">
										<@DictData name="${subTableField.dict_field?if_exists?html}" text="${subTableField.dict_text?if_exists?html}" tablename="${subTableField.dict_table?if_exists?html}" var="dataList">
											<#list dataList as dictdata> 
												<label <#if dictdata_index==0>class="first"</#if>>
													<input 
														value="${dictdata.typecode?if_exists?html}" 
														${subTableField.extend_json?if_exists} 
														name="${sub}[0].${subTableField.field_name}" 
														type="checkbox" 
														<#if subTableField.operationCodesReadOnly?if_exists>onclick="return false;"</#if>
														<#if dictdata_index==0&&subTableField.is_null != 'Y'>datatype="*"</#if> 
													/><label></label>${dictdata.typename?if_exists?html}
												</label>
											</#list> 
										</@DictData>
									</fieldset>
								</div>
							</li>
					<#elseif subTableField.show_type=='list'>
						<li id="${sub}[0].${subTableField.field_name}" class="clearfix " typ="list">
							<label class="desc">${subTableField.content}: <#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
							<div class="content">
									<@DictData name="${subTableField.dict_field?if_exists?html}" text="${subTableField.dict_text?if_exists?html}" tablename="${subTableField.dict_table?if_exists?html}" var="dataList">
										<select 
											id="${sub}[0].${subTableField.field_name}" 
											${subTableField.extend_json?if_exists} 
											name="${sub}[0].${subTableField.field_name}" 
											<#if subTableField.operationCodesReadOnly?if_exists>
												onfocus="this.defOpt=this.selectedIndex" onchange="this.selectedIndex=this.defOpt;"</#if><#if subTableField.is_null != 'Y'>datatype="*"
											</#if> 
											class="ui-input-select province fld"
											>
											<#list dataList as dictdata> 
												<option 
													value="${dictdata.typecode?if_exists?html}" 
													<#if dictdata.typecode?if_exists?html=="${subTableData['${subTableField.field_name}']?if_exists?html}">
														selected="selected"
													</#if>>${dictdata.typename?if_exists?html}
												</option> 
											</#list> 
										</select>
									</@DictData>
							</div>
						</li>
				<#elseif subTableField.show_type=='date'>
					<li id="${sub}[0].${subTableField.field_name}" class="clearfix " typ="date" reqd="1">
							<label class="desc">${subTableField.content}: <#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
							<div class="content">
								<input 
									type="text" 
									maxlength="256" 
									class="ui-input-text xl input fld" 
									name="${sub}[0].${subTableField.field_name}" 
									id="${sub}[0].${subTableField.field_name}"
									 <#-- update--begin--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
									<#if subTableField.field_must_input?if_exists?html != ''><#if subTableField.field_must_input == 'Y' || subTableField.is_null != 'Y'>ignore="checked"<#else>ignore="ignore"</#if><#elseif subTableField.is_null != "Y">ignore="checked"<#else>ignore="ignore"</#if>
									<#-- update--end--author:zhangjiaqiang Date:20170417 for:增加校验必填项 --> 
									${subTableField.extend_json?if_exists}
									onClick="WdatePicker({<#if subTableField.operationCodesReadOnly?if_exists> readonly = true</#if>})" 
									<#if subTableField.operationCodesReadOnly?exists> readonly = "readonly"</#if>
									<#if subTableField.field_valid_type?if_exists?html != ''>
										datatype="${subTableField.field_valid_type?if_exists?html}" 
										<#else>
											<#if subTableField.is_null != 'Y'>datatype="*"</#if>
									</#if>
								/>
							</div>
						</li>
					<#elseif subTableField.show_type=='datetime'>
						<li id="${sub}[0].${subTableField.field_name}" class="clearfix " typ="date" reqd="1">
							<label class="desc">${subTableField.content}: <#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
							<div class="content">
								<input 
									type="text" 
									maxlength="256" 
									class="ui-input-text xl input fld" 
									name="${sub}[0].${subTableField.field_name}" 
									id="${sub}[0].${subTableField.field_name}" 
									 <#-- update--begin--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
									<#if subTableField.field_must_input?if_exists?html != ''><#if subTableField.field_must_input == 'Y' || subTableField.is_null != 'Y'>ignore="checked"<#else>ignore="ignore"</#if><#elseif subTableField.is_null != "Y">ignore="checked"<#else>ignore="ignore"</#if>
									<#-- update--end--author:zhangjiaqiang Date:20170417 for:增加校验必填项 -->
									${subTableField.extend_json?if_exists}
									onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'<#if subTableField.operationCodesReadOnly?if_exists> readonly = true</#if>})" 
									<#if subTableField.operationCodesReadOnly?exists> readonly = "readonly"</#if>
									<#if subTableField.field_valid_type?if_exists?html != ''>
										datatype="${subTableField.field_valid_type?if_exists?html}" 
										<#else>
											<#if subTableField.is_null != 'Y'>datatype="*"</#if>
									</#if>
								/>
							</div>
						</li>
						<#-- update--begin--author:taoyan Date:20170707 for:TASK #2918 【bug】online样式，通用移动模板2一对多 -->
						<#else>
						<li id="${sub}[0].${subTableField.field_name}" class="clearfix " typ="name" reqd="1">
							<label class="desc">${subTableField.content}:<#if subTableField.is_null != 'Y'><span class="req">*</span></#if></label>
							<div class="content">
								<input 
									type="text" 
									maxlength="256" 
									class="ui-input-text xl input fld" 
									name="${sub}[0].${subTableField.field_name}" 
									id="${sub}[0].${subTableField.field_name}" 
									<#if subTableField.field_must_input?if_exists?html != ''><#if subTableField.field_must_input == 'Y' || subTableField.is_null != 'Y'>ignore="checked"<#else>ignore="ignore"</#if><#elseif subTableField.is_null != "Y">ignore="checked"<#else>ignore="ignore"</#if>
									${subTableField.extend_json?if_exists}
									<#if subTableField.field_valid_type?if_exists?html != ''>
										datatype="${subTableField.field_valid_type?if_exists?html}" 
										<#else>
											<#if subTableField.type == 'int'>
												datatype="n"  <#if subTableField.is_null == 'Y'>ignore="ignore" </#if>
												<#elseif subTableField.type=='double'>
												datatype="/^(-?\d+)(\.\d+)?$/" <#if subTableField.is_null == 'Y'>ignore="ignore" </#if>
												<#else>
												<#if subTableField.is_null != 'Y'>datatype="*"</#if>
											</#if>
									</#if>
								/>
							</div>
						</li>
						<#-- update--end--author:taoyan Date:20170707 for:TASK #2918 【bug】online样式，通用移动模板2一对多 -->
					</#if>
		    </#list>
		  </div>
	</#if>
</div>
	<li>
	 	<#--//update-begin--Author:Yandong  Date:20171227 for：TASK #2375 【online模板】通用移动模板002，有很多问题-->
		<button type="button" class="btn btn-primary btn-lg btn-block jeecgDetail" id="${sub}_addBtn">添加${field['${sub}'].head.content?if_exists?html}</button>
		<#--//update-end--Author:Yandong  Date:20171227 for：TASK #2375 【online模板】通用移动模板002，有很多问题-->
	</li>
	
	<script type="text/javascript">
		$(function(){
			$("#${sub}_addBtn").click(function(){
		 		 var div = $("#add_${sub}_template").html();
			 	 $("#${sub}_area").append(div);
			 	 resetNum${sub}("${sub}_area");
			 	 
			 	 $("button[name='${sub}_delBtn']").die().live('click',function(){
			 	 	$(this).parent().parent().parent().remove();   
		        	resetNum${sub}("${sub}_area");
			 	 });
			 	 
		    });  
			$("button[name='${sub}_delBtn']").click(function(){   
		       $(this).parent().parent().parent().remove();   
		        resetNum${sub}("${sub}_area");
		    });
	    });
	    
	    function resetNum${sub}(subid) {
	
		$("#"+subid+" div[name=items]").each(function(i){
			 var $this = $(this);
			 $this.find("span[name='index']").html(i+1);
			  
		  	$this.find('input, select').each(function(){
			var $cthis = $(this), name = $cthis.attr('name'), val = $this.val();
			if(name!=null){
				if (name.indexOf("#index#") >= 0){
					$cthis.attr("name",name.replace('#index#',i));
				}else{
					var s = name.indexOf("[");
					var e = name.indexOf("]");
					var new_name = name.substring(s+1,e);
					$cthis.attr("name",name.replace(new_name,i));
				}
			  }
		    });
		});
	  }
	</script>