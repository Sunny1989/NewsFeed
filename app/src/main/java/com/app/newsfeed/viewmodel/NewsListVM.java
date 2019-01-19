package com.app.newsfeed.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.widget.ProgressBar;

import com.app.newsfeed.BuildConfig;
import com.app.newsfeed.listener.OnRequestCompleteListener;
import com.app.newsfeed.model.pojo.ArticlesPojo;
import com.app.newsfeed.model.pojo.NewsListPojo;
import com.app.newsfeed.network.ApiClient;
import com.app.newsfeed.network.CallbackResponse;
import com.app.newsfeed.network.ServiceGenerator;
import com.app.newsfeed.utility.Constants;
import com.app.newsfeed.utility.Utils;

import java.util.HashMap;

import retrofit2.Response;

public class NewsListVM {

    public ObservableArrayList<ArticlesPojo> articlesPojos = new ObservableArrayList<>();
    private Context context;
    private OnRequestCompleteListener onRequestCompleteListener;

    public NewsListVM(Context context, OnRequestCompleteListener onRequestCompleteListener) {
        this.context = context;
        this.onRequestCompleteListener = onRequestCompleteListener;
    }

    /**
     * This method will get the from server based on selected filters.
     *
     * @param newsType
     */
    public void loadNewsList(String newsType, ProgressBar pbNews) {
        HashMap<String, String> filters = getQueryParams(newsType);
        //changing the URL, as we weather api also.
        ServiceGenerator.changeBaseURL(BuildConfig.BASE_URL);
        ApiClient apiClient = ServiceGenerator.createService(ApiClient.class, context);
        retrofit2.Call<NewsListPojo> callNewsData = apiClient.callTopHeadlines(BuildConfig.NEWS_API_KEY, filters);
        callNewsData.enqueue(new CallbackResponse<NewsListPojo>(context, pbNews) {
            @Override
            public void onFinalResponse(retrofit2.Call<NewsListPojo> call, Response<NewsListPojo> response) {
                if (response != null && response.body() != null) {
                    articlesPojos = response.body().articles;
                    if (onRequestCompleteListener != null) {
                        onRequestCompleteListener.onRequestSuccess();
                    }
                }
            }
        });
    }

    /**
     * This method will add or update the query parameters based on selected Navigation Option.
     *
     * @param newsType
     * @return
     */
    public HashMap<String, String> getQueryParams(String newsType) {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("language", "en");
        //queryMap.put("sources", "google-news");

        if (newsType.equalsIgnoreCase(Constants.SPORTS_LBL)) {
            queryMap.put("category", "sports");
            queryMap.put("country", "in");
        } else if (newsType.equalsIgnoreCase(Constants.NATIONAL_LBL)) {
            queryMap.put("country", "in");
        } else if (newsType.equalsIgnoreCase(Constants.LATEST_LBL)) {
            //queryMap.remove("sources");
            queryMap.put("country", "in");
            queryMap.put("from", Utils.milliSecondsToDate(System.currentTimeMillis()));
            queryMap.put("to", Utils.milliSecondsToDate(System.currentTimeMillis()));
        } else if (newsType.equalsIgnoreCase(Constants.INTERNATIONAL_LBL)) {
            //queryMap.remove("sources");
            queryMap.remove("country");
        }

        return queryMap;
    }

}
