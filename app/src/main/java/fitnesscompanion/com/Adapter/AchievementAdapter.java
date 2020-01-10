package fitnesscompanion.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.ActivityDB;
import fitnesscompanion.com.Database.ActivityDataDB;
import fitnesscompanion.com.Model.Achievement;
import fitnesscompanion.com.Model.Activity;
import fitnesscompanion.com.R;
/**
 * Created by Soon Kok Fung
 */

public class AchievementAdapter extends BaseAdapter {

    private final int defaultValue = 5;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.rateBar)
    RatingBar rateBar;
    @BindView(R.id.txtValue) TextView txtValue;
    @BindView(R.id.txtMax) TextView txtMax;
    private Context context;
    private ActivityDB activityDB;
    private ActivityDataDB activityDataDB;
    private ArrayList<Activity> activities;
    private ArrayList<Achievement> activityData;

    public AchievementAdapter(Context context) {
        this.context=context;
        activityDB = new ActivityDB(context);
        activityDataDB = new ActivityDataDB(context);
        activities = activityDB.getAllData();
        activityData = activityDataDB.getAchievement();
    }
    public void setValue(int value) {
        int power = 3;
        int star =3;

        if(value<defaultValue) {
            power=1;
            star=0;
        }
        else if(value>=defaultValue & value<(int)Math.pow(defaultValue,2)) {
            power=2;
            star=1;
        }
        else if (value>=(int)Math.pow(defaultValue,2)&value<(int)Math.pow(defaultValue,3)) {
            power=3;
            star=2;
        }

        txtMax.setText(String.valueOf((int)Math.pow(defaultValue,power)));
        txtValue.setText(String.valueOf(value));
        rateBar.setRating(star);
    }
    @Override
    public int getCount() {
        return activities.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_achievement,null);
        ButterKnife.bind(this, rowView);

        imageView.setImageBitmap(activityData.get(position).getImageFromJSon());
        txtTitle.setText(activityData.get(position).getName());
        setValue(activityData.get(position).getCount());

        return rowView;
    }
}
