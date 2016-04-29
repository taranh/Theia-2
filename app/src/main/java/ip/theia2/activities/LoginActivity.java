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
import ip.theia2.networking.ServerMessageHandler;
import ip.theia2.exceptions.TheiaLoginException;
import ip.theia2.networking.interfaces.LoginHandler;

/**
 * This class consists of methods for the "Login" activity which handles login in the app
 * implementing the LoginHandler interface.
 *
 * @author  Taran Hackman
 * @author  Kai Diep
 * @author  Zachary Shannon
 * @see     LoginHandler
 * @see     DotsPasswordTransformationMethod
 */
public class LoginActivity extends Activity implements LoginHandler{

    private EditText editTextUser, editTextPass;
    private ServerMessageHandler srv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        createTextFields();
        createButtonClicked();

        final InputStream trustStore = getResources().openRawResource(R.raw.truststore);
        final LoginHandler lh = this;

        // Create the server connection.
        (new Thread(){
            public void run(){
                srv = ServerMessageHandler.getInstance();

                //Add this connection.
                srv.startServerConnection("theiaserver.ddns.net", 5575, trustStore);

                //Add this login handler.
                srv.addLoginHandler(lh);
            }
        }).start();
    }

    /**
     * Adds OnFocusChangeListeners for several EditText objects to ensure that the keyboard is
     * hidden whenever an EditText loses focus. The password is displayed as dots ensuring security
     * and protection of the user's login details.
     */
    private void createTextFields() {
        editTextUser = (EditText) findViewById(R.id.editTextsUser);
        editTextUser.setOnFocusChangeListener(new mOnFocusChangeListener());

        editTextPass = (EditText) findViewById(R.id.editTextPass);
        editTextPass.setTransformationMethod(new DotsPasswordTransformationMethod());
        editTextPass.setOnFocusChangeListener(new mOnFocusChangeListener());

    }

    /**
     * Adds OnClickListeners for each button. One button the launches the CreateActivity activity
     * for new accounts. The other button attempts to log into the server that verifies login
     * details.
     */
    private void createButtonClicked() {
        // Go to the create page when the create button is pressed.
        Button createButton = (Button) findViewById(R.id.buttonCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        // Login when the login button is pressed.
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
                        } catch (InterruptedException e){
                            e.printStackTrace();
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
    public void loginSuccess() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * In the case login credentials are rejected.
     */
    public void loginReject() {
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
    public void loginFail() {
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), "Login Failed",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    /**
     * Hides the keyboard whenever a View object loses focus in the app.
     *
     * @param view the View to hide the keyboard on.
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * This class implements the OnFocusChangeListener interface to hide the keyboard whenever the
     * focus of a View object is changed.
     */
    private class mOnFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        }
    }
}
