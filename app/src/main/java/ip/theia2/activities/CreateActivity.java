package ip.theia2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ip.theia2.DotsPasswordTransformationMethod;
import ip.theia2.R;

/**
 * This class consists of methods for the "Create Account" activity which handles account creation
 * in the app.
 *
 * @author  Taran Hackman
 * @author  Kai Diep
 */
public class CreateActivity extends Activity {
    Button createButton;
    EditText usernameText, passwordText, courseText, favouriteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the layout of activity.
        setContentView(R.layout.create_page);
        createButtons();
        createEditText();
    }

    /**
     * Adds OnFocusChangeListeners for several EditText objects to ensure that the keyboard is hidden
     * whenever an EditText loses focus.
     */
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

    /**
     * Adds an OnClickListener for the button checking for null username or password. On the button
     * is pressed, an account will be created which the user can use to log into the app providing
     * that the username or password is not empty.
     *
     * TODO - Don't let there be duplicates in the database for username
     * TODO - Add the account info to the database
     */
    private void createButtons() {
        createButton = (Button) findViewById(R.id.buttoncCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameText.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter a username",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else if (passwordText.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter a password",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Intent intent = new Intent(CreateActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Hides the keyboard whenever a View object loses focus in the app.
     *
     * @param view the View to hide the keyboard on.
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Activity.INPUT_METHOD_SERVICE);
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
