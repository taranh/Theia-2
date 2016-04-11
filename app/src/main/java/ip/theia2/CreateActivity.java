package ip.theia2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Laptop on 11/03/2016.
 */
public class CreateActivity extends Activity {

    Button createButton;
    EditText usernameText, passwordText, courseText, favouriteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_page);

        createButtons();
        createEditText();
    }

    private void createEditText(){
        usernameText = (EditText) findViewById(R.id.editTextcUser);
        usernameText.setOnFocusChangeListener(new mOnFocusChangeListener());

        passwordText = (EditText) findViewById(R.id.editTextcPass);
        passwordText.setTransformationMethod(new DotsPasswordTransformationMethod());
        passwordText.setOnFocusChangeListener(new mOnFocusChangeListener());

        courseText = (EditText) findViewById(R.id.editTextcCourse);
        courseText.setOnFocusChangeListener(new mOnFocusChangeListener());

        favouriteText = (EditText) findViewById(R.id.editTextcPlace);
        favouriteText.setOnFocusChangeListener(new mOnFocusChangeListener());
    }

    private void createButtons(){
        createButton = (Button) findViewById(R.id.buttoncCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - Don't let there be duplicates in the database for username
                if(usernameText.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter a username",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else if (passwordText.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter a password",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    // TODO - Add the account info to the database
                    Intent intent = new Intent(CreateActivity.this, LoginActivity.class);
                    startActivity(intent);
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
