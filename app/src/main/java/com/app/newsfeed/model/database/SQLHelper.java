package com.app.newsfeed.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {

    private static SQLHelper instance;
    //Its gonna change when we will put upgrade condition!
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "newsfeed.db";

    public static final String TABLE_NEWS_BOOKMARK = "tb_news_bookmark";

    public static final String COMMA_SEP = ",";

    public static final String SOURCE_ID = "sourceId";
    public static final String SOURCE_NAME = "sourceName";
    public static final String AUTHOR = "author";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String URL = "url";
    public static final String URL_TO_IMAGE = "urlToImage";
    public static final String PUBLISHED_AT = "publishedAt";
    public static final String CONTENT = "content";

    private String SQL_CREATE_TABLE_NEWS_BOOKMARK = "CREATE TABLE IF NOT EXISTS " + TABLE_NEWS_BOOKMARK + " ("
            + SOURCE_ID + " TEXT " + COMMA_SEP + SOURCE_NAME + " TEXT" + COMMA_SEP
            + AUTHOR + " TEXT" + COMMA_SEP + TITLE + " TEXT" + COMMA_SEP + DESCRIPTION + " TEXT" + COMMA_SEP +
            URL + " TEXT PRIMARY KEY" + COMMA_SEP + URL_TO_IMAGE + " TEXT" + COMMA_SEP +
            PUBLISHED_AT + " TEXT" + COMMA_SEP + CONTENT + " TEXT" + ");";


    public static synchronized SQLHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SQLHelper(context);
        }
        return instance;
    }

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NEWS_BOOKMARK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

}
