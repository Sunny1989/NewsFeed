package com.app.newsfeed.utility;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.app.newsfeed.R;
import com.app.newsfeed.adapter.NewsListAdapter;
import com.app.newsfeed.model.pojo.ArticlesPojo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class BindingUtils {

    @BindingAdapter("bind:imgUrl")
    public static void setImage(ImageView image, String url) {
        Glide.with(image.getContext())
                .load(url)
                .apply(new RequestOptions().override(160, 160).transforms(new CenterCrop(), new RoundedCorners(20)))
                .into(image);
    }

    @BindingAdapter("bind:imgUrlFull")
    public static void setFullImage(ImageView image, String url) {
        Glide.with(image.getContext())
                .load(url)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(20)))
                .into(image);
    }

    @BindingAdapter({"bind:favoriteitems", "bind:isShowBookmark"})
    public static void bindList(RecyclerView view, ObservableArrayList<ArticlesPojo> list, boolean isShowBookmark) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setAdapter(new NewsListAdapter(view, list, isShowBookmark));
    }

}
