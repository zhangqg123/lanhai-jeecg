package com.jeecg.lanhai.roomservice.liveroom.pojo.Request;

public class GetLoginInfoReq {
    private String userID = "";
    private String code;
    private String openId;

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

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
