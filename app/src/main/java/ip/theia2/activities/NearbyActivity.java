package ip.theia2.activities;

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
import java.util.List;

import ip.theia2.R;
import ip.theia2.TestFriends;
import ip.theia2.User;

/**
 * This class consists of methods for the "Who's Nearby" fragment which displays nearby friends in
 * a certain threshold (in metres) implementing the LocationListener interface. This class uses the
 * Haversine formula to calculate distances using latitude and longitude between two users.
 * <p>
 * The Haversine formula calculates the great-circle distance between two points - that is, the
 * shortest distance over the Earth's surface.
 *
 * TODO: Add client-server functions.
 *
 * @author Kai Diep
 * @author Zachary Shannon
 * @see    <a href="http://www.movable-type.co.uk/scripts/latlong.html">Haversine Formula</a>
 */
public class NearbyActivity extends ListFragment implements LocationListener{
    // List to store friends.
    private ArrayList<String> friends = new ArrayList<>();
    // Displays friends on the ListView object.
    private ArrayAdapter<String> adapter;
    // User's current location.
    private Location userLocation;
    // Gets user's current location.
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set up ListView list adapter.
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, friends);
        setListAdapter(adapter);

        // Get user's current location.
        locationManager = (LocationManager) this.getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            userLocation = getLastKnownLocation();
        }
        else {
            System.err.println("Permission Error");
        }

        // DEBUG
        // Change this to somewhere near you!
        LatLng somewhereNear = new LatLng(51.379013, -2.393116);
        User nearbyFriend = new User("Your Nearby Friend", somewhereNear);
        addFriend(nearbyFriend);
        // Should not show up in the list.
        addFriend(TestFriends.valerie);

        return inflater.inflate(R.layout.nearby_page, container, false);
    }

    @Override
    public void onLocationChanged(Location location) {
        userLocation = location;
    }

    /**
     * Adds a user to the ListView displaying name and closeness (in metres).
     */
    private void addFriend(User user) {
        if(isNearby(user)) {
            friends.add(user.getName() + " - " + getDistance(user).intValue() + "m");
            adapter.notifyDataSetChanged();
        }
    }
    /**
     *  Checks if a friend is within threshold distance to the user.
     */
    private boolean isNearby(User user) {
        Double threshold = 1000d;   // 1 km

        return threshold >= getDistance(user);
    }

    /**
     * Calculates the distance between the user and friend using the Haversine formula.
     *
     * @param friend the user's friend.
     * @return the distance (in metres) between the user and friend.
     */
    private Double getDistance(User friend){
        double R = 6371000d;        // Earth's mean radius in metres.

        // Formula parameters.
        double phi1 = Math.toRadians(userLocation.getLatitude());
        double phi2 = Math.toRadians(friend.getLatLng().latitude);

        // Difference between two latitudes.
        double dPhi =  Math.toRadians(friend.getLatLng().latitude -
                userLocation.getLatitude());

        // Difference between two longitudes
        double dLambda = Math.toRadians(friend.getLatLng().longitude -
               userLocation.getLongitude());

        // The formula.
        double a = Math.sin(dPhi/2) * Math.sin(dPhi/2) + Math.cos(phi1)
                * Math.cos(phi2) * Math.sin(dLambda / 2) * Math.sin(dLambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /**
     * Returns the user's current location by searching over all enabled providers.
     *
     * @return the user's location.
     * @see <a href="http://goo.gl/hgWoyJ">Finding user's location.</a>
     */
    private Location getLastKnownLocation() {
        //http://stackoverflow.com/questions/9873190/my-current-location-always-returns-null-how-can-i-fix-this
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        Location loc = null;

        for (String provider : providers) {
            try {
                loc = locationManager.getLastKnownLocation(provider);
            } catch (SecurityException e) {
                System.err.println("Permission Error");
            }

            if (loc == null) {
                continue;
            }
            if (bestLocation == null || loc.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = loc;
            }
        }

        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }

    /*
    private LatLng parseLatLngString(String string) {
        return new LatLng(Double.parseDouble(string.split("&&")[0]),
                Double.parseDouble(string.split("&&")[1]));
    }
    */


}
