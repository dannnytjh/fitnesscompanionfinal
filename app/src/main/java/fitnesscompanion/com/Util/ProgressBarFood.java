package fitnesscompanion.com.Util;

import android.content.Context;
import android.graphics.Color;
import android.widget.ProgressBar;
import android.widget.TextView;

import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class ProgressBarFood {
    private Context context;
    private ProgressBar progressBar;
    private TextView txtIntake;
    private TextView txtRemaining;
    private int max=1000;
    private int value=1000;
    private int color = Color.GREEN;

    public ProgressBarFood(Context context,ProgressBar progressBar, TextView txtIntake, TextView txtRemaining) {
        this.context=context;
        this.progressBar = progressBar;
        this.txtIntake = txtIntake;
        this.txtRemaining = txtRemaining;
        setProgressBar();
    }

    private void setProgressBar() {



        progressBar.setMax(max);
        progressBar.setProgress(value);

        int remaining = max-value;
        String text =context.getString(R.string.remaining)+" " +String.valueOf(remaining) + " " +context.getString(R.string.cal);

        txtIntake.setText(String.valueOf(value) +" "+ context.getString(R.string.cal) +" " +context.getString(R.string.intake));


        if(remaining<0) {
            text=context.getString(R.string.over)+" " +String.valueOf(value-max) +" "+context.getString(R.string.cal);
        }

        txtRemaining.setText(text);

        ProgressBarAnimation animation = new ProgressBarAnimation(progressBar,0,value);
        animation.setDuration(1000);
        progressBar.startAnimation(animation);

        setProgressBarStyle();
    }
    private void setProgressBarStyle() {

        int percentage = (value*100)/max;

        if(percentage>50 & percentage<=80)
            color = Color.YELLOW;
        if (percentage>80)
            color = Color.RED;

        progressBar.getProgressDrawable().setColorFilter(color,android.graphics.PorterDuff.Mode.SRC_IN);
    }
}
