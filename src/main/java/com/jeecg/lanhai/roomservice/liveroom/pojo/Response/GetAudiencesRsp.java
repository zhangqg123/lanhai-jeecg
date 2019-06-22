package com.jeecg.lanhai.roomservice.liveroom.pojo.Response;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;



import com.jeecg.lanhai.roomservice.liveroom.pojo.Audience;

import java.util.ArrayList;

public class GetAudiencesRsp extends BaseRsp {
    private int audienceCount = 0;
    @JsonProperty(value = "audiences")
    private ArrayList<Audience> audiences = new ArrayList<>();

    public int getAudienceCount() {
        return audienceCount;
    }

    @JsonIgnore
    public ArrayList<Audience> getAudiences() {
        return audiences;
    }

    public void setAudiences(ArrayList<Audience> audiences) {
        this.audiences = audiences;
    }
}
