package ip.theia2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by Laptop on 11/03/2016.
 */
public class SettingsActivity extends Fragment {

    EditText userText, courseText, dobText, placeText;
    Button chpassButton, okButton, delButton;
    RadioButton privRadButton, pubRadButton;

    public SettingsActivity(){
        // constructor
    }

    // this makes the settings_page.xml page
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout page = (LinearLayout) inflater.inflate(R.layout.settings_page, container, false);

        // Assign UI elements
        // TODO - fill fields with the current account settings
        userText = (EditText) page.findViewById(R.id.editTextsUser);
        // userText.setText();
        courseText = (EditText) page.findViewById(R.id.editTextsCourse);
        // courseText.setText();
        dobText = (EditText) page.findViewById(R.id.editTextsBirthday);
        // dobText.setText();
        placeText = (EditText) page.findViewById(R.id.editTextsPlace);
        // placeText.setText();
        chpassButton = (Button) page.findViewById(R.id.buttonsChPassword);
        privRadButton = (RadioButton) page.findViewById(R.id.radioButtonsPriv);
        pubRadButton = (RadioButton) page.findViewById(R.id.radioButtonsPub);
        /*if(public account){
            pubRadButton.setChecked(true);
        } else {
            privRadButton.setChecked(true);
        }*/
        okButton = (Button) page.findViewById(R.id.buttonsOK);
        delButton = (Button) page.findViewById(R.id.buttonsDel);

        addTextFunctionality();
        addRadioFunctionality();
        addButtonFunctionality();

        return page;
    }

    private void addTextFunctionality(){

    }
    private void addButtonFunctionality(){
        chpassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change password of account
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // saves settings to database
                // change database info to the field strings
                /*userText.getText().toString();
                courseText.getText().toString();
                dobText.getText().toString();
                placeText.getText().toString();*/
                Toast toast = Toast.makeText(getActivity(), "Settings Saved",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opens prompt
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete account")
                        .setMessage("Do you really want to delete your account?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // deletes profile from database
                                Toast.makeText(getActivity(), "Account deleted", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }
    private void addRadioFunctionality(){
        pubRadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(privRadButton.isChecked()){
                    privRadButton.setChecked(false);
                }
                pubRadButton.setChecked(true);
            }
        });
        privRadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(pubRadButton.isChecked()){
                    pubRadButton.setChecked(false);
                }
                privRadButton.setChecked(true);
            }
        });
    }

}
