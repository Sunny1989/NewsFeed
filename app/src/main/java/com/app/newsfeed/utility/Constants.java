package com.app.newsfeed.utility;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constants {

    @Retention(RetentionPolicy.SOURCE)
    // Enumerate valid values for this interface
    @IntDef({PROFILE, NEWS, WEATHER})
    // Create an interface for validating int types
    public @interface NavigationType {
    }

    // Declare the constants
    public static final int PROFILE = 0;
    public static final int NEWS = 1;
    public static final int WEATHER = 2;


    public static final String PROFILE_LBL = "Profile";
    public static final String NEWS_LBL = "News";
    public static final String WEATHER_LBL = "Weather";
    public static final String NATIONAL_LBL = "National";
    public static final String INTERNATIONAL_LBL = "International";
    public static final String SPORTS_LBL = "Sports";
    public static final String FAVORITES_LBL = "Favorites";
    public static final String LATEST_LBL = "Latest";

    public static final String ARTICLE_OBJ = "article_obj";

    public static final String LOCATION_OBJ = "location_obj";
    public static final String ADDRESS_OBJ = "address_obj";
    public static final String ADD_RECEIVER = "add_receiver";

    public static final int LOCATION_GPS_REQUEST_CODE = 3;

    public static final String NEWS_TYPE = "news_type";

}
