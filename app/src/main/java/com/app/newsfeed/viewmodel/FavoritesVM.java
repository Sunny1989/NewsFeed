package com.app.newsfeed.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;

import com.app.newsfeed.model.database.SQLHelper;
import com.app.newsfeed.model.database.SQLManager;
import com.app.newsfeed.model.pojo.ArticlesPojo;

public class FavoritesVM {

    public ObservableArrayList<ArticlesPojo> articlesPojos = new ObservableArrayList<>();
    public Context context;

    public FavoritesVM(Context context) {
        this.context = context;
    }

    public void loadData() {
        //This will be loaded from database. As we will store the link.
        SQLManager sqlManager = SQLManager.getInstance(context);
        sqlManager.openDatabase();
        String sqlFavorites = "Select * from " + SQLHelper.TABLE_NEWS_BOOKMARK;
        articlesPojos = sqlManager.getNewsBookmark(sqlFavorites);
        sqlManager.closeDatabase();
    }

}
