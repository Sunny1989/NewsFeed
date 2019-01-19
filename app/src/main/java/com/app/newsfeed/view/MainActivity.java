package com.app.newsfeed.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ExpandableListView;

import com.app.newsfeed.BaseActivity;
import com.app.newsfeed.R;
import com.app.newsfeed.adapter.CustomExpandableListAdapter;
import com.app.newsfeed.databinding.MainActivityBinding;
import com.app.newsfeed.listener.OnCompleteListener;
import com.app.newsfeed.utility.Constants;
import com.app.newsfeed.utility.Utils;
import com.app.newsfeed.view.fragments.NewsFragment;
import com.app.newsfeed.view.fragments.ProfileFragment;
import com.app.newsfeed.view.fragments.WeatherFragment;
import com.app.newsfeed.viewmodel.MainViewModel;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity implements OnCompleteListener {

    private MainActivityBinding mainActivityBinding;
    private List<String> mExpandableTitleList;
    private HashMap<String, List<String>> mExpandableListData;
    private int selectedFragment = -1;
    //private NewsFragment newsFragment;
    private WeatherFragment weatherFragment;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainActivityBinding.appBar.toolbar.setTitle(R.string.news);
        setSupportActionBar(mainActivityBinding.appBar.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainActivityBinding.drawerLayout, mainActivityBinding.appBar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainActivityBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        MainViewModel mainViewModel = new MainViewModel();
        //First preparing data..
        mainViewModel.prepareListData(mExpandableTitleList, mExpandableListData, this);
    }

    @Override
    public void onBackPressed() {
        if (mainActivityBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainActivityBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onComplete(final List<String> expandableTitleList, final HashMap<String, List<String>> expandableListData) {
        //Populating the data, when data is fetched.
        CustomExpandableListAdapter expandableListAdapter = new CustomExpandableListAdapter(this, expandableTitleList, expandableListData);
        mainActivityBinding.elvNews.setAdapter(expandableListAdapter);

        mainActivityBinding.elvNews.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (selectedFragment != groupPosition) {
                    /*if (groupPosition != Constants.NEWS) {
                        parent.collapseGroup(Constants.NEWS);
                    }*/
                    changeFragment(groupPosition, null);
                    mainActivityBinding.drawerLayout.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });

        mainActivityBinding.elvNews.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (expandableListData.get(expandableTitleList.get(groupPosition)) != null) {
                    if (groupPosition == Constants.NEWS) {
                        changeFragment(Constants.NEWS, expandableListData.get(expandableTitleList.get(groupPosition)).get(childPosition));
                        mainActivityBinding.drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
                return false;
            }
        });

        //Initial Loading of data with Latest news
        changeFragment(Constants.NEWS, Constants.LATEST_LBL);
        mainActivityBinding.elvNews.expandGroup(Constants.NEWS);
    }

    /**
     * This method will change the fragment.
     */
    private void changeFragment(@Constants.NavigationType int type, String newsType) {
        selectedFragment = type;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (type == Constants.NEWS) {
            mainActivityBinding.appBar.toolbar.setTitle(R.string.news);
            NewsFragment newsFragment = new NewsFragment();
            Bundle bundle = new Bundle();
            newsFragment.setArguments(bundle);
            if (newsType != null) {
                bundle.putString(Constants.NEWS_TYPE, newsType);
            } else {
                bundle.putString(Constants.NEWS_TYPE, Constants.LATEST_LBL);
            }
            fragmentTransaction.replace(mainActivityBinding.appBar.contentMain.linContainer.getId(), newsFragment, Constants.NEWS_LBL);
        } else if (type == Constants.PROFILE) {
            mainActivityBinding.appBar.toolbar.setTitle(R.string.profile);
            fragmentTransaction.replace(mainActivityBinding.appBar.contentMain.linContainer.getId(), new ProfileFragment(), Constants.PROFILE_LBL);
        } else if (type == Constants.WEATHER) {
            mainActivityBinding.appBar.toolbar.setTitle(R.string.weather);
            if (weatherFragment == null) {
                weatherFragment = new WeatherFragment();
            }
            fragmentTransaction.replace(mainActivityBinding.appBar.contentMain.linContainer.getId(), weatherFragment, Constants.WEATHER_LBL);
        }
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (weatherFragment != null) {
                        weatherFragment.getCurrentLocation(MainActivity.this);
                    }
                } else {
                    String permission = permissions[0];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        boolean showRationale = Utils.shouldShowRequestPermissionRationale(MainActivity.this, permission);

                        if (!showRationale) {
                            //showRationale(permission);
                            //User has denied with Never Ask Again flag!
                            DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    //REQUEST_PERMISSION_SETTING
                                    startActivityForResult(intent, 101);
                                }
                            };
                            String permissionFor = null;
                            if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                permissionFor = getString(R.string.location);
                            }

                            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle(R.string.enable_permissions)
                                    .setMessage(String.format(getString(R.string.you_have_denied), permissionFor)).setPositiveButton(R.string.go_to_settings, okClickListener).setNegativeButton(getString(R.string.cancel), null).create();
                            dialog.show();

                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.LOCATION_GPS_REQUEST_CODE) {
            if (weatherFragment != null) {
                weatherFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
