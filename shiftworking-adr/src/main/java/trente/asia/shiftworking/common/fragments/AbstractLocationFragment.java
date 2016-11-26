package trente.asia.shiftworking.common.fragments;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import trente.asia.android.util.AndroidUtil;

public abstract class AbstractLocationFragment extends AbstractSwFragment implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

	private final int REQUEST_CHECK_SETTINGS = 31;

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		default:
			break;
		}
	}

	protected void getLocation(){
        if(AndroidUtil.verifyLocationPermissions(activity)){
            // check location setting
            GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(activity).addApi(LocationServices.API).addConnectionCallbacks(AbstractLocationFragment.this).addOnConnectionFailedListener(AbstractLocationFragment.this).build();
            mGoogleApiClient.connect();

            LocationRequest locationRequestHighAccuracy = LocationRequest.create();
            locationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequestHighAccuracy.setInterval(30 * 1000);
            locationRequestHighAccuracy.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequestHighAccuracy);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                @Override
                public void onResult(LocationSettingsResult result){
                    final Status status = result.getStatus();
                    // final
                    // LocationSettingsStates
                    // =
                    // result.getLocationSettingsStates();
                    switch(status.getStatusCode()){
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied.
                            successLocation();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed
                            // by showing the user a dialog.
                            try{
                                // Show the dialog by calling startResolutionForResult(), and check
                                // the result in onActivityResult().
                                status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                            }catch(IntentSender.SendIntentException e){
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied.
                            // However, we have no way to fix the settings so
                            // we won't show the dialog.
                            break;
                    }
                }
            });
        }
	}

	protected abstract void successLocation();

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent){
		super.onActivityResult(requestCode, resultCode, returnedIntent);
		if(resultCode != Activity.RESULT_OK) return;
		switch(requestCode){
		case REQUEST_CHECK_SETTINGS:
			successLocation();
			break;

		default:
			break;
		}
	}

	@Override
	public void onConnected(@Nullable Bundle bundle){

	}

	@Override
	public void onConnectionSuspended(int i){

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult){

	}
}
