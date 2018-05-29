package com.atg.openssp.common.core.broker.dto;

import com.atg.openssp.common.cache.dto.BannerAd;
import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.*;

public class BannerAdDtoTest {

    @Test
    public void getBannerAds() {
        Gson gson = new Gson();

        String json = "{\"bannerAds\":[{\"id\":\"450dac13-f568-4c74-ab0b-aa61645bd847\",\"bidfloor_currency\":\"USD\",\"bidfloor_price\":0.0,\"w\":300,\"h\":200,\"placement_id\":\"1\",\"adUnitCode\":\"auc1\",\"size\":\"300x200\",\"promo_sizes\":\"300x200\",\"mimes\":[],\"btypes\":[],\"battrs\":[],\"topframe\":1,\"expdir\":[],\"api\":[],\"wmax\":300,\"hmax\":200,\"wmin\":300,\"hmin\":200}]}\n";
//                "VideoAdDto dto = gson.fromJson("{\"videoAds\":[{\"id\":\"2af9ffe5-0115-4ea0-a08c-ec8190a19008\",\"bidfloor_currency\":\"USD\",\"bidfloor_price\":0.0,\"mimes\":[],\"w\":300,\"h\":200,\"min_duration\":10,\"max_duration\":60,\"start_delay\":0,\"protocols\":[],\"battr\":[],\"linearity\":1,\"companionad\":[],\"api\":[],\"videoad_id\":\"1\"}]}\n", VideoAdDto.class);
        BannerAdDto dto = gson.fromJson(json, BannerAdDto.class);
        System.out.println(dto);
    }
}