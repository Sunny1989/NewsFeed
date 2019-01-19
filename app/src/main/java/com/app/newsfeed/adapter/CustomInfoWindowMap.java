package com.app.newsfeed.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.app.newsfeed.R;
import com.app.newsfeed.databinding.InfoWindowBinding;
import com.app.newsfeed.model.pojo.InfoWindowPojo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowMap implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public CustomInfoWindowMap(Context ctx) {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        InfoWindowBinding infoWindowBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.layout_info_window_map, null, false);
        InfoWindowPojo infoWindowPojo = (InfoWindowPojo) marker.getTag();
        if (infoWindowPojo != null) {
            infoWindowBinding.name.setText(infoWindowPojo.name);
            infoWindowBinding.tvDescription.setText("Clouds: " + infoWindowPojo.description);
            infoWindowBinding.tvTemp.setText("Temperature: " + infoWindowPojo.temperature);
            infoWindowBinding.tvWind.setText("Winds: " + infoWindowPojo.wind);
        }
        return infoWindowBinding.getRoot();
    }
}
