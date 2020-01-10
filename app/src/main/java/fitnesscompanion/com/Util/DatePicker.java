package fitnesscompanion.com.Util;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Soon Kok Fung
 */

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private Context context;
    private long max;
    private long min;
    private Calendar selected = Calendar.getInstance();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private GetDate getDate;
    private DatePickerDialog datePickerDialog;

    public DatePicker() {}
    @SuppressLint("ValidFragment")
    public DatePicker(Context context,GetDate getDate) {
        this.context=context;
        this.getDate=getDate;
    }

    //set By date format
    public void setSelectedDate(Date date){
        selected.setTime(date);
    }
    public void setMaxDate(Date date) {
        max = date.getTime();
    }
    public void setMinDate(Date date) {
        min = date.getTime();
    }
    //set By String format(yyyy/MM/dd)

    public void setSelectedDate(String date) {
        try {
            selected.setTime(new Date(String.valueOf(dateFormat.parse(date))));
        } catch (ParseException e) {
            Log.e("Invalid Date Format","Please use (yyyy-MM-dd) format");
        }
    }
    public void setMaxDate(String date) {
        try {
            max = dateFormat.parse(date).getTime();
        } catch (ParseException e) {
            Log.e("Invalid Date Format","Please use (yyyy-MM-dd) format");
        }
    }
    public void setMinDate(String date) {
        try {
            min = dateFormat.parse(date).getTime();
        } catch (ParseException e) {
            Log.e("Invalid Date Format","Please use (yyyy-MM-dd) format");
        }
    }
    private void datePickerSetting() {
        if(max!=0)
            datePickerDialog.getDatePicker().setMaxDate(max);
        if(min!=0)
            datePickerDialog.getDatePicker().setMinDate(min);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = selected.get(Calendar.YEAR);
        int month = selected.get(Calendar.MONTH);
        int day = selected.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(context,this,year,month,day);
        datePickerDialog.setCustomTitle(new LinearLayout(context));

        datePickerSetting();

        return datePickerDialog;
    }
    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, dayOfMonth, 0, 0, 0);
        getDate.onSuccess(cal.getTime());
    }

    public interface GetDate{
        void onSuccess(Date date);
    }
}
