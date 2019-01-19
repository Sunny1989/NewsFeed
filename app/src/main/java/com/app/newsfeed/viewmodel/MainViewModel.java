package com.app.newsfeed.viewmodel;

import com.app.newsfeed.listener.OnCompleteListener;
import com.app.newsfeed.utility.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainViewModel {

    public void prepareListData(List<String> expandableTitleList, HashMap<String, List<String>> expandableListData, OnCompleteListener onCompleteListener) {
        expandableListData = new HashMap<>();
        expandableTitleList = new ArrayList<>();
        //Profile
        expandableTitleList.add(Constants.PROFILE_LBL);
        expandableListData.put(Constants.PROFILE_LBL, null);

        //News
        expandableTitleList.add(Constants.NEWS_LBL);
        List<String> newsSubList = new ArrayList<>();
        newsSubList.add(Constants.NATIONAL_LBL);
        newsSubList.add(Constants.INTERNATIONAL_LBL);
        newsSubList.add(Constants.SPORTS_LBL);
        newsSubList.add(Constants.FAVORITES_LBL);
        newsSubList.add(Constants.LATEST_LBL);
        expandableListData.put(Constants.NEWS_LBL, newsSubList);

        //Weather
        expandableTitleList.add(Constants.WEATHER_LBL);
        expandableListData.put(Constants.WEATHER_LBL, null);

        if (onCompleteListener != null) {
            onCompleteListener.onComplete(expandableTitleList, expandableListData);
        }

    }
}
