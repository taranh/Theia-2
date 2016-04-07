package ip.theia2;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kai on 07/04/2016.
 */
public class User {

    public final String name;
    public final LatLng location;

    public User(String name, LatLng location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return location;
    }
}
