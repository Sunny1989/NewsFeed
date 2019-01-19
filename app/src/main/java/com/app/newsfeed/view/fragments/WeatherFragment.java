package com.app.newsfeed.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.newsfeed.R;
import com.app.newsfeed.adapter.CustomInfoWindowMap;
import com.app.newsfeed.listener.OnCompleteListenerWithData;
import com.app.newsfeed.model.pojo.GroupWeatherPojo;
import com.app.newsfeed.model.pojo.InfoWindowPojo;
import com.app.newsfeed.model.pojo.WeatherDataPojo;
import com.app.newsfeed.services.GetAddressIntentService;
import com.app.newsfeed.utility.Constants;
import com.app.newsfeed.viewmodel.WeatherVm;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class WeatherFragment extends Fragment implements OnMapReadyCallback, OnCompleteListenerWithData {

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    //private Location currentLocation;
    private LocationAddressResultReceiver addressResultReceiver;
    private LocationRequest mLocationRequest;
    private Location currentLocation;
    private LocationCallback locationCallback;
    private WeatherVm weatherVm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressResultReceiver = new LocationAddressResultReceiver(new Handler());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        weatherVm = new WeatherVm(getActivity(), this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLocation = locationResult.getLocations().get(0);
                if (currentLocation != null) {
                    mFusedLocationClient.removeLocationUpdates(locationCallback);
                }
                getAddress(currentLocation);
            }
        };
        getCurrentLocation(getActivity());
        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Displaying India on Map by default.
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(BOUNDS_INDIA, 0));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //Setting view for info-window.
        CustomInfoWindowMap customInfoWindow = new CustomInfoWindowMap(getActivity());
        mMap.setInfoWindowAdapter(customInfoWindow);
        //Fetching record for other cities.
        weatherVm.getMultipleCitiesWeather();
    }

    /**
     * This method will take the current location.
     *
     * @param context
     */
    public void getCurrentLocation(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Request for Location Permission!
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            //If app has location permission.

            //getLastLocation does not return the location.
            /*mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        getAddress(location);
                    }
                }
            });*/

            //So tried with Location update.
            final LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(2000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


            //Adding location settings, to display enable location pop-up also!
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            SettingsClient client = LocationServices.getSettingsClient(getActivity());
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

            task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    // All location settings are satisfied. The client can initialize
                    // location requests here.
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                }
            });

            task.addOnFailureListener(getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(getActivity(), Constants.LOCATION_GPS_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.LOCATION_GPS_REQUEST_CODE && resultCode == 0) {
            //No thanks, option is selected then we should stop the update!
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        } else if (requestCode == Constants.LOCATION_GPS_REQUEST_CODE && resultCode == -1) {
            //Ok and it will enable the location GPS and thus we will get the location.
            getCurrentLocation(getActivity());
        }
    }

    /**
     * This method will get the address of the given location.
     *
     * @param location
     */
    private void getAddress(Location location) {
        if (!Geocoder.isPresent()) {
            Toast.makeText(getActivity(), "Can't find current address, ", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getActivity(), GetAddressIntentService.class);
        intent.putExtra(Constants.ADD_RECEIVER, addressResultReceiver);
        intent.putExtra(Constants.LOCATION_OBJ, location);
        getActivity().startService(intent);
    }

    @Override
    public void onRequestSuccess(Object object) {
        if (object instanceof WeatherDataPojo) {
            //Single city!
            WeatherDataPojo weatherDataPojo = (WeatherDataPojo) object;
            LatLng city = new LatLng(Double.parseDouble(weatherDataPojo.coord.lat), Double.parseDouble(weatherDataPojo.coord.lon));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(city);
            markerOptions.title(weatherDataPojo.name);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            Marker cityMarker = mMap.addMarker(markerOptions);
            InfoWindowPojo infoWindowPojo = new InfoWindowPojo(weatherDataPojo.name, weatherDataPojo.weather.get(0).description, weatherDataPojo.main.temp, weatherDataPojo.wind.speed);
            cityMarker.setTag(infoWindowPojo);
        } else if (object instanceof GroupWeatherPojo) {
            //Group weather!
            GroupWeatherPojo groupWeatherPojo = (GroupWeatherPojo) object;
            int count = groupWeatherPojo.cnt;

            if (count > 0) {
                //Then we should set up markers in map!
                float mapColor = 0;
                for (WeatherDataPojo weatherDataPojo : groupWeatherPojo.list) {
                    LatLng city = new LatLng(Double.parseDouble(weatherDataPojo.coord.lat), Double.parseDouble(weatherDataPojo.coord.lon));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(city);
                    markerOptions.title(weatherDataPojo.name);
                    //Marker color to keep changing!
                    if (mapColor >= BitmapDescriptorFactory.HUE_ROSE) {
                        mapColor = 0;
                    } else {
                        mapColor = mapColor + 30;
                    }
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(mapColor));
                    Marker cityMarker = mMap.addMarker(markerOptions);
                    InfoWindowPojo infoWindowPojo = new InfoWindowPojo(weatherDataPojo.name, weatherDataPojo.weather.get(0).description, weatherDataPojo.main.temp, weatherDataPojo.wind.speed);
                    cityMarker.setTag(infoWindowPojo);
                }

            }
        }
    }

    private class LocationAddressResultReceiver extends ResultReceiver {
        @SuppressLint("RestrictedApi")
        LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == 0) {
                Log.d("Address", "Location null retrying");
                getCurrentLocation(getActivity());
            } else if (resultCode == 1) {
                Toast.makeText(getActivity(), getString(R.string.current_address_not_found), Toast.LENGTH_SHORT).show();
            } else if (resultCode == 2) {
                //Getting address.
                Address address = resultData.getParcelable(Constants.ADDRESS_OBJ);
                //City
                //It will return county!
                String city = address.getSubAdminArea();
                if (city != null) {
                    Toast.makeText(getActivity(), getString(R.string.your_current_city) + city, Toast.LENGTH_SHORT).show();
                }

                if (weatherVm != null) {
                    weatherVm.getCurrentCityWeather(address.getLatitude(), address.getLongitude());
                }

            }
        }
    }

}




