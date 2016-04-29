package ip.theia2;

import com.google.android.gms.maps.model.LatLng;

/**
 * Stores user information.
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

    //Return true if has a location.
    public Boolean hasLocation(){
        if(location != null){
            return false;
        }
        else{
            return true;
        }
    }

    public LatLng getLatLng() {
        return location;
    }
}
