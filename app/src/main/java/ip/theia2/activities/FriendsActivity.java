package ip.theia2.activities;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import ip.theia2.R;
import ip.theia2.TestFriends;
import ip.theia2.User;
import ip.theia2.networking.ServerHandler;

/**
 * Fragment for the "Friends" page to handle friends.
 */
public class FriendsActivity extends ListFragment{

    private ArrayList<String> friends = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, friends);
        setListAdapter(adapter);

        // Adding test friends onto the list.

        ServerHandler sh = ServerHandler.getInstance();
        ArrayList<String[]> friends = sh.getFriendList();

        for(int i = 0; i < friends.size(); i++){
            addFriend(new User(friends.get(i)[0], parseLatLngString(friends.get(i)[1])));
        }

        return inflater.inflate(R.layout.friends_page, container, false);
    }

    private void addFriend(User user){
        friends.add(user.getName());
        adapter.notifyDataSetChanged();
    }

    // Takes in a string "latitude&&longitude" and converts into a LatLng object.
    private LatLng parseLatLngString(String string) {
        return new LatLng(Double.parseDouble(string.split("&&")[0]),
                Double.parseDouble(string.split("&&")[1]));
    }
}
