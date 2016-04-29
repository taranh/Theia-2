package ip.theia2.activities;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ip.theia2.R;
import ip.theia2.TestFriends;
import ip.theia2.User;

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

        return inflater.inflate(R.layout.friends_page, container, false);
    }

    private void addFriend(User user){
        friends.add(user.getName());
        adapter.notifyDataSetChanged();
    }
}
