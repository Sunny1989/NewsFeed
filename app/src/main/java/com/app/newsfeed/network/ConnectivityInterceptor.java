package com.app.newsfeed.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.app.newsfeed.utility.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

    private Context context;

    public ConnectivityInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (!Utils.checkNetwork(context)) {
            throw new IOException("No connectivity");
        } else {
            Response response = null;
            try {
                Request request = chain.request();
                response = chain.proceed(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
