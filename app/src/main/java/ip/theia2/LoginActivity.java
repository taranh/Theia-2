package ip.theia2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;

/**
 * Created by Laptop on 11/03/2016.
 */
public class LoginActivity extends Activity implements NetworkMessageHandler{

    public NetworkConnection conn;

    boolean loginState = false;
    EditText editTextUser,editTextPass;
    String serverReply;

    /**
     * Implements the handle message interface.
     * @param msg message to handle.
     * @return success or fail
     */
    public Boolean handleMessage(final String msg){
        //Modifying the UI, so has to be run on the UI thread...
        //From:
        //http://stackoverflow.com/questions/5161951/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi

        //Server reply
        serverReply = msg;

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        createTextFields();
        createButtonClicked();
    }

    private void createTextFields(){
        editTextUser = (EditText) findViewById(R.id.editTextsUser);
        editTextUser.setOnFocusChangeListener(new mOnFocusChangeListener());

        editTextPass = (EditText) findViewById(R.id.editTextPass);
        editTextPass.setTransformationMethod(new DotsPasswordTransformationMethod());
        editTextPass.setOnFocusChangeListener(new mOnFocusChangeListener());

    }

    private void createButtonClicked() {

        final NetworkMessageHandler nmh = this;

        Button createButton = (Button) findViewById(R.id.buttonCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
        final String inUser = editTextUser.getText().toString();
        final String inPass = editTextPass.getText().toString();
        Button logonButton = (Button) findViewById(R.id.buttonSignIn);
        logonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inUser.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter your username",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else if (inPass.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter your password",
                            Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    boolean canLogin = false;
                    // TODO - check if the credentials are correct.

                    /*final InputStream trustStore = getResources().openRawResource(R.raw.truststore);

                    (new Thread(){
                        public void run(){
                            //This seriously needs error handling.
                            conn = new NetworkConnection("138.38.109.121", 5575, trustStore, nmh); //We are always using 5571.

//                            try {
//                                Thread.sleep(100);
//                            }
//                            catch(InterruptedException e){
//                                //No one cares
//                            }

                            //Speaking to the server
                            conn.sendMessage("login" + inUser + inPass);
                        }
                    }).start();*/


                    /*// change canLogin according to the
                    if (serverReply.equals("acceptlog")){
                        canLogin = true;
                    } else {
                        canLogin = false;
                    }*/

                    canLogin = true;
                    if (canLogin) {
                        loginState = true;
                        if (loginState) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Login failed",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
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
