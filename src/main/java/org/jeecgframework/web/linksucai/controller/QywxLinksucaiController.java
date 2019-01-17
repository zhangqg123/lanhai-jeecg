package org.jeecgframework.web.linksucai.controller;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.p3.core.util.plugin.ContextHolderUtils;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
//import org.jeecgframework.poi.excel.entity.ExcelTitle;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.linksucai.entity.WeixinLinksucaiEntity;
import org.jeecgframework.web.linksucai.oauth2.rule.RemoteWeixinMethod;
import org.jeecgframework.web.linksucai.oauth2.util.SignatureUtil;
import org.jeecgframework.web.linksucai.service.WeixinLinksucaiServiceI;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.jeecg.demo.entity.JeecgDemoExcelEntity;
import com.jeecg.qywx.account.dao.QywxAgentDao;
import com.jeecg.qywx.account.entity.QywxAgent;
import com.jeecg.qywx.base.service.QywxGzuserinfoService;


/**   
 * @Title: Controller
 * @Description: 链接素材
 * @author onlineGenerator
 * @date 2015-01-22 21:39:44
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/qywxLinksucaiController")
public class QywxLinksucaiController extends BaseController {
	/**
	 * 签名密钥key
	 */
	private static final String SIGN_KEY = "4B6CAED6F7B19126F72780372E839CC47B1912B6CAED753F";
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(QywxLinksucaiController.class);

	@Autowired
	private WeixinLinksucaiServiceI weixinLinksucaiService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private RemoteWeixinMethod remoteWeixinMethod;
