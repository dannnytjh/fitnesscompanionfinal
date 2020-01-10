package fitnesscompanion.com.Util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

@SuppressLint("ValidFragment")
public class SeekBarPicker extends DialogFragment {

    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.txtValue) TextView txtValue;
    @BindView(R.id.txtValueUnit) TextView txtValueUnit;
    @BindView(R.id.txtMax) TextView txtMax;
    @BindView(R.id.txtMaxUnit) TextView txtMaxUnit;
    @BindView(R.id.seekBar) SeekBar seekBar;
    @BindView(R.id.btnUpdate) Button btnUpdate;
    @BindView(R.id.btnCancel) Button btnCancel;

    private String title;
    private int max;
    private int min;
    private int value;
    private int range =1;
    private String unit;
    private GetValue getValue;

    private Context context;
    private AlertDialog.Builder builder;
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            if (progress <= min) {
                seekBar.setProgress(min);
                progress = min * range;
            } else {
                progress = progress * range;
            }
            txtValue.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private View.OnClickListener onUpdate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getDialog().dismiss();
            getValue.onSuccess(Integer.parseInt(txtValue.getText().toString()));
        }
    };
    private View.OnClickListener onCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getDialog().dismiss();
        }
    };

    public SeekBarPicker(Context context,GetValue getValue) {
        this.context=context;
        this.getValue=getValue;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setRange(int range) {
        this.range=range;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_seekbar,null);
        builder.setView(rootView);
        ButterKnife.bind(this, rootView);
        setting();
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        btnCancel.setOnClickListener(onCancel);
        btnUpdate.setOnClickListener(onUpdate);
        return builder.create();
    }

    private void setting() {
        min=min/range;
        txtTitle.setText(title);
        txtMaxUnit.setText(unit);
        txtMax.setText(String.valueOf(max));
        txtValueUnit.setText(unit);
        txtValue.setText(String.valueOf(value));
        seekBar.setMax(max/range);
        seekBar.setProgress(value/range);
    }
    public interface GetValue{
        void onSuccess(int value);
    }
}
