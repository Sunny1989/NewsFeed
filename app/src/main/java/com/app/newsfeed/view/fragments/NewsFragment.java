package com.app.newsfeed.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.newsfeed.R;
import com.app.newsfeed.adapter.CustomFragmentAdapter;
import com.app.newsfeed.databinding.NewsFragmentBinding;
import com.app.newsfeed.utility.Constants;

public class NewsFragment extends Fragment implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    private NewsFragmentBinding newsBinding;
    private CustomFragmentAdapter customFragmentAdapter;
    private NewsListFragment newsListFragment;
    private String newsType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        newsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);

        customFragmentAdapter = new CustomFragmentAdapter(getChildFragmentManager());

        if (newsListFragment == null) {
            newsListFragment = new NewsListFragment();
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            newsType = bundle.getString(Constants.NEWS_TYPE);
            newsListFragment.setArguments(bundle);
        }

        customFragmentAdapter.addFragment(newsListFragment, getString(R.string.news));
        customFragmentAdapter.addFragment(new FavoritesFragment(), getString(R.string.favorites));

        newsBinding.vpNews.setAdapter(customFragmentAdapter);
        newsBinding.vpNews.addOnPageChangeListener(this);
        newsBinding.tabNews.addOnTabSelectedListener(this);

        newsBinding.tabNews.setupWithViewPager(newsBinding.vpNews);

        changeTab(newsType);

        return newsBinding.getRoot();
    }

    public void changeTab(String newsType) {
        if (newsType.equalsIgnoreCase(Constants.FAVORITES_LBL)) {
            newsBinding.tabNews.getTabAt(1).select();
        } else {
            newsBinding.tabNews.getTabAt(0).select();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
