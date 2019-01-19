package com.app.newsfeed.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupWeatherPojo {
    public int cnt;

    @SerializedName("list")
    public List<WeatherDataPojo> list;
}
