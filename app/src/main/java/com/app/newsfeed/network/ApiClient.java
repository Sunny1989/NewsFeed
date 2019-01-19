package com.app.newsfeed.network;


import com.app.newsfeed.model.pojo.GroupWeatherPojo;
import com.app.newsfeed.model.pojo.NewsListPojo;
import com.app.newsfeed.model.pojo.WeatherDataPojo;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiClient {

    @GET("top-headlines")
    Call<NewsListPojo> callTopHeadlines(@Header("X-Api-Key") String apiKey, @QueryMap HashMap<String, String> filters);

    @GET("group")
    Call<GroupWeatherPojo> callWeatherSeveralCities(@Query("id") String ids, @Query("units") String units, @Query("APPID") String appId);

    @GET("weather")
    Call<WeatherDataPojo> callWeatherByCoordinates(@Query("lat") double lat, @Query("lon") double lon, @Query("APPID") String appId);
}
