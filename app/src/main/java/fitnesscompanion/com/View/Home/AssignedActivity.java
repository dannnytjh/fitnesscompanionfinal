package fitnesscompanion.com.View.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Adapter.ActivityAssignedExpandableAdapter;
import fitnesscompanion.com.Model.Activity;
import fitnesscompanion.com.Model.ActivityAssigned;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.ActivityRequest;

/**
 * Created by Soon Kok Fung
 */
public class AssignedActivity extends AppCompatActivity {

    @BindView(R.id.expandableList) ExpandableListView expandableList;

    private ActivityRequest activityRequest;
    private ActivityAssignedExpandableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned);
        getSupportActionBar().setTitle("Assigned Activity");
        activityRequest= new ActivityRequest(this);
        ButterKnife.bind(this);

        activityRequest.getAssigned(new ActivityRequest.VolleyCallAssigned() {
            @Override
            public void onSuccess(ArrayList<ActivityAssigned> assignedArrayList) {

                if(assignedArrayList.size()==0) {
                    Toast.makeText(getApplicationContext(),"No Activity Assigned",Toast.LENGTH_LONG).show();
                    AlertDialog alertDialog = new AlertDialog.Builder(AssignedActivity.this).create();
                    alertDialog.setTitle("No Activity Assigned");
                    alertDialog.setMessage("There is no activity assigned to you by trainer. Please wait for few days. Thank you.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();
                                }
                            });
                    alertDialog.show();

                }
                else{
                    adapter = new ActivityAssignedExpandableAdapter(getApplicationContext(),assignedArrayList,getData(assignedArrayList));
                    expandableList.setAdapter(adapter);
                }
            }
        });
    }
    private HashMap<ActivityAssigned,ArrayList<Activity>> getData(ArrayList<ActivityAssigned> assignedArrayList) {
        HashMap<ActivityAssigned,ArrayList<Activity>> hashMap = new HashMap<>();

        for(int x=0;x<assignedArrayList.size();x++) {
            hashMap.put(assignedArrayList.get(x),assignedArrayList.get(x).getActivities());
        }

        return hashMap;
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,MenuActivity.class).putExtra("index",0));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
