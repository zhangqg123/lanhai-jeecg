package com.jeecg.lanhai.roomservice.liveroom.service;


import java.util.Map;

import com.jeecg.lanhai.roomservice.liveroom.pojo.Room;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.AddAudienceReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.AddPusherReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.CreateRoomReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.DelAudienceReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.DeletePusherReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.DestroyRoomReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.GetAudiencesReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.GetCustomInfoReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.GetPushersReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.GetRoomListReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.PusherHeartbeatReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.SetCustomInfoReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.BaseRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.CreateRoomRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.GetAudiencesRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.GetCustomInfoRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.GetRoomListRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.LoginRsp;


public interface RoomService {
    LoginRsp login(String sdkAppID, String accountType, String userID, String userSig);

    BaseRsp logout(String userID, String token);

    GetRoomListRsp getRoomList(String userID, String token, GetRoomListReq req, int type);

    Room getPushers(String userID, String token, GetPushersReq req, int type);

    CreateRoomRsp createRoom(String userID, String token, CreateRoomReq req, int type);

    BaseRsp destroyRoom(String userID, String token, DestroyRoomReq req, int type);

    BaseRsp addPusher(String userID, String token, AddPusherReq req, int type);

    BaseRsp deletePusher(String userID, String token, DeletePusherReq req, int type);

    void test();

    BaseRsp pusherHeartbeat(String userID, String token, PusherHeartbeatReq req, int type);

    GetCustomInfoRsp getCustomInfo(String userID, String token, GetCustomInfoReq req, int type);

    GetCustomInfoRsp setCustomInfo(String userID, String token, SetCustomInfoReq req, int type);

    BaseRsp addAudience(String userID, String token, AddAudienceReq req, int type);

    BaseRsp delAudience(String userID, String token, DelAudienceReq req, int type);

    GetAudiencesRsp getAudiences(String userID, String token, GetAudiencesReq req, int type);
}
