package com.jeecg.lanhai.roomservice.webrtc.pojo.Response;


import java.util.ArrayList;

import com.jeecg.lanhai.roomservice.webrtc.pojo.Member;

public class GetRoomMembersRsp extends BaseRsp {
    private ArrayList<Member> members = new ArrayList<>();

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }
}
