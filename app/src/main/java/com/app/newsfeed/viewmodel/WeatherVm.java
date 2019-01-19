package com.app.newsfeed.viewmodel;

import android.content.Context;
import android.widget.Toast;

import com.app.newsfeed.BuildConfig;
import com.app.newsfeed.listener.OnCompleteListenerWithData;
import com.app.newsfeed.model.pojo.GroupWeatherPojo;
import com.app.newsfeed.model.pojo.WeatherDataPojo;
import com.app.newsfeed.network.ApiClient;
import com.app.newsfeed.network.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherVm {

    //Data taken from OpenWeatherApi!
    private final int KOLKATA_ID = 1275004;
    private final int NEW_DELHI_ID = 1261481;
    private final int MUMBAI_ID = 1275339;
    private final int CHENNAI_ID = 1264527;

    private Context context;
    private OnCompleteListenerWithData onCompleteListenerWithData;

    public WeatherVm(Context context, OnCompleteListenerWithData onCompleteListenerWithData) {
        this.context = context;
        this.onCompleteListenerWithData = onCompleteListenerWithData;
    }


    public void getMultipleCitiesWeather() {
        ServiceGenerator.changeBaseURL(BuildConfig.WEATHER_BASE_URL);
        ApiClient apiClient = ServiceGenerator.createService(ApiClient.class, context);
        String defaultCities = KOLKATA_ID + "," + NEW_DELHI_ID + "," + MUMBAI_ID + "," + CHENNAI_ID;
        retrofit2.Call<GroupWeatherPojo> callNewsData = apiClient.callWeatherSeveralCities(defaultCities, "metric", BuildConfig.WEATHER_APP_ID);
        callNewsData.enqueue(new Callback<GroupWeatherPojo>() {
            @Override
            public void onResponse(Call<GroupWeatherPojo> call, Response<GroupWeatherPojo> response) {
                if (response.body() != null) {
                    if (onCompleteListenerWithData != null) {
                        onCompleteListenerWithData.onRequestSuccess(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<GroupWeatherPojo> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is responsible for getting weather report of specific city based on longitude and latitude.
     *
     * @param lat
     * @param lon
     */
    public void getCurrentCityWeather(double lat, double lon) {
        ServiceGenerator.changeBaseURL(BuildConfig.WEATHER_BASE_URL);
        ApiClient apiClient = ServiceGenerator.createService(ApiClient.class, context);
        retrofit2.Call<WeatherDataPojo> callNewsData = apiClient.callWeatherByCoordinates(lat, lon, BuildConfig.WEATHER_APP_ID);
        callNewsData.enqueue(new Callback<WeatherDataPojo>() {
            @Override
            public void onResponse(Call<WeatherDataPojo> call, Response<WeatherDataPojo> response) {
                if (response.body() != null) {
                    if (onCompleteListenerWithData != null) {
                        onCompleteListenerWithData.onRequestSuccess(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherDataPojo> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
