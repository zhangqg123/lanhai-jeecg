package com.jeecg.lanhai.roomservice.liveroom.pojo.Request;

public class GetLoginInfoReq {
    private String userID = "";
    private String code;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
