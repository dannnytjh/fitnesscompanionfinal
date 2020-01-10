package fitnesscompanion.com.View.Home;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.GoalDB;
import fitnesscompanion.com.Database.StandardGoalDB;
import fitnesscompanion.com.Model.Goal;
import fitnesscompanion.com.Model.StandardGoal;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.UserRequest;

public class DisplayGoal extends AppCompatActivity {
@BindView(R.id.textViewGoalNameTitle)
    TextView goalName;
@BindView(R.id.txtDescGoal) TextView goalDesc;
@BindView(R.id.txtType) TextView goalType;

    private int standardGoalId;
    private StandardGoalDB standardGoalDB;
    private GoalDB goalDB;
    private StandardGoal standardGoal;
    private ArrayList<Goal> goal;
    private UserRequest userRequest;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_goal);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Goal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        standardGoalId = getIntent().getIntExtra("standardGoalId",0);
        standardGoalDB = new StandardGoalDB(this);
        userRequest = new UserRequest(this);
        goalDB = new GoalDB(this);
        standardGoal= standardGoalDB.getStandardGoal(standardGoalId);
        goal = goalDB.getAllData();
        goalName.setText(standardGoal.getGoalName());
        goalDesc.setText(goal.get(0).getDescription());
        processType();
    }
    public void processType(){
        if(goal.get(0).getType() == 1){
            type = "Weight";
        }
        else if(goal.get(0).getType() == 2){
            type = "Food Intake";
        }
        else if(goal.get(0).getType() == 3){
            type = "Exercise";
        }goalType.setText(type);
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
