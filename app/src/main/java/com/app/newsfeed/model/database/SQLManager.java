package com.app.newsfeed.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.ObservableArrayList;

import com.app.newsfeed.model.pojo.ArticlesPojo;
import com.app.newsfeed.model.pojo.SourcePojo;


public class SQLManager {

    private static SQLManager instance;
    private static SQLHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    private SQLManager(Context context) {
        mContext = context;
    }

    public static synchronized SQLManager getInstance(Context context) {
        if (instance == null) {
            instance = new SQLManager(context);
        }

        if (mDatabaseHelper == null) {
            mDatabaseHelper = SQLHelper.getInstance(context.getApplicationContext());
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        // Opening new database
        if (mDatabase == null || !mDatabase.isOpen()) {
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
        }
    }

    /**
     * This method will insert a new record of News bookmark.
     *
     * @param articlesPojo
     * @return
     */
    public boolean insertNewsBookmark(ArticlesPojo articlesPojo) {
        ContentValues values = new ContentValues();
        values.put(SQLHelper.SOURCE_ID, articlesPojo.source.id);
        values.put(SQLHelper.SOURCE_NAME, articlesPojo.source.name);
        values.put(SQLHelper.AUTHOR, articlesPojo.author);
        values.put(SQLHelper.TITLE, articlesPojo.title);
        values.put(SQLHelper.DESCRIPTION, articlesPojo.description);
        values.put(SQLHelper.URL, articlesPojo.url);
        values.put(SQLHelper.URL_TO_IMAGE, articlesPojo.urlToImage);
        values.put(SQLHelper.PUBLISHED_AT, articlesPojo.publishedAt);
        values.put(SQLHelper.CONTENT, articlesPojo.content);
        long id = mDatabase.insertWithOnConflict(SQLHelper.TABLE_NEWS_BOOKMARK, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            //update scenario, but here not required to update.
            return false;
        } else {
            return true;
        }
    }


    /**
     * @param query
     * @return
     */
    public ObservableArrayList<ArticlesPojo> getNewsBookmark(String query) {
        ObservableArrayList<ArticlesPojo> articleList = new ObservableArrayList<>();
        try {
            Cursor cursor = mDatabase.rawQuery(query, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ArticlesPojo articlesPojo = new ArticlesPojo();
                    articlesPojo.source = new SourcePojo();
                    articlesPojo.source.id = cursor.getString(cursor.getColumnIndex(SQLHelper.SOURCE_ID));
                    articlesPojo.source.name = cursor.getString(cursor.getColumnIndex(SQLHelper.SOURCE_NAME));
                    articlesPojo.author = cursor.getString(cursor.getColumnIndex(SQLHelper.AUTHOR));
                    articlesPojo.title = cursor.getString(cursor.getColumnIndex(SQLHelper.TITLE));
                    articlesPojo.description = cursor.getString(cursor.getColumnIndex(SQLHelper.DESCRIPTION));
                    articlesPojo.url = cursor.getString(cursor.getColumnIndex(SQLHelper.URL));
                    articlesPojo.urlToImage = cursor.getString(cursor.getColumnIndex(SQLHelper.URL_TO_IMAGE));
                    articlesPojo.publishedAt = cursor.getString(cursor.getColumnIndex(SQLHelper.PUBLISHED_AT));
                    articlesPojo.content = cursor.getString(cursor.getColumnIndex(SQLHelper.CONTENT));
                    articleList.add(articlesPojo);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articleList;
    }

}

