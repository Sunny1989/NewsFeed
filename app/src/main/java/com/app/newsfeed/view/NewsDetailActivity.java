package com.app.newsfeed.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.app.newsfeed.BaseActivity;
import com.app.newsfeed.R;
import com.app.newsfeed.databinding.NewsDetailBinding;
import com.app.newsfeed.model.pojo.ArticlesPojo;
import com.app.newsfeed.utility.Constants;

public class NewsDetailActivity extends BaseActivity {

    private NewsDetailBinding newsDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);

        newsDetailBinding.toolbar.setTitle(R.string.news_detail);
        newsDetailBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(newsDetailBinding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArticlesPojo articlesPojo = bundle.getParcelable(Constants.ARTICLE_OBJ);
            if (articlesPojo != null) {
                newsDetailBinding.setArticle(articlesPojo);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
