package fitnesscompanion.com.View.Home;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Adapter.StandardGoalAdapter;
import fitnesscompanion.com.Database.GoalDB;
import fitnesscompanion.com.Database.StandardGoalDB;
import fitnesscompanion.com.Model.Goal;
import fitnesscompanion.com.Model.StandardGoal;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.UserRequest;
import fitnesscompanion.com.Util.ConnectionDetector;

public class NewGoalActivity extends AppCompatActivity {
    /*@BindView(R.id.buttonFood) Button btnFood;
    @BindView(R.id.buttonWeight) Button btnWeight;
    @BindView(R.id.buttonExercise) Button btnExercise;*/
    //@BindView(R.id.spinnerGoal) Spinner spinGoal;
    @BindView(R.id.listViewGoal) ListView listView;

    private ArrayList<Goal> goal;
    private ArrayList<StandardGoal> standardGoal;
    private ConnectionDetector detector;
    private GoalDB goalDB;
    //private Goal goal;
    private StandardGoal sGoal;
    private StandardGoalDB standardGoalDB;
    private UserRequest userRequest;
    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            finish();
            startActivity(new Intent(NewGoalActivity.this, GoalDetailActivity.class).putExtra("standardGoalId", standardGoal.get(position).getStandardGoalId() ));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);
        getSupportActionBar().setTitle("Goal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        detector = new ConnectionDetector(this);
        standardGoalDB = new StandardGoalDB(this);
        goalDB = new GoalDB(this);

        userRequest = new UserRequest(this);
        standardGoal = standardGoalDB.getAllData();
        listView.setOnItemClickListener(onItemClickListener);
        userRequest.getGoalData(new UserRequest.VolleyCallgetUser() {
            @Override
            public void onSuccess() {
                goal = goalDB.getAllData();
                try {

                    //getStandardGoal();
                    if(goal.size() == 0){
                        Toast.makeText(NewGoalActivity.this,"You do not have any goals yet. Please choose a goal",Toast.LENGTH_LONG).show();
                        getStandardGoal();
                    }
                    else {
                        finish();

                        startActivity(new Intent(NewGoalActivity.this, DisplayGoal.class).putExtra("standardGoalId",goal.get(0).getStandardGoalId()));
                    }
                } catch (Exception ex) {
                    Log.i("Here","Error Goal Data : "+ex.getMessage());
                    getStandardGoal();
                }
            }

        });
        /*userRequest.getStandardGoal(new UserRequest.VolleyCallgetUser() {
            @Override
            public void onSuccess() {
                setListView();
            }
        });*/
    }
private void getStandardGoal(){
    userRequest.getStandardGoal(new UserRequest.VolleyCallgetUser() {
        @Override
        public void onSuccess() {
            setListView();
        }
    });
        }


    private void setListView() {
        standardGoal = standardGoalDB.getAllData();
        listView.setAdapter(new StandardGoalAdapter(this,standardGoal));
    }

    /* private void foodDialog(){
     GoalDialog dialog = new GoalDialog();
     dialog.show(getFragmentManager(),"Food Intake");
     }*/
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
