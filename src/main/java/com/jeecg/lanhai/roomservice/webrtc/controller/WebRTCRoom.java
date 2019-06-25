package com.jeecg.lanhai.roomservice.webrtc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeecg.lanhai.roomservice.webrtc.pojo.Request.CreateRoomReq;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Request.EnterRoomReq;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Request.GetLoginInfoReq;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Request.GetRoomListReq;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Request.GetRoomMembersReq;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Request.HeartBeatReq;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Request.QuitRoomReq;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Response.BaseRsp;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Response.CreateRoomRsp;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Response.EnterRoomRsp;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Response.GetLoginInfoRsp;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Response.GetRoomListRsp;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Response.GetRoomMembersRsp;
import com.jeecg.lanhai.roomservice.webrtc.service.WebRTCRoomService;

/**
 * webrtc房间接口
 */
@Controller
@ResponseBody
@RequestMapping("/weapp/webrtc_room")
public class WebRTCRoom {
    @Autowired
    WebRTCRoomService webRTCRoomService;

    @ResponseBody
    @RequestMapping("get_login_info")
    public GetLoginInfoRsp get_login_info(@RequestBody GetLoginInfoReq req){
    	System.out.println("get login info 111");
        return webRTCRoomService.getLoginInfo(req.getUserID());
    }

    @ResponseBody
    @RequestMapping("create_room")
    public CreateRoomRsp create_room(@RequestBody CreateRoomReq req){
        return webRTCRoomService.createRoom(req);
    }

    @ResponseBody
    @RequestMapping("enter_room")
    public EnterRoomRsp enter_room(@RequestBody EnterRoomReq req){
        return webRTCRoomService.enterRoom(req);
    }

    @ResponseBody
    @RequestMapping("quit_room")
    public BaseRsp quit_room(@RequestBody QuitRoomReq req){
        return webRTCRoomService.quitRoom(req);
    }

    @ResponseBody
    @RequestMapping("heartbeat")
    public BaseRsp heartbeat(@RequestBody HeartBeatReq req) {
        return webRTCRoomService.heartbeat(req);
    }

    @ResponseBody
    @RequestMapping("get_room_list")
    public GetRoomListRsp get_room_list(@RequestBody GetRoomListReq req) {
        return webRTCRoomService.getRoomList(req);
    }

    @ResponseBody
    @RequestMapping("get_room_members")
    public GetRoomMembersRsp get_room_members(@RequestBody GetRoomMembersReq req){
        return webRTCRoomService.getRoomMembers(req);
    }
}