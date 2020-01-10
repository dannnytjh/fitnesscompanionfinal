package fitnesscompanion.com.Adapter;

import android.content.Context;
import android.util.Log;
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
import fitnesscompanion.com.Model.ActivityAssigned;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */
public class ActivityAssignedExpandableAdapter extends BaseExpandableListAdapter {

    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.txtCal) TextView txtCal;
    @BindView(R.id.txtDuration) TextView txtDuration;
  //  @BindView(R.id.imageView) ImageView imageView;

    private ArrayList<ActivityAssigned> assignedArrayList;
    private HashMap<ActivityAssigned,ArrayList<Activity>> listData;
    private Context context;

    public ActivityAssignedExpandableAdapter(Context context,ArrayList<ActivityAssigned> assignedArrayList, HashMap<ActivityAssigned, ArrayList<Activity>> listData) {
        this.assignedArrayList = assignedArrayList;
        this.listData = listData;
        this.context = context;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.adapter_diet_header,null);
        int cal =0;
        ArrayList<Activity> activities = assignedArrayList.get(groupPosition).getActivities();

        for(int x=0;x<activities.size();x++) {
            cal += activities.get(x).getCalories() * (activities.get(x).getTime());
        }

        ((TextView)convertView.findViewById(R.id.txtTitle)).setText(assignedArrayList.get(groupPosition).getName());
       // ((TextView)convertView.findViewById(R.id.txtCal)).setText(String.valueOf(cal)+ "cal");

        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.adapter_activity_assigned,null);
        ButterKnife.bind(this,convertView);

        Activity activity = (Activity)getChild(groupPosition,childPosition);
        Log.i("Here","Title : "+activity.getName());
        Log.i("Here","Cal : "+ String.valueOf(activity.getCalories()));
        txtTitle.setText(activity.getName());
        txtDuration.setText(String.valueOf(activity.getTime())+ " minutes");
        txtCal.setText(String.valueOf((activity.getCalories())*activity.getTime())+" cal");

//        imageView.setImageBitmap(activity.getImageFromJSon());

        return convertView;
    }

    @Override
    public int getGroupCount() {
        return assignedArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listData.get(assignedArrayList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return assignedArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listData.get(assignedArrayList.get(groupPosition)).get(childPosition);
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
