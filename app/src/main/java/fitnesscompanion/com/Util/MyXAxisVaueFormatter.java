package fitnesscompanion.com.Util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Soon Kok Fung
 */

public class MyXAxisVaueFormatter implements IAxisValueFormatter {
    private String[] stringValue;

    public MyXAxisVaueFormatter(String[] stringValue) {
        this.stringValue=stringValue;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return stringValue[(int)value];
    }
}