//	@Autowired
//	private WeixinAccountServiceI weixinAccountService;
	@Autowired
	private QywxGzuserinfoService qywxGzuserinfoService;

	@Autowired
	private QywxAgentDao qywxAgentDao;

	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
     * 转向私有模板
     * @return
     */
	@RequestMapping(params = "qywxAgentList")
	public ModelAndView privateList() {
		return new ModelAndView("weixin/guanjia/qylink/privateWeixinLinksucaiList");
	}
	
    @RequestMapping(params = "privateDatagrid")
	@ResponseBody
	/**
	 * 私有查询数据
	 * @param newsTemplate
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	public void privateDatagrid(WeixinLinksucaiEntity weixinLinksucai,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
    	CriteriaQuery cq = new CriteriaQuery(WeixinLinksucaiEntity.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, weixinLinksucai);
		String qywxAccountId = ResourceUtil.getConfigByName("qywxAccountId");
        cq.eq("accountid", qywxAccountId);
		cq.eq("shareStatus", "display");
		cq.add();
//		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,weixinLinksucai);
//		this.weixinLinksucaiService.getDataGridReturn(cq, true);
        this.systemService.getDataGridReturn(cq, true);
//		String baseurl = ResourceUtil.getConfigByName("domain");
//		
//		for(int i=0;i<dataGrid.getResults().size();i++)
//		{
//			WeixinLinksucaiEntity  t=(WeixinLinksucaiEntity) dataGrid.getResults().get(i);
//			
//			String inner_link = baseurl+"/qywxLinksucaiController.do?link&id="+t.getId();
//			t.setInnerLink(inner_link);
//		}
//
//		//update-begin--Author:macaholin  Date:20150404 for：新增微信公众账号邮编名称（不存储数据库，只有用于显示），用于区分相同素材标题情况下是哪个父亲账号分享下来的
//		List<WeixinAccountEntity> accountList = weixinAccountService.loadAll(WeixinAccountEntity.class);
//		//update-end--Author:macaholin  Date:20150404 for：新增微信公众账号邮编名称（不存储数据库，只有用于显示），用于区分相同素材标题情况下是哪个父亲账号分享下来的
		TagUtil.datagrid(response, dataGrid);
	}
    
	/**
	 * 链接跳转
	 */
	@RequestMapping(params = "link")
	public void link(WeixinLinksucaiEntity weixinLinksucai,HttpServletRequest request, HttpServletResponse response) {
		//获取请求路径
		String backUrl = this.getRequestUrlWithParams(request);
		//URL配置ID
		String id = request.getParameter("id");
		//如果带有参数jwid，标示指定公众号，逻辑如下
		String qyUserId = null;
		String outer_link_deal = null;
		String requestQueryString = null;
		
		String outUrl = null;
		//update-begin-------author:scott-----------date:20151012--------for:如果连接带参数jwid，则通过jwid原始ID获取公众号信息------------
		weixinLinksucai = systemService.getEntity(WeixinLinksucaiEntity.class, id);
		//微信公众账号ID
		//update-start--Author:scott  Date:20150809 for：【判断访问地址是否有附加参数，有的话原样带回去】----------------------
//	    if(request.getQueryString().contains(":8080")){
//			requestQueryString = (request.getRequestURL() + "?" + request.getQueryString()).replace(weixinLinksucai.getInnerLink(), "");	    	
//	    }else{
	    	requestQueryString = (request.getRequestURL() + "?" + request.getQueryString()).replace(":8080", "").replace(weixinLinksucai.getInnerLink(), "");
//	    }
	    
		outUrl = weixinLinksucai.getOuterLink();
		String agentId=weixinLinksucai.getAgentId();

		qyUserId=ResourceUtil.getQyUserId();
		if(oConvertUtils.isEmpty(qyUserId)){
			outer_link_deal = remoteWeixinMethod.callQyAuthor2ReturnUrl(request, agentId, backUrl);
		}	
		if(oConvertUtils.isEmpty(outer_link_deal)){
			qyUserId = ResourceUtil.getQyUserId();
			outer_link_deal = outUrl;
		}
			
		//-------------------------------------------------------------------------------------------------------------
		
		try {
			//---update-begin--author:scott-----date:20151127-----for:参数加签名----------------------------------
			if(outer_link_deal.indexOf("https://open.weixin.qq.com")!=-1){
				//针对调整到auth2.0链接不加签名
				response.sendRedirect(outer_link_deal);
			}else{
					//针对参数加签，防止用户篡改
					String sign = SignatureUtil.sign(SignatureUtil.getSignMap(outer_link_deal), SIGN_KEY);
//					System.out.println("------------outer_url------------"+outer_link_deal+"&agentId="+agentId+"&sign="+sign);
					response.sendRedirect(outer_link_deal+"&agentId="+agentId+"&sign="+sign);
			}
			//---update-end--author:scott-----date:20151127-----for:参数加签名----------------------------------
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除链接素材
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(WeixinLinksucaiEntity weixinLinksucai, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		weixinLinksucai = systemService.getEntity(WeixinLinksucaiEntity.class, weixinLinksucai.getId());
		message = "链接素材删除成功";
		
		if(weixinLinksucai.getAccountid().equals(ResourceUtil.getConfigByName("qywxAccountId"))
				||weixinLinksucai.getAccountid().equals(ResourceUtil.getConfigByName("qywxAccountId"))){
			try{
				weixinLinksucaiService.delete(weixinLinksucai);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}catch(Exception e){
				e.printStackTrace();
				message = "链接素材删除失败";
				throw new BusinessException(e.getMessage());
			}
		}else{
			message="父级共享的数据子公众帐号不能操作";
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除链接素材
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "链接素材删除成功";
		try{
			for(String id:ids.split(",")){
				WeixinLinksucaiEntity weixinLinksucai = systemService.getEntity(WeixinLinksucaiEntity.class, 
				id
				);
				if(!weixinLinksucai.getAccountid().equals(ResourceUtil.getConfigByName("qywxAccountId"))){
					continue;
				}
				weixinLinksucaiService.delete(weixinLinksucai);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "链接素材删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加链接素材
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(WeixinLinksucaiEntity weixinLinksucai, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "链接素材添加成功";
		try{
			Serializable id=weixinLinksucaiService.save(weixinLinksucai);
			String baseurl = ResourceUtil.getConfigByName("domain");
			String inner_link = baseurl+"/qywxLinksucaiController.do?link&id="+id;
			weixinLinksucai.setInnerLink(inner_link);
			weixinLinksucai.setShareStatus("display");
			weixinLinksucaiService.saveOrUpdate(weixinLinksucai);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "链接素材添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	@RequestMapping(params = "doInsert")
	@ResponseBody
	public AjaxJson doInsert(WeixinLinksucaiEntity weixinLinksucai, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "链接素材添加成功";
		try{
			WeixinLinksucaiEntity  weixinLinksucaiEntity = weixinLinksucaiService.findUniqueByProperty(WeixinLinksucaiEntity.class, "outerLink", weixinLinksucai.getOuterLink());
			if(weixinLinksucaiEntity!=null){
				message = "链接素材已存在";
				j.setObj(weixinLinksucaiEntity.getInnerLink());
			}else{
				weixinLinksucai.setAccountid(ResourceUtil.getConfigByName("qywxAccountId"));
				Serializable id=weixinLinksucaiService.save(weixinLinksucai);
//				String baseurl = ResourceUtil.getConfigByName("domain");
				String getContextPath = request.getContextPath();  
				String baseurl;
				if(request.getServerPort()==80){
					baseurl = request.getScheme()+"://"+request.getServerName()+getContextPath;		
				}else{
					baseurl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+getContextPath;	
				}	
				String inner_link = baseurl+"/qywxLinksucaiController.do?link&id="+id;
				weixinLinksucai.setInnerLink(inner_link);
				weixinLinksucaiService.saveOrUpdate(weixinLinksucai);
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
				j.setObj(inner_link);				
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "链接素材添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新链接素材
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(WeixinLinksucaiEntity weixinLinksucai, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "链接素材更新成功";
		WeixinLinksucaiEntity t = weixinLinksucaiService.get(WeixinLinksucaiEntity.class, weixinLinksucai.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(weixinLinksucai, t);
			weixinLinksucaiService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "链接素材更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 链接素材新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(WeixinLinksucaiEntity weixinLinksucai, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(weixinLinksucai.getId())) {
			weixinLinksucai = weixinLinksucaiService.getEntity(WeixinLinksucaiEntity.class, weixinLinksucai.getId());
			req.setAttribute("weixinLinksucaiPage", weixinLinksucai);
			
		}
		List<Map> agentList = getQywxAgents();
		req.setAttribute("agentList", agentList);
		req.setAttribute("accountid", ResourceUtil.getConfigByName("qywxAccountId"));
		return new ModelAndView("weixin/guanjia/qylink/weixinLinksucai-add");
	}

	private List<Map> getQywxAgents() {
		List<QywxAgent> qywxAgents = qywxAgentDao.getAllQywxAgents();
		List<Map> agentList=new ArrayList<Map>();
		for (QywxAgent  qywxagent:qywxAgents) {
			String id=qywxagent.getWxAgentid();
			String name = qywxagent.getAgentName();
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", id);
			map.put("name", name);
			agentList.add(map);
		}
		return agentList;
	}
	/**
	 * 链接素材编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(WeixinLinksucaiEntity weixinLinksucai, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(weixinLinksucai.getId())) {
			weixinLinksucai = weixinLinksucaiService.getEntity(WeixinLinksucaiEntity.class, weixinLinksucai.getId());
			req.setAttribute("weixinLinksucaiPage", weixinLinksucai);
			
		}
		List<Map> agentList = getQywxAgents();
		req.setAttribute("agentList", agentList);

		req.setAttribute("accountid", ResourceUtil.getConfigByName("qywxAccountId"));
		return new ModelAndView("weixin/guanjia/qylink/weixinLinksucai-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		return new ModelAndView("weixin/guanjia/qylink/weixinLinksucaiUpload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public void exportXls(WeixinLinksucaiEntity weixinLinksucai,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid) {
		response.setContentType("application/vnd.ms-excel");
		String codedFileName = null;
		OutputStream fOut = null;
		try {
			codedFileName = "链接素材";
			// 根据浏览器进行转码，使其支持中文文件名
			if (BrowserUtils.isIE(request)) {
				response.setHeader(
						"content-disposition",
						"attachment;filename="
								+ java.net.URLEncoder.encode(codedFileName,
										"UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"),
						"ISO8859-1");
				response.setHeader("content-disposition",
						"attachment;filename=" + newtitle + ".xls");
			}
			// 产生工作簿对象
			Workbook workbook = null;
			CriteriaQuery cq = new CriteriaQuery(WeixinLinksucaiEntity.class, dataGrid);
			org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, weixinLinksucai, request.getParameterMap());
			
			List<WeixinLinksucaiEntity> weixinLinksucais = this.weixinLinksucaiService.getListByCriteriaQuery(cq,false);
			workbook = ExcelExportUtil.exportExcel(new ExportParams("链接素材列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
					"导出信息"), WeixinLinksucaiEntity.class, weixinLinksucais);
			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (Exception e) {
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {

			}
		}
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public void exportXlsByT(WeixinLinksucaiEntity weixinLinksucai,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid) {
		response.setContentType("application/vnd.ms-excel");
		String codedFileName = null;
		OutputStream fOut = null;
		try {
			codedFileName = "链接素材";
			// 根据浏览器进行转码，使其支持中文文件名
			if (BrowserUtils.isIE(request)) {
				response.setHeader(
						"content-disposition",
						"attachment;filename="
								+ java.net.URLEncoder.encode(codedFileName,
										"UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"),
						"ISO8859-1");
				response.setHeader("content-disposition",
						"attachment;filename=" + newtitle + ".xls");
			}
			// 产生工作簿对象
			Workbook workbook = null;
			workbook = ExcelExportUtil.exportExcel(new ExportParams("链接素材列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
					"导出信息"), WeixinLinksucaiEntity.class, null);
			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (Exception e) {
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {

			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<WeixinLinksucaiEntity> listWeixinLinksucaiEntitys = 
					ExcelImportUtil.importExcel(file.getInputStream(),WeixinLinksucaiEntity.class,params);
				for (WeixinLinksucaiEntity weixinLinksucai : listWeixinLinksucaiEntitys) {
					weixinLinksucaiService.save(weixinLinksucai);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(ExceptionUtil.getExceptionMessage(e));
			}finally{
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return j;
	}
	
	@RequestMapping(params = "poplink")
	public ModelAndView poplink(ModelMap modelMap,
                                    @RequestParam String id) 
	{
		//WeixinLinksucaiEntity weixinLinksucai = weixinLinksucaiService.getEntity(WeixinLinksucaiEntity.class, id);
		
		ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");
		String absolutePathUrl =  bundler.getString("domain")  + "/qywxLinksucaiController.do?link&id=" + id;
        modelMap.put("url",absolutePathUrl);
		return new ModelAndView("weixin/guanjia/qylink/poplinksucai");
	}
	
	
	
	/**
     * 获取Request请求的路径信息 带参数
     * @param request
     * @return
     */
    private static String getRequestUrlWithParams(HttpServletRequest request){
  	  String backurl = request.getScheme()+"://"+request.getServerName()+request.getRequestURI()+"?"+request.getQueryString();
  	  return backurl;
    }
}
