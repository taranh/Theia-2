package ip.theia2.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import java.util.Stack;

import ip.theia2.R;
import ip.theia2.TestFriends;
import ip.theia2.User;

/**
 * TODO: Add server-sent locations to maps, must coincide with user's location request interval.
 * TODO: Do stuff for when user is unable to connect to api.
 */

public class MapActivity extends Fragment
        implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mUserLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.maps_page, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(isLocationEnabled()) {
            setupMap();
        }
        else {
            createLocationAlertDialog(this.getActivity());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        // testing

        LatLng testFriend = new LatLng(51.380928, -2.360182);
        //addMarker("hey its me ur brother", testFriend);

        String testFriend2 = "51.379748&&-2.330712";
        Stack<User> userStack = new Stack<>();
        userStack.push(TestFriends.albert);
        userStack.push(TestFriends.frida);
        userStack.push(TestFriends.orlando);
        userStack.push(new User("Erik Uberti", parseLatLngString(testFriend2)));

        for (int i = 0; i <= userStack.size(); i++) {
            addMarker(userStack.pop(), BitmapDescriptorFactory.HUE_GREEN);
        }
    }

    private synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            // Add a marker to the map to show user's location.
            if (mLastLocation != null) {
                mUserLocation = new LatLng(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude());
                addMarker(new User("You", mUserLocation),
                        BitmapDescriptorFactory.HUE_BLUE);
            }

            // Move the camera to user's location.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(mUserLocation).zoom(13).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // Check for new user's location.
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000); // 10 seconds
            mLocationRequest.setFastestInterval(5000); // 5 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            System.err.println("Permission Error");
        }
    }

    @Override
    public void onConnectionSuspended(int i){
        System.err.println("Connection Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
        System.err.println("Connection Failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO: Clear map when locchange is received.
        //mMap.clear();
        addMarker(new User("You", new LatLng(location.getLatitude(), location.getLongitude())),
                BitmapDescriptorFactory.HUE_BLUE);
    }

    // Adds a marker on the map, refer to hue colour wheel for customising colours.
    private void addMarker(User user, float hue) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(user.getLatLng());
        markerOptions.title(user.getName());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(hue));
        mMap.addMarker(markerOptions);
    }

    // Takes in a string "latitude&&longitude" and converts into a LatLng object.
    private LatLng parseLatLngString(String string) {
        return new LatLng(Double.parseDouble(string.split("&&")[0]),
                Double.parseDouble(string.split("&&")[1]));
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_theia);
        mapFragment.getMapAsync(this);
    }

    /**
     * Checks if locations services are enabled.
     */
    private boolean isLocationEnabled(){
        // http://stackoverflow.com/questions/10311834/how-to-check-if-location-services-are-enabled
        int locationMode = 0;
        String locationProviders;

        // Checks if using the old or new Android Location API.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(this.getActivity().getContentResolver(),
                        Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                System.err.println(e.getMessage());
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }
        else {
            locationProviders = Settings.Secure.getString(this.getActivity().getContentResolver(),
                    Settings.Secure.LOCATION_MODE);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * Create a dialog to warn the user that location services are not enabled.
     */
    private void createLocationAlertDialog(final Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(context.getResources().getString(R.string.location_services_not_enabled));
        dialog.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Go to location settings on device.
                Intent intent = new Intent (Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);

                // Checks if location services are enabled.
                if(isLocationEnabled()){
                    setupMap();
                }
                else{
                    createLocationAlertDialog(context);
                }
            }
        });
        dialog.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Go back to MainActivity
                Intent intent = new Intent(MapActivity.this.getActivity(), MainActivity.class);
                context.startActivity(intent);
            }
        });
        dialog.show();
    }
}
