package com.app.newsfeed.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticlesPojo implements Parcelable {

    public String content;
    public String publishedAt;
    public String author;
    public String urlToImage;
    public String title;
    public SourcePojo source;
    public String description;
    public String url;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.publishedAt);
        dest.writeString(this.author);
        dest.writeString(this.urlToImage);
        dest.writeString(this.title);
        dest.writeParcelable(this.source, flags);
        dest.writeString(this.description);
        dest.writeString(this.url);
    }

    public ArticlesPojo() {
    }

    protected ArticlesPojo(Parcel in) {
        this.content = in.readString();
        this.publishedAt = in.readString();
        this.author = in.readString();
        this.urlToImage = in.readString();
        this.title = in.readString();
        this.source = in.readParcelable(SourcePojo.class.getClassLoader());
        this.description = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<ArticlesPojo> CREATOR = new Parcelable.Creator<ArticlesPojo>() {
        @Override
        public ArticlesPojo createFromParcel(Parcel source) {
            return new ArticlesPojo(source);
        }

        @Override
        public ArticlesPojo[] newArray(int size) {
            return new ArticlesPojo[size];
        }
    };
}
