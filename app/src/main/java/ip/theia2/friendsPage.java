package ip.theia2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Laptop on 11/03/2016.
 */
public class friendsPage extends Fragment {

    public friendsPage(){
        // constructor
    }

    // this makes the settings_page.xml page show up.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends_page, container, false);

        return rootView;
    }

}
