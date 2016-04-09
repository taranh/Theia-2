package ip.theia2;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 *  Transformation method to convert char sequence into dots to be used in password EditText.
 *
 *  Adapted from user "IntelliJ Amiya" answer in:
 *  http://stackoverflow.com/questions/6125352/how-to-change-password-field-to-diplay-asterisks-instead-of-dots
 */

public class DotsPasswordTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence source;

        public PasswordCharSequence(CharSequence source) {
            this.source = source;   // Store char sequence
        }

        public char charAt(int index) {
            return (char) Integer.parseInt("\\u2022".substring(2), 16); // Convert char to dots
        }

        public int length() {
            return source.length();
        }

        public CharSequence subSequence(int start, int end) {
            return source.subSequence(start, end);
        }
    }
}
