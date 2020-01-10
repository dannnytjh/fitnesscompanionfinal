package fitnesscompanion.com.Util;

import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Soon Kok Fung
 */

public class SeekBarController {
    private int min;
    private int range;
    private TextView txtResult;

    public SeekBarController(int min, int max , int value, int range , SeekBar seekBar, TextView txtResult) {
        this.min =min/range;
        this.txtResult=txtResult;
        this.range=range;

        seekBar.setMax(max/range);
        seekBar.setProgress(value/range);
        txtResult.setText(String.valueOf(value));
        seekBar.setOnSeekBarChangeListener(new onSeekBar());
    }
    private class onSeekBar implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(progress<=min){
                seekBar.setProgress(min);
                progress = min*range;
            }
            else{
                progress = progress*range;
            }
            txtResult.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
