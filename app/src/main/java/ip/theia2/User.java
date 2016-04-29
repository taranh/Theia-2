package ip.theia2;

import com.google.android.gms.maps.model.LatLng;

/**
 * This class consists of methods which handles user information.
 */
public class User {

    public final String name;
    public final LatLng location;

    /**
     * Constructor method.
     *
     * @param name the name of the user.
     * @param location current location of the user.
     */
    public User(String name, LatLng location) {
        this.name = name;
        this.location = location;
    }

    /**
     * @return name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * @return location of the user as LatLng object.
     */
    public LatLng getLatLng() {
        return location;
    }
}
