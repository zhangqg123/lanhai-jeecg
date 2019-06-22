package com.jeecg.lanhai.roomservice.webrtc.service;

import com.jeecg.lanhai.roomservice.webrtc.pojo.Request.CreateRoomReq;
import com.jeecg.lanhai.roomservice.webrtc.pojo.Request.EnterRoomReq;
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


public interface WebRTCRoomService {
    GetLoginInfoRsp getLoginInfo(String userID);
    CreateRoomRsp createRoom(CreateRoomReq req);
    EnterRoomRsp enterRoom(EnterRoomReq req);
    BaseRsp quitRoom(QuitRoomReq req);
    BaseRsp heartbeat(HeartBeatReq req);
    GetRoomListRsp getRoomList(GetRoomListReq req);
    GetRoomMembersRsp getRoomMembers(GetRoomMembersReq roomID);
}