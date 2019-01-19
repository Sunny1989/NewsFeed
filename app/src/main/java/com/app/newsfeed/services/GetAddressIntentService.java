package com.app.newsfeed.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.app.newsfeed.utility.Constants;

import java.util.List;
import java.util.Locale;

public class GetAddressIntentService extends IntentService {

    private static final String IDENTIFIER = "GetAddressIntentService";
    private ResultReceiver addressResultReceiver;

    public GetAddressIntentService() {
        super(IDENTIFIER);
    }

    //handle the address request
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //get result receiver from intent
        addressResultReceiver = intent.getParcelableExtra(Constants.ADD_RECEIVER);

        if (addressResultReceiver == null) {
            Log.e("GetAddressIntentService", "No receiver, not processing the request further");
            return;
        }

        Location location = intent.getParcelableExtra(Constants.LOCATION_OBJ);

        //send no location error to results receiver
        if (location == null) {
            //msg = "No location, can't go further without location";
            sendResultsToReceiver(0, null);
            return;
        }
        //call GeoCoder getFromLocation to get address
        //returns list of addresses, take first one and send info to result receiver
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (Exception ioException) {
            Log.e("", "Error in getting address for the location");
        }

        if (addresses == null || addresses.size() == 0) {
            //msg = "No address found for the location";
            sendResultsToReceiver(1, null);
        } else {
            Address address = addresses.get(0);
            sendResultsToReceiver(2, address);
        }
    }

    //to send results to receiver in the source activity
    private void sendResultsToReceiver(int resultCode, Address address) {
        Bundle bundle = new Bundle();
        //bundle.putString("address_result", message);
        bundle.putParcelable(Constants.ADDRESS_OBJ, address);
        addressResultReceiver.send(resultCode, bundle);
    }
}
