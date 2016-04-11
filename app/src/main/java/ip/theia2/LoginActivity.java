package ip.theia2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Laptop on 11/03/2016.
 */
public class LoginActivity extends Activity{

    boolean loginState = false;
    EditText editTextUser,editTextPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        createTextFields();
        createButtonClicked();
    }

    private void createTextFields(){
        editTextUser = (EditText) findViewById(R.id.editTextUser);
        editTextUser.setOnFocusChangeListener(new mOnFocusChangeListener());

        editTextPass = (EditText) findViewById(R.id.editTextPass);
        editTextPass.setTransformationMethod(new DotsPasswordTransformationMethod());
        editTextPass.setOnFocusChangeListener(new mOnFocusChangeListener());

    }

    private void createButtonClicked() {
        Button createButton = (Button) findViewById(R.id.buttonCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
        Button logonButton = (Button) findViewById(R.id.buttonSignIn);
        logonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextUser.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter your username",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else if (editTextPass.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter your password",
                            Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    boolean canLogin = false;
                    // TODO - check if the credentials are correct.
                    // change canLogin accordingly
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
