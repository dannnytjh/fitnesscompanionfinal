package fitnesscompanion.com.View.Profile.Graph;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.R;
import fitnesscompanion.com.Util.DatePicker;
import fitnesscompanion.com.Util.MyXAxisVaueFormatter;


@SuppressLint("ValidFragment")
public class BarChartFragment extends Fragment {
    @BindView(R.id.txtDate) TextView txtDate;
    @BindView(R.id.barChart) BarChart barChart;

    private Context context;
    private int mode;
    private int month;
    private int year;


    public BarChartFragment(Context context,int mode) {
        this.context=context;
        this.mode=mode;
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        ButterKnife.bind(this, rootView);

        txtDate.setOnClickListener(onDateControl);
        txtDate.setText(getResources().getStringArray(R.array.monthArray)[month] +" " +String.valueOf(year));

        return rootView;

    }
    private View.OnClickListener onDateControl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           DatePicker datePicker = new DatePicker();
        }
    };

    private void barChartController() {
        barChart.invalidate();
        barChart.clear();
        barChart.fitScreen();

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.setFitBars(true);
        barChart.animateY(2500);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new MyXAxisVaueFormatter(getResources().getStringArray(R.array.monthArray)));
        //xAxis.setLabelCount((getResources().getStringArray(R.array.monthArray)).length-1);

        YAxis left = barChart.getAxisLeft();
        left.setDrawAxisLine(true);
        left.setDrawGridLines(true);
        left.setAxisMinimum(0f);
        left.setGranularity(1f);

        YAxis right = barChart.getAxisRight();
        right.setDrawAxisLine(true);
        right.setDrawGridLines(true);
        right.setAxisMinimum(0f);
        right.setGranularity(1f);
    }

}
