package fitnesscompanion.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Model.Activity;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class ActivityExpandableAdapter extends BaseExpandableListAdapter {


    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtDuration)
    TextView txtDuration;
    @BindView(R.id.txtCalories)
    TextView txtCalories;


    private Context context;
    private ArrayList<String> listDataHeader;
    private HashMap<String, ArrayList<Activity>> listData;

    public ActivityExpandableAdapter(Context context, ArrayList<String> listDataHeader, HashMap<String, ArrayList<Activity>> listData) {
        this.context=context;
        this.listDataHeader=listDataHeader;
        this.listData=listData;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.adapter_activity_header,null);

        ((TextView)convertView.findViewById(R.id.txtTitle)).setText((String)getGroup(groupPosition));


        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.adapter_activity,null);
        ButterKnife.bind(this, convertView);

        Activity activity = (Activity) getChild(groupPosition,childPosition);

        imageView.setImageBitmap(activity.getImageFromJSon());
        txtName.setText(activity.getName());
        txtDuration.setText(String.valueOf(activity.getTime()));
        txtCalories.setText(String.valueOf(activity.getCalories()));

        return convertView;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listData.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listData.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
