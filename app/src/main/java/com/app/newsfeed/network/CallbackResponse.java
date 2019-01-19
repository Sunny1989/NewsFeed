package com.app.newsfeed.network;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.newsfeed.model.pojo.BasePojo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * We will use this in almost cases as we just need to show response in activity and for failure we just show toast or snackbar.
 * Only in few cases we will require to implement the default Callback.
 */

public abstract class CallbackResponse<T> implements Callback<T>, ResponseListener<T> {

    //We might take <T> to responsebody which is default for all requests.
    private Context context;
    private final String OK_STATUS = "ok";
    private ProgressBar pbNews;

    public CallbackResponse(Context context, ProgressBar pbNews) {
        this.context = context;
        //Over here you can start with progress dialog/bar!
        if (pbNews != null) {
            pbNews.setVisibility(View.VISIBLE);
            this.pbNews = pbNews;
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Response<BasePojo> basePojoResponse = (Response<BasePojo>) response;
        if (basePojoResponse.body().status.equalsIgnoreCase(OK_STATUS)) {
            this.onFinalResponse(call, response);
        } else {
            //Some error occurred!
            Toast.makeText(context, basePojoResponse.body().status, Toast.LENGTH_SHORT).show();
        }
        closeProgress();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        //This will be when request failed to execute.
        //May be server down, no network, or authentication failure on server, TimeoutError
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
        closeProgress();
    }

    private void closeProgress() {
        if (pbNews != null) {
            pbNews.setVisibility(View.GONE);
        }
    }
}
