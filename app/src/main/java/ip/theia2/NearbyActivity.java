package ip.theia2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Fragment for the "Who's Nearby" page to display nearby friends.
 * TODO: Add client-server functions
 */
public class NearbyActivity extends ListFragment implements LocationListener{

    private ArrayList<String> friends = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Location userLocation;
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, friends);
        setListAdapter(adapter);

        // Get user's current location
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        //Test
        LatLng somewhereNear = new LatLng(51.379013, -2.393116);    // Change this to somewhere near you!
        User nearbyFriend = new User("Your Nearby Friend", somewhereNear);

        addFriend(nearbyFriend);
        addFriend(TestFriends.valerie); // Should not show up in the list.

        return inflater.inflate(R.layout.nearby_page, container, false);
    }

    @Override
    public void onLocationChanged(Location location) {
        userLocation = location;
    }

    /**
     * Add a nearby friend to the ListView.
     */
    private void addFriend(User user) {
        if(isNearby(user)) {
            friends.add(user.getName() + " - " + getDistance(user).intValue() + "m");
            adapter.notifyDataSetChanged();
        }
    }
    /**
     *  Checks if a friend is near the user within a 1km radius using the Haversine formula.
     */
    private boolean isNearby(User user) {
        double threshold = 1000d;

        if(threshold >= getDistance(user)){
            return true;
        }
        else{
            return false;
        }
    }

    private Double getDistance(User friend){
        double R = 6371000d;        // Earth's mean radius in metres.

        double phi1 = Math.toRadians(userLocation.getLatitude());
        double phi2 = Math.toRadians(friend.getLatLng().latitude);

        double dPhi =  Math.toRadians(friend.getLatLng().latitude -
                userLocation.getLatitude());       // Difference between two latitudes.
        double dLambda = Math.toRadians(friend.getLatLng().longitude -
               userLocation.getLongitude());       // Difference between two longitudes

        // Haversine formula
        double a = Math.sin(dPhi/2) * Math.sin(dPhi/2) + Math.cos(phi1)
                * Math.cos(phi2) * Math.sin(dLambda / 2) * Math.sin(dLambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /*
    // Takes in a string "latitude&&longitude" and converts into a LatLng object.
    private LatLng parseLatLngString(String string) {
        return new LatLng(Double.parseDouble(string.split("&&")[0]),
                Double.parseDouble(string.split("&&")[1]));
    }
    */


}
