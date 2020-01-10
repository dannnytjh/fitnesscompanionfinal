package fitnesscompanion.com.View;


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
import android.widget.Toast;

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
public class PassRecovery extends DialogFragment {

    @BindView(R.id.editEmail) EditText editEmail;
    @BindView(R.id.layoutEmail) TextInputLayout layoutEmail;
    @BindView(R.id.btnRecovery) Button btnRecovery;
    @BindView(R.id.btnCancel) Button btnCancel;

    private Context context;
    private AlertDialog.Builder builder;
    private Validation validation;
    private ConnectionDetector detector;
    private UserRequest userRequest;
    private View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getDialog().dismiss();
        }
    };
    private View.OnClickListener onRecovery = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(validation.checkEmail(editEmail,layoutEmail)&detector.isConnected()) {
               userRequest.checkMail(new UserRequest.VolleyCallCheckMail() {
                   @Override
                   public void onSuccess(int count) {
                       switch (count) {
                           case 0 :
                               Toast.makeText(context,getString(R.string.status_recovery),Toast.LENGTH_LONG).show();
                               break;
                           case 1 :
                               userRequest.resetPass(editEmail.getText().toString());
                               getDialog().dismiss();
                               break;
                       }

                   }
               },editEmail.getText().toString());
            }

        }
    };

    public PassRecovery(Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_forget_pass, null);
        builder.setView(rootView);
        ButterKnife.bind(this, rootView);
        validation = new Validation(context);
        userRequest = new UserRequest(context);
        detector = new ConnectionDetector(context);
        btnRecovery.setOnClickListener(onRecovery);
        btnCancel.setOnClickListener(onCancel);
        new Listener(context).setListenerEmail(editEmail, layoutEmail);

        return builder.create();
    }
}
