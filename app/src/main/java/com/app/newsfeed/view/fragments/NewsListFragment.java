package com.app.newsfeed.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.newsfeed.R;
import com.app.newsfeed.databinding.NewsListBinding;
import com.app.newsfeed.listener.OnRequestCompleteListener;
import com.app.newsfeed.utility.Constants;
import com.app.newsfeed.viewmodel.NewsListVM;

public class NewsListFragment extends Fragment implements OnRequestCompleteListener, SwipeRefreshLayout.OnRefreshListener {

    private NewsListBinding newsListBinding;
    private NewsListVM newsListVM;
    private String newsType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        newsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_list, container, false);
        newsListVM = new NewsListVM(getActivity(), this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            newsType = bundle.getString(Constants.NEWS_TYPE);
            if (newsType != null) {
                setData(newsType);
            }
        }

        newsListBinding.swipeNews.setOnRefreshListener(this);
        return newsListBinding.getRoot();
    }

    public void setData(String newsType) {
        newsListVM.loadNewsList(newsType, newsListBinding.pbNews);
    }

    @Override
    public void onRequestSuccess() {
        newsListBinding.setNewsListVM(newsListVM);
        newsListBinding.executePendingBindings();
    }

    @Override
    public void onRefresh() {
        if (newsType != null) {
            setData(newsType);
            newsListBinding.swipeNews.setRefreshing(false);
        }
    }
}
