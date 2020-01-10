package fitnesscompanion.com.View.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import fitnesscompanion.com.R;

public class ItemAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private ArrayList<Integer> colList;
    private ArrayList<Float> distList;
    private ArrayList<String> timeList;
    private ArrayList<Float> avgPaceList;
    private ArrayList<String> dateList;
    private ArrayList<Integer> traineeIDList;


    ItemAdapter (Context context, ArrayList<Integer> id, ArrayList<Float> dist, ArrayList<String> time,
                        ArrayList<Float> avgPace, ArrayList<String> date, ArrayList<Integer> traineeID){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        colList = id;
        distList = dist;
        timeList = time;
        avgPaceList = avgPace;
        dateList = date;
        traineeIDList = traineeID;
    }

    @Override
    public int getCount() {
        return distList.size();
    }

    @Override
    public Object getItem(int i) {
        return distList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = inflater.inflate(R.layout.run_listview_detail, null);
        TextView distDetail = v.findViewById(R.id.distDetailTV);
        TextView timeDetail = v.findViewById(R.id.timeDetailTV);
        TextView avgPaceDetail = v.findViewById(R.id.avgPaceDetailTV);
        TextView dateDetail = v.findViewById(R.id.dateDetailTV);
        TextView columnId = v.findViewById(R.id.columnId);

        // Displaying list in reverse chronological order (list.size() - 1 - i)
        String dist = String.format(Locale.getDefault(),"%.2f ",
                distList.get(distList.size() - 1 - i));
        String time = timeList.get(timeList.size() - 1 - i);
        String avgP = String.format(Locale.getDefault(), "%.4s ",
                convertDecimalToMins(avgPaceList.get(avgPaceList.size() - 1 - i)));
        String date = dateList.get(dateList.size() - 1 - i);
        int ID = colList.get(colList.size()-1-i);
        int tID = traineeIDList.get(traineeIDList.size()-1-i);



        columnId.setText(String.valueOf(ID));
        distDetail.setText(dist);
        timeDetail.setText(time);
        avgPaceDetail.setText(avgP);
        dateDetail.setText(date);

        return v;
    }


    /**
     * Convert a decimal representing time into 'mm:ss' format.
     * Example: 9.87 minutes => 9 mins 52 secs (9:52)
     * @param decimal - Float of decimal number representing minutes (e.g. 9.87 mins)
     * @return - String of converted decimal in 'mm:ss' format
     */
    private String convertDecimalToMins(float decimal){
        int mins = (int) Math.floor(decimal);
        double fractional = decimal - mins;
        int secs = (int) Math.round(fractional * 60);
        return String.format(Locale.getDefault(), "%d:%02d", mins, secs);
    }
}
