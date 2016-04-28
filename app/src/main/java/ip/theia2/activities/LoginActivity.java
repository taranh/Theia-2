package ip.theia2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;

import ip.theia2.DotsPasswordTransformationMethod;
import ip.theia2.R;
import ip.theia2.networking.ServerHandler;
import ip.theia2.exceptions.TheiaLoginException;
import ip.theia2.networking.interfaces.LoginHandler;

/**
 * Activity for the "Login" page to handle login.
 */
public class LoginActivity extends Activity implements LoginHandler{

    private EditText editTextUser, editTextPass;
    private String serverReply;

    private ServerHandler srv;


    @Override
    /**
     * Actions to carry out on creation
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        createTextFields();
        createButtonClicked();

        final InputStream trustStore = getResources().openRawResource(R.raw.truststore);
        final LoginHandler lh = this;

        //Create the server connection
        (new Thread(){
            public void run(){

                srv = ServerHandler.getInstance();

                //Add this connection.
                srv.startServerConnection("theiaserver.ddns.net", 5575, trustStore);

                //Add this login handler.
                srv.addLoginHandler(lh);
            }
        }).start();
    }

    /**
     * Create text fields
     */
    private void createTextFields(){
        editTextUser = (EditText) findViewById(R.id.editTextsUser);
        editTextUser.setOnFocusChangeListener(new mOnFocusChangeListener());

        editTextPass = (EditText) findViewById(R.id.editTextPass);
        editTextPass.setTransformationMethod(new DotsPasswordTransformationMethod());
        editTextPass.setOnFocusChangeListener(new mOnFocusChangeListener());

    }

    /**
     * Add button click listeners.
     */
    private void createButtonClicked() {

        /**
         * Go to the create page when the create button is pressed.
         */
        Button createButton = (Button) findViewById(R.id.buttonCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Login when the login button is pressed.
         */

        Button logonButton = (Button) findViewById(R.id.buttonSignIn);
        logonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String inUser = editTextUser.getText().toString();
                final String inPass = editTextPass.getText().toString();

                (new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            //No one cares
                        }

                        //Attempt a login.
                        try {
                            srv.login(inUser, inPass);
                        } catch (TheiaLoginException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Sent login");
                    }
                }).start();

                if (inUser.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter your username",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else if (inPass.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter your password",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    /**
     * In the case a successful login is recorded.
     */
    public void loginSuccess(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * In the case login credentials are rejected.
     */
    public void loginReject(){
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Wrong Username or Password", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    /**
     * In the case an unsuccessful login is recorded.
     */
    public void loginFail(){
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), "Login Failed",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class mOnFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        }
    }
}
