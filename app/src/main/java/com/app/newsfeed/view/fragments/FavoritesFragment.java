package com.app.newsfeed.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.newsfeed.R;
import com.app.newsfeed.databinding.FavoriteFragmentBinding;
import com.app.newsfeed.viewmodel.FavoritesVM;

public class FavoritesFragment extends Fragment {

    private FavoriteFragmentBinding favoriteFragmentBinding;
    private FavoritesVM favoritesVM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        favoriteFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false);
        favoritesVM = new FavoritesVM(getActivity());
        favoritesVM.loadData();
        favoriteFragmentBinding.setFavoritesVM(favoritesVM);
        return favoriteFragmentBinding.getRoot();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && favoritesVM != null){
            favoritesVM.loadData();
            favoriteFragmentBinding.setFavoritesVM(favoritesVM);
        }
    }
}
