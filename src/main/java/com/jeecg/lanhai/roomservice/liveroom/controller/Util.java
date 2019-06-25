package com.jeecg.lanhai.roomservice.liveroom.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.jeecg.lanhai.roomservice.liveroom.logic.IMMgr;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.GetLoginInfoReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.GetLoginInfoRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.GetTestPushUrlRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.GetTestRtmpAccUrlRsp;
import com.jeecg.lanhai.roomservice.liveroom.service.UtilService;

import javax.annotation.Resource;

@Controller
@ResponseBody
@RequestMapping("/weapp/utils")
public class Util {
    @Resource
    IMMgr imMgr;

    @Autowired
    UtilService utilService;

    @ResponseBody
    @RequestMapping(value = "get_login_info")
    public GetLoginInfoRsp get_login_info(@ModelAttribute GetLoginInfoReq req){
        return imMgr.getLoginInfo(req.getOpenId());
    }

    @ResponseBody
    @RequestMapping(value = "get_test_pushurl", method = RequestMethod.GET)
    public GetTestPushUrlRsp get_test_pushurl(){
        return utilService.getTestPushUrl();
    }

    @ResponseBody
    @RequestMapping("get_test_rtmpaccurl")
    public GetTestRtmpAccUrlRsp get_test_rtmpaccurl(){
        return utilService.getTestRtmpAccUrl();
    }
}
