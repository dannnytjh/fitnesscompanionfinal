package fitnesscompanion.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Model.Activity;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class ActivityAdapter extends BaseAdapter {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtDuration)
    TextView txtDuration;
    @BindView(R.id.txtCalories)
    TextView txtCalories;

    private Context context;
    private ArrayList<Activity> activities;

    public ActivityAdapter(Context context, ArrayList<Activity> activities) {
        this.context=context;
        this.activities=activities;
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
        View rowView = inflater.inflate(R.layout.adapter_activity,null);
        ButterKnife.bind(this, rowView);

        imageView.setImageBitmap(activities.get(position).getImageFromJSon());
        txtName.setText(activities.get(position).getName());
        txtDuration.setText(String.valueOf(activities.get(position).getTime()));
        txtCalories.setText(String.valueOf(activities.get(position).getCalories()));

        return rowView;
    }
}
