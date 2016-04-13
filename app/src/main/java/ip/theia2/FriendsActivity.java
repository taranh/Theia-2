package ip.theia2;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsActivity extends ListFragment{

    private ArrayList<String> friends = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    // This makes the settings_page.xml page show up.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, friends);
        setListAdapter(adapter);

        addFriend(TestFriends.albert);
        addFriend(TestFriends.frida);
        addFriend(TestFriends.orlando);

        return inflater.inflate(R.layout.friends_page, container, false);
    }

    private void addFriend(User user){
        friends.add(user.getName() + " " + user.getLatLng().toString());
        adapter.notifyDataSetChanged();
    }



}
