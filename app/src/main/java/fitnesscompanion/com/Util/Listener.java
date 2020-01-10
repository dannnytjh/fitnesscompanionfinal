package fitnesscompanion.com.Util;

import android.content.Context;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
/**
 * Created by Soon Kok Fung
 */

public class Listener {
    private Validation validation;
    private EditText editText;
    private EditText editText2;
    private TextInputLayout textInputLayout;

    public Listener(Context context) {
        validation = new Validation(context);
    }
    public void setListenerEmail(EditText editText, TextInputLayout textInputLayout) {
        this.editText =editText;
        this.textInputLayout=textInputLayout;
        editText.addTextChangedListener(new onTextWatcherEmail());
    }
    public void setListenerName(EditText editText, TextInputLayout textInputLayout) {
        this.editText =editText;
        this.textInputLayout=textInputLayout;
        editText.addTextChangedListener(new onTextWatcherName());
    }
    public void setListenerPass(EditText editText, TextInputLayout textInputLayout) {
        this.editText =editText;
        this.textInputLayout=textInputLayout;
        editText.addTextChangedListener(new onTextWatcherPass());
    }
    public void setListenerCPass(EditText editText, EditText editText2, TextInputLayout textInputLayout) {
        this.editText =editText;
        this.editText2 =editText2;
        this.textInputLayout=textInputLayout;
        editText.addTextChangedListener(new onTextWatcherCPass());
    }
    public void setListenerCurrentPass(EditText editText, TextInputLayout textInputLayout) {
        this.editText =editText;
        this.textInputLayout=textInputLayout;
        editText.addTextChangedListener(new onTextWatcherCurrentPass());
    }

    public class onTextWatcherName implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validation.checkName(editText,textInputLayout);

        }

        @Override
        public void afterTextChanged(Editable s) {
            validation.checkName(editText,textInputLayout);
        }
    }

    public class onTextWatcherEmail implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validation.checkEmail(editText,textInputLayout);
        }

        @Override
        public void afterTextChanged(Editable s) {
            validation.checkEmail(editText,textInputLayout);
        }
    }
    private class onTextWatcherPass implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validation.checkPassword(editText,textInputLayout);
        }

        @Override
        public void afterTextChanged(Editable s) {
            validation.checkPassword(editText,textInputLayout);
        }
    }
    private class onTextWatcherCPass implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validation.checkConfirmPassword(editText,editText2,textInputLayout);
        }

        @Override
        public void afterTextChanged(Editable s) {
            validation.checkConfirmPassword(editText,editText2,textInputLayout);
        }
    }
    public class onTextWatcherCurrentPass implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validation.checkCurrentPass(editText,textInputLayout);

        }

        @Override
        public void afterTextChanged(Editable s) {
            validation.checkCurrentPass(editText,textInputLayout);
        }
    }

}
