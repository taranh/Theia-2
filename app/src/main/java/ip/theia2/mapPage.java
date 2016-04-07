package ip.theia2;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

/**
 * TODO: Add server-sent locations to maps, must coincide with user's location request interval.
 * TODO: Show rationale for when location permission has not been granted.
 * TODO: Do stuff for when user is unable to connect to api.
 */

public class mapPage extends Fragment
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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_theia);
        mapFragment.getMapAsync(this);
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
        userStack.push(new User("hey its me ur brother", testFriend));
        userStack.push(new User("Your Management friend", parseLatLngString(testFriend2)));

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
        System.out.println("Connected");

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
}
