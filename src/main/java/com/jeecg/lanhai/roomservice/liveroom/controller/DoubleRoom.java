package com.jeecg.lanhai.roomservice.liveroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeecg.lanhai.roomservice.liveroom.logic.RoomMgr;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Room;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.AddPusherReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.CreateRoomReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.DeletePusherReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.DestroyRoomReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.GetPushUrlReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.GetPushersReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.GetRoomListReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.PusherHeartbeatReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.BaseRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.CreateRoomRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.GetPushUrlRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.GetRoomListRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.LoginRsp;
import com.jeecg.lanhai.roomservice.liveroom.service.RoomService;
import com.jeecg.lanhai.roomservice.liveroom.service.UtilService;

/**
 * 双人房间接口
 */
@Controller
@ResponseBody
@RequestMapping("/weapp/double_room")
public class DoubleRoom {
    @Autowired
    RoomService roomService;

    @Autowired
    UtilService utilService;

    @ResponseBody
    @RequestMapping("login")
    public LoginRsp login(String sdkAppID, String accountType, String userID, String userSig){
        return roomService.login(sdkAppID, accountType, userID, userSig);
    }

    @ResponseBody
    @RequestMapping("logout")
    public BaseRsp logout(String userID, String token){
        return roomService.logout(userID, token);
    }

    @ResponseBody
    @RequestMapping("get_push_url")
    public GetPushUrlRsp get_push_url(String userID, String token, @RequestBody GetPushUrlReq req){
        return utilService.getPushUrl(userID, token, req);
    }

    @ResponseBody
    @RequestMapping("get_room_list")
    public GetRoomListRsp get_room_list(String userID, String token, @RequestBody GetRoomListReq req){
        return roomService.getRoomList(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }

    @ResponseBody
    @RequestMapping("get_pushers")
    public Room get_pushers(String userID, String token, @RequestBody GetPushersReq req){
        return roomService.getPushers(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }

    @ResponseBody
    @RequestMapping("create_room")
    public CreateRoomRsp create_room(String userID, String token, @RequestBody CreateRoomReq req){
        return roomService.createRoom(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }

    @ResponseBody
    @RequestMapping("destroy_room")
    public BaseRsp destroy_room(String userID, String token, @RequestBody DestroyRoomReq req) {
        return roomService.destroyRoom(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }

    @ResponseBody
    @RequestMapping("add_pusher")
    public BaseRsp add_pusher(String userID, String token, @RequestBody AddPusherReq req) {
        return roomService.addPusher(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }

    @ResponseBody
    @RequestMapping("delete_pusher")
    public BaseRsp delete_pusher(String userID, String token, @RequestBody DeletePusherReq req) {
        return roomService.deletePusher(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }

    @ResponseBody
    @RequestMapping("pusher_heartbeat")
    public BaseRsp pusher_heartbeat(String userID, String token, @RequestBody PusherHeartbeatReq req) {
        return roomService.pusherHeartbeat(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }
}
