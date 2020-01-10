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
import android.widget.RatingBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Model.Feedback;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.FeedbackRequest;
import fitnesscompanion.com.Util.ConnectionDetector;
/**
 * Created by Soon Kok Fung
 */

@SuppressLint("ValidFragment")
public class FeedbackDialog extends DialogFragment {

    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.editFeedback) EditText editFeedback;
    @BindView(R.id.btnOk) Button btnOk;
    @BindView(R.id.btnCancel) Button btnCancel;

    private Context context;
    private AlertDialog.Builder builder;
    private ConnectionDetector detector;
    private View.OnClickListener onSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (detector.isConnected()) {
                getDialog().dismiss();
                new FeedbackRequest(context).addFeedback(new Feedback(editFeedback.getText().toString(), ratingBar.getRating()));
            }
        }
    };
    private View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getDialog().dismiss();
        }
    };
    public FeedbackDialog(Context context){
        this.context=context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_feedback,null);
        builder.setView(rootView);
        ButterKnife.bind(this, rootView);
        detector = new ConnectionDetector(context);
        btnCancel.setOnClickListener(onCancel);
        btnOk.setOnClickListener(onSubmit);
        return builder.create();
    }
}
