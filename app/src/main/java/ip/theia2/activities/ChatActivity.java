package ip.theia2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ip.theia2.R;

/**
 * Created by Laptop on 11/03/2016.
 */
public class ChatActivity extends Fragment {

    public ChatActivity(){

    }

    // this makes the settings_page.xml page show up.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout page = (LinearLayout) inflater.inflate(R.layout.chat_page, container, false);

        return page;
    }

}
