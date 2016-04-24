package ip.theia2;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 *  Transformation method to convert char sequence into dots to be used in password EditText.
 */
public class DotsPasswordTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence source;

        public PasswordCharSequence(CharSequence source) {
            // Store char sequence
            this.source = source;
        }

        public char charAt(int index) {
            // Convert char to dots
            return (char) Integer.parseInt("\\u2022".substring(2), 16);
        }

        public int length() {
            return source.length();
        }

        public CharSequence subSequence(int start, int end) {
            return source.subSequence(start, end);
        }
    }
}
