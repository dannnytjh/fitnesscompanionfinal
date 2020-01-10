package fitnesscompanion.com.View.Profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.R;
import fitnesscompanion.com.Util.Listener;
import fitnesscompanion.com.Util.Validation;

@SuppressLint("ValidFragment")
public class UpdateAddressDialog extends DialogFragment {
    @BindView(R.id.editAddress) EditText editAdd;
    @BindView(R.id.btnUpdate) Button btnUpdate;
    @BindView(R.id.btnCancel) Button btnCancel;

    private Context context;
    private AlertDialog.Builder builder;
    private Validation validation;
    private GetAddress getAdd;
    private View.OnClickListener onUpdate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            editAdd.getText().toString();
                getDialog().dismiss();
               getAdd.onSuccess(editAdd.getText().toString());

        }
    };
    private View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getDialog().dismiss();
        }
    };

    public UpdateAddressDialog(Context context, GetAddress getAddress) {
        this.context=context;
        this.getAdd = getAddress;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_update_address,null);
        builder.setView(rootView);
        ButterKnife.bind(this, rootView);
        validation = new Validation(context);
      //  new Listener(context).setListenerName(editName,layoutName);
        btnUpdate.setOnClickListener(onUpdate);
        btnCancel.setOnClickListener(onCancel);

        return builder.create();
    }
    public interface GetAddress{
        void onSuccess(String address);
    }

}
