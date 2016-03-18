package ip.theia2;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Laptop on 11/03/2016.
 */
public class settingsPage extends Fragment {

    public settingsPage(){
        // constructor
    }

    // this makes the settings_page.xml page
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_page, container, false);

        return rootView;
    }

}
