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
 * This class consists of methods that handles the creation of the "Friends" fragment to handle
 * and display the user's friends in the app.
 *
 * @author Kai Diep
 * @author Zachary Shannon
 */
public class FriendsActivity extends ListFragment {

    // Stores all of the user's friends.
    private ArrayList<String> friends = new ArrayList<>();
    // Displays friends on the ListView object.
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, friends);
        setListAdapter(adapter);

        // DEBUG
        addFriend(TestFriends.albert);
        addFriend(TestFriends.frida);
        addFriend(TestFriends.orlando);

        return inflater.inflate(R.layout.friends_page, container, false);
    }

    /**
     * Adds a user to the ListView displaying name.
     *
     * @param user the user to be displayed.
     */
    private void addFriend(User user){
        friends.add(user.getName());
        adapter.notifyDataSetChanged();
    }
}
