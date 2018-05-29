package com.atg.openssp.common.core.broker.dto;

import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.*;

public class VideoAdDtoTest {

    @Test
    public void getVideoAds() {
        Gson gson = new Gson();

        String json = "{\"videoAd\":[{\"id\":\"2af9ffe5-0115-4ea0-a08c-ec8190a19008\",\"bidfloor_currency\":\"USD\",\"bidfloor_price\":0.0,\"w\":300,\"h\":200,\"videoad_id\":\"1\",\"mimes\":[],\"min_duration\":10,\"max_duration\":60,\"start_delay\":0,\"protocols\":[],\"battr\":[],\"linearity\":1,\"companionad\":[],\"api\":[]}]}";
        VideoAdDto dto = gson.fromJson(json, VideoAdDto.class);
        System.out.println(dto);
    }
}