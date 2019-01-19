package com.app.newsfeed.network;

import android.support.annotation.NonNull;

import com.app.newsfeed.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .header("X-Api-Key", BuildConfig.NEWS_API_KEY);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
