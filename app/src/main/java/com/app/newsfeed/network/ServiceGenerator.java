package com.app.newsfeed.network;

import android.content.Context;

import com.app.newsfeed.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    //private static HeaderInterceptor headerInterceptor;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static ConnectivityInterceptor connectivityInterceptor;

    public static <S> S createService(Class<S> serviceClass, Context context) {
        addInterceptors(context);
        builder.client(httpClient.build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    public static void addInterceptors(Context context) {
        //First we checked for connectivity of Internet.
        if (!httpClient.interceptors().contains(connectivityInterceptor)) {
            connectivityInterceptor = new ConnectivityInterceptor(context);
            httpClient.addInterceptor(connectivityInterceptor);
        }
        /*if (!httpClient.interceptors().contains(headerInterceptor)) {
            headerInterceptor = new HeaderInterceptor();
            httpClient.addInterceptor(headerInterceptor);
        }*/
        if (BuildConfig.DEBUG && !httpClient.networkInterceptors().contains(loggingInterceptor)) {
            //We adding http logger intercept, as we want above interceptors details also (like headers).
            //condition is for debug and if its not added already!
            httpClient.addNetworkInterceptor(loggingInterceptor);
        }
    }

    /**
     * This method will change the base url at runtime. So if we have multiple servers then we can keep changing it.
     *
     * @param baseUrl
     */
    public static void changeBaseURL(String baseUrl) {
        builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());
    }

}
