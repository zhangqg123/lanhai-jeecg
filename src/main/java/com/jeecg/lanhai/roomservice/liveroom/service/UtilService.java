package com.jeecg.lanhai.roomservice.liveroom.service;


import java.util.Map;

import com.jeecg.lanhai.roomservice.liveroom.pojo.Request.GetPushUrlReq;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.GetPushUrlRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.GetTestPushUrlRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.GetTestRtmpAccUrlRsp;
import com.jeecg.lanhai.roomservice.liveroom.pojo.Response.MergeStreamRsp;


public interface UtilService {
    GetPushUrlRsp getPushUrl(String userID, String token);

    GetPushUrlRsp getPushUrl(String userID, String token, GetPushUrlReq req);

    MergeStreamRsp mergeStream(String userID, String token, Map map);

    GetTestPushUrlRsp getTestPushUrl();

    GetTestRtmpAccUrlRsp getTestRtmpAccUrl();
}
