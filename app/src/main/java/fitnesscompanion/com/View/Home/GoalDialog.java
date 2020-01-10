package fitnesscompanion.com.View.Home;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.R;

public class GoalDialog extends DialogFragment {
    @BindView(R.id.btnSubmitGoal) Button btnSubmit;
    @BindView(R.id.btnCancelGoal) Button btnCancel;
    @BindView(R.id.spinnerGoal) Spinner spinGoal;
    private AlertDialog.Builder builder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_food_goal,container,false);
        ButterKnife.bind(this,view);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}
