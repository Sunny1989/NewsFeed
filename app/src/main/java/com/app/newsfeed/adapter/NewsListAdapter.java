package com.app.newsfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.newsfeed.R;
import com.app.newsfeed.databinding.NewsItemBinding;
import com.app.newsfeed.model.database.SQLManager;
import com.app.newsfeed.model.pojo.ArticlesPojo;
import com.app.newsfeed.utility.Constants;
import com.app.newsfeed.view.NewsDetailActivity;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsItemHolder> {

    private ObservableList<ArticlesPojo> articleList;
    private Context context;
    private RecyclerView rvArticles;
    private boolean isShowBookmark;

    public NewsListAdapter(RecyclerView rvArticles, ObservableArrayList<ArticlesPojo> articleList, boolean isShowBookmark) {
        this.articleList = articleList;
        this.rvArticles = rvArticles;
        this.isShowBookmark = isShowBookmark;
    }

    @Override
    public NewsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_item, parent, false);
        context = parent.getContext();
        return new NewsItemHolder(v);
    }

    @Override
    public void onBindViewHolder(final NewsItemHolder holder, int position) {
        ArticlesPojo articlesPojo = articleList.get(position);
        holder.newsItemBinding.setArticle(articlesPojo);
        holder.newsItemBinding.setShowBookmark(isShowBookmark);
        holder.newsItemBinding.tvNum.setText(String.valueOf(position + 1));
        holder.newsItemBinding.setNewsListAdapter(this);
        holder.newsItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (articleList == null) {
            return 0;
        } else {
            return articleList.size();
        }
    }

    public class NewsItemHolder extends RecyclerView.ViewHolder {
        public NewsItemBinding newsItemBinding;

        public NewsItemHolder(View itemView) {
            super(itemView);
            newsItemBinding = DataBindingUtil.bind(itemView);
        }
    }

    /**
     * This method will store the news record as a bookmark in database.
     *
     * @param view
     */
    public void bookMarkNews(View view) {
        int position = rvArticles.getChildLayoutPosition((View) view.getParent());
        ArticlesPojo articlesPojo = articleList.get(position);
        SQLManager sqlManager = SQLManager.getInstance(context);
        sqlManager.openDatabase();
        boolean status = sqlManager.insertNewsBookmark(articlesPojo);
        sqlManager.closeDatabase();
        Toast.makeText(context, "Article is bookmarked", Toast.LENGTH_SHORT).show();
    }

    public void openDetailScreen(View view) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        Bundle bundle = new Bundle();
        int position = rvArticles.getChildLayoutPosition(view);
        bundle.putParcelable(Constants.ARTICLE_OBJ, articleList.get(position));
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}