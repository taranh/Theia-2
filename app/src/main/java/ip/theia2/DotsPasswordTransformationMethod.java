package ip.theia2;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 *  This class consists of methods which creates a new Transformation method to convert char
 *  sequences into dots used in password EditText.
 *
 *  @author Kai Diep
 *  @see <a href="http://goo.gl/XcHKV4">Change EditText password mask character to asterisk (*)</a>
 *  @see    PasswordTransformationMethod
 *  @see    CharSequence
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
