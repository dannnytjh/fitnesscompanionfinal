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
import fitnesscompanion.com.Util.Listener;
import fitnesscompanion.com.Util.Validation;

/**
 * Created by Soon Kok Fung
 */
@SuppressLint("ValidFragment")
public class UpdateNameDialog extends DialogFragment {

    @BindView(R.id.editName) EditText editName;
    @BindView(R.id.layoutName) TextInputLayout layoutName;
    @BindView(R.id.btnUpdate) Button btnUpdate;
    @BindView(R.id.btnCancel) Button btnCancel;

    private Context context;
    private AlertDialog.Builder builder;
    private Validation validation;
    private GetName getName;
    private View.OnClickListener onUpdate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (validation.checkName(editName, layoutName)) {
                getDialog().dismiss();
                getName.onSuccess(editName.getText().toString());
            }
        }
    };
    private View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getDialog().dismiss();
        }
    };
    public UpdateNameDialog(Context context, GetName getName) {
        this.context=context;
        this.getName = getName;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_update_name,null);
        builder.setView(rootView);
        ButterKnife.bind(this, rootView);
        validation = new Validation(context);
        new Listener(context).setListenerName(editName,layoutName);
        btnUpdate.setOnClickListener(onUpdate);
        btnCancel.setOnClickListener(onCancel);

        return builder.create();
    }
    public interface GetName{
        void onSuccess(String name);
    }
}
