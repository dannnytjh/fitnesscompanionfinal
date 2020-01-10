package fitnesscompanion.com.View.Profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.UserRequest;
import fitnesscompanion.com.Util.ConnectionDetector;
import fitnesscompanion.com.Util.Listener;
import fitnesscompanion.com.Util.Validation;
/**
 * Created by Soon Kok Fung
 */

@SuppressLint("ValidFragment")
public class ChangePassDialog extends DialogFragment {

    @BindView(R.id.editPass) EditText editPass;
    @BindView(R.id.editNewPass) EditText editNewPass;
    @BindView(R.id.editNewPassC) EditText editNewPassC;
    @BindView(R.id.layoutPass) TextInputLayout layoutPass;
    @BindView(R.id.layoutNewPass) TextInputLayout layoutNewPass;
    @BindView(R.id.layoutNewPassC) TextInputLayout layoutNewPassC;
    @BindView(R.id.btnUpdate) Button btnUpdate;
    @BindView(R.id.btnCancel) Button btnCancel;

    private Context context;
    private AlertDialog.Builder builder;
    private ConnectionDetector detector;
    private Validation validation;
    private UserRequest userRequest;
    private Check check;
    private View.OnClickListener onUpdate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (detector.isConnected() & validation()) {
                userRequest.updatePassword(editNewPass.getText().toString());
                getDialog().dismiss();
                check.onSuccess(true);
            }
        }
    };
    private View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getDialog().dismiss();
        }
    };
    public ChangePassDialog(Context context,Check check) {
        this.context=context;
        this.check=check;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_update_pass,null);
        builder.setView(rootView);
        ButterKnife.bind(this, rootView);
        detector = new ConnectionDetector(context);
        validation = new Validation(context);
        userRequest = new UserRequest(context);

        new Listener(context).setListenerCurrentPass(editPass,layoutPass);
        new Listener(context).setListenerPass(editNewPass,layoutNewPass);
        new Listener(context).setListenerCPass(editNewPassC,editNewPass,layoutNewPassC);

        btnCancel.setOnClickListener(onCancel);
        btnUpdate.setOnClickListener(onUpdate);
        return builder.create();
    }

    private boolean validation() {
        return validation.checkCurrentPass(editPass,layoutPass)&
                validation.checkPassword(editNewPass,layoutNewPass)&
                validation.checkConfirmPassword(editNewPassC,editNewPass,layoutNewPassC);
    }

    public interface Check{
        void onSuccess(boolean check);
    }
}
