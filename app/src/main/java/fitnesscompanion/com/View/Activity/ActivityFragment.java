package fitnesscompanion.com.View.Activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Adapter.ActivityAdapter;
import fitnesscompanion.com.Adapter.ActivityExpandableAdapter;
import fitnesscompanion.com.Database.ActivityDB;
import fitnesscompanion.com.Model.Activity;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */
@SuppressLint("ValidFragment")
public class ActivityFragment extends Fragment {
    @BindView(R.id.listViewNormal) ListView listViewNormal;
    @BindView(R.id.expandedListView) ExpandableListView expandedListView;
    @BindView(R.id.linearLayout) LinearLayout linearLayout;

    private ActivityDB activityDB;

    private ArrayList<Activity> activityList;
    private ArrayList<Activity> commonActivity;
    private ArrayList<Activity> favouriteActivity;
    private ActivityExpandableAdapter activityExpandableAdapter;

    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            getActivity().finish();
            startActivity(new Intent(context, DetailActivity.class).putExtra("no", activityList.get(position).getNo()));
        }
    };
    private ExpandableListView.OnChildClickListener onChildClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
            Activity activity = (Activity) activityExpandableAdapter.getChild(groupPosition, childPosition);
            getActivity().finish();
            startActivity(new Intent(context, DetailActivity.class).putExtra("no", activity.getNo()));
            return false;
        }
    };

    public ActivityFragment(Context context) {
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDB = new ActivityDB(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activity, container, false);
        ButterKnife.bind(this, rootView);

        favouriteActivity = activityDB.getFavourite();

        if(favouriteActivity.size()!=0) {
            linearLayout.setVisibility(View.GONE);
            expandedListView.setOnChildClickListener(onChildClickListener);
            activityExpandableAdapter = new ActivityExpandableAdapter(context,getHeader(),getData());
            expandedListView.setAdapter(activityExpandableAdapter);
            expandedListView.expandGroup(0);
        }
        else {
            activityList=activityDB.getAllData();
            listViewNormal.setOnItemClickListener(onItemClickListener);
            listViewNormal.setAdapter(new ActivityAdapter(context,activityList));
        }
        return rootView;
    }

    private ArrayList<String> getHeader() {
        ArrayList<String> header = new ArrayList<String>();
        header.add(context.getString(R.string.favourite));
        header.add(context.getString(R.string.common));
        return header;
    }
    private HashMap<String, ArrayList<Activity>> getData() {
        HashMap<String, ArrayList<Activity>>
                arrayListHashMap = new
                HashMap<String,ArrayList<Activity>>();

        commonActivity= activityDB.getCommon();
        favouriteActivity=activityDB.getFavourite();

        arrayListHashMap.put(getHeader().get(0),favouriteActivity);
        arrayListHashMap.put(getHeader().get(1),commonActivity);

        return arrayListHashMap;
    }

}
