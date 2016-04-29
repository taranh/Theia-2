package ip.theia2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ip.theia2.R;
import ip.theia2.exceptions.TheiaLoginException;
import ip.theia2.networking.ServerHandler;
import ip.theia2.networking.interfaces.ChatHandler;

/**
 * Created by Laptop on 11/03/2016.
 * Modified by Zach 29/04/2016
 */
public class ChatActivity extends Fragment implements ChatHandler{

    private Spinner spinner;

    private View chatV;

    public ChatActivity(){

    }

    /**
     * On creation of this view.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatV = inflater.inflate(R.layout.chat_page, container, false);

        spinner = (Spinner) chatV.findViewById(R.id.friendSpinner);


        //get the list of friends from the server
        ServerHandler sh = ServerHandler.getInstance();
        ArrayList<String[]> friends = sh.getFriendList();

        List<String> friendsList = new ArrayList<String>();

        //Copy one to the other.

        for(int i = 0; i < friends.size(); i++){
            friendsList.add(friends.get(i)[0]);
        }

        //Create an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, friendsList);

        spinner.setAdapter(adapter);

        //Setup the send button.
        Button sendButton = (Button) chatV.findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get the spinner.
                Spinner theSpinner = (Spinner) chatV.findViewById(R.id.friendSpinner);

                EditText text = (EditText) chatV.findViewById(R.id.editText);

                final ServerHandler sh = ServerHandler.getInstance();

                final String dest = theSpinner.getSelectedItem().toString();

                final String msg = text.getText().toString();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        TextView tv = (TextView) chatV.findViewById(R.id.chatOut);
                        tv.setText("You said: " + msg);
                    }
                });

                (new Thread() {
                    public void run() {
                        System.out.println("Sending message");
                        sh.sendMessage("chat " + dest + " " + msg);
                    }
                }).start();
            }
        });

        //Setup the chat handler.
        sh.addChatHandler(this);

        return chatV;

    }

    /**
     * Handles chat messages.
     * @param source
     * @param msg
     */
    public void chatMessage(final String source, final String msg){

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                TextView tv = (TextView) chatV.findViewById(R.id.chatOut);
                tv.setText(tv.getText().toString() + "\n" + source + " says: " + msg);
            }
        });

    }



}
