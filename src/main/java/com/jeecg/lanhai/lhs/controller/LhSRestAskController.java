package com.jeecg.lanhai.lhs.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.velocity.VelocityContext;
import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.jeecgframework.p3.core.common.utils.AjaxJson;
import org.jeecgframework.p3.core.page.SystemTools;
import org.jeecgframework.p3.core.util.plugin.ContextHolderUtils;
import org.jeecgframework.p3.core.util.plugin.ViewVelocity;
import org.jeecgframework.p3.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jeecg.ask.entity.LhDsAskEntity;
import com.jeecg.ask.service.LhDsAskService;
import com.jeecg.ask.utils.DeleteFileUtil;
import com.jeecg.ask.utils.ImageUtil;
import com.jeecg.ask.utils.LstConstants;
import com.jeecg.lanhai.lhs.service.LhsService;
import com.jeecg.lhs.entity.LhSBlacklistEntity;
import com.jeecg.lhs.entity.LhSUserEntity;
import com.jeecg.lhs.service.LhSBlacklistService;
import com.jeecg.lhs.service.LhSUserService;
import com.jeecg.lhs.utils.ResponseMessage;
import com.jeecg.lhs.utils.Result;

 /**
 * 描述：黑名单表
 * @author: www.jeecg.org
 * @since：2019年04月23日 09时42分42秒 星期二 
 * @version:1.0
 */
@Controller
@RequestMapping("/lhs/lhSRestAsk")
@Api(value = "lh提问服务", description = "lh提问服务接口", tags = "lhSRestAskAPI")
public class LhSRestAskController extends BaseController{
	@Autowired
	private LhDsAskService lhDsAskService;
	@Autowired
	private LhsService lhsService;
  
	@ApiOperation(value = "创建提问")
	@RequestMapping(value= "/createAsk", method = RequestMethod.POST)
	public @ResponseBody AjaxJson createAsk(HttpServletRequest request, @RequestBody LhDsAskEntity lstAsk) {
		AjaxJson j = new AjaxJson();
		String xcxId=request.getParameter("xcxId");
		try {
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		    String sDate = simpleDateFormat.format(new Date());  
			lstAsk.setAskDate(simpleDateFormat.parse(sDate));
			lhDsAskService.insert(lstAsk);
			lhsService.sendWeChat(lstAsk,xcxId);
			j.setMsg("保存成功");
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("保存失败");
		    e.printStackTrace();
		}
		return j;
	}
	
	@ApiOperation(value = "更新提问")
	@RequestMapping(value= "/updateAsk", method = RequestMethod.POST)
	public @ResponseBody AjaxJson updateAsk(HttpServletRequest request, @RequestBody LhDsAskEntity lstAsk) {
		AjaxJson j = new AjaxJson();
		String xcxId=request.getParameter("xcxId");
		try {			
			lhDsAskService.update(lstAsk);
			String sendOpenId=null;
//			String sendMessage=null;
			if(lstAsk.getAskStatus()==LstConstants.TRANS_ASK||lstAsk.getAskStatus()==LstConstants.TRANS_ANSWER){
				sendOpenId=lstAsk.getDealOpenId();
//				sendMessage="翻译视频提交成功";
			}
			if(lstAsk.getAskStatus()==LstConstants.ANSWER_ASK){
				sendOpenId=lstAsk.getAnswerOpenId();
//				sendMessage="回答视频提交成功，请等待审核";
			}
			if(sendOpenId!=null){
				lhsService.sendWeChat(lstAsk,xcxId);
			}
			
			j.setMsg("保存成功");
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("保存失败");
		    e.printStackTrace();
		}
		return j;
	}
	
	@ApiOperation(value = "上传文件")
	@RequestMapping(value = "/doUpload", method = RequestMethod.POST)
	@ResponseBody
    public  AjaxJson doUpload(MultipartHttpServletRequest request, HttpServletResponse response) {
    	AjaxJson j = new AjaxJson();
		Map<String, Object> attributes = new HashMap<String, Object>();
		try {
			String openId = request.getParameter("openId");
			String existFile = request.getParameter("existFile");
			//获取所有文件名称  
	        String basePath = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("/");   
			Iterator<String> it = request.getFileNames();  
			while(it.hasNext()){  
			    //根据文件名称取文件  
			    MultipartFile multifile = request.getFile(it.next());  
//			    String fileName = multifile.getOriginalFilename(); 
		        String realFilename=multifile.getOriginalFilename();
		        String fileExtension = realFilename.substring(realFilename.lastIndexOf("."));
				String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";// 初始化种子
		        String fileName=RandomStringUtils.random(8, str)+System.currentTimeMillis()+fileExtension;
//				if(existFile!=null&existFile!=""){
//					fileName=existFile+fileExtension;
//				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		        String datedir = sdf.format(new Date());  
//			    String filePath = "upload/img/lst/"+datedir+"/"; 
			    String filePath = "upload/img/lst/"+openId+"/"; 
			    File file = new File(basePath+filePath);
				if (!file.exists()) {
					file.mkdirs();// 创建文件根目录
				}
				String filePathName = filePath+fileName;
				
			    String savePath = basePath+filePathName;

			    System.out.println(savePath);
			    File newFile = new File(savePath);  
			    //上传的文件写入到指定的文件中  
			    multifile.transferTo(newFile);  
			    boolean flag;
			    if(existFile!=null&existFile!=""){
				    String deletePath=basePath+filePath+existFile;
				    flag = DeleteFileUtil.deleteFile(deletePath);
			    }
//	    		FileCopyUtils.copy(realFilename.getBytes(), newFile);
//				ImageUtil.zoomImageScale(newFile, savePath,300);
			    attributes.put("url", filePath);
			    attributes.put("fileKey", fileName);
			    break;
			}
			j.setMsg("文件上传成功");
			j.setAttributes(attributes);
		}  catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg("文件上传失败");
		}  

		return j;
    }
	

}

