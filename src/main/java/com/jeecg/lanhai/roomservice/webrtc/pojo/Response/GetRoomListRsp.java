package com.jeecg.lanhai.roomservice.webrtc.pojo.Response;


import java.util.ArrayList;

import com.jeecg.lanhai.roomservice.webrtc.pojo.WebRTCRoom;

public class GetRoomListRsp extends BaseRsp {
    private ArrayList<WebRTCRoom> rooms;

    public ArrayList<WebRTCRoom> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<WebRTCRoom> rooms) {
        this.rooms = rooms;
    }
}
