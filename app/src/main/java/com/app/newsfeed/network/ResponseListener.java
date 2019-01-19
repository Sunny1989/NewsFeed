package com.app.newsfeed.network;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sumeet on 30/10/17.
 */

public interface ResponseListener<T> {
    void onFinalResponse(Call<T> call, Response<T> response);
}
