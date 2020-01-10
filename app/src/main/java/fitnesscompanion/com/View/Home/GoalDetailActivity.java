package fitnesscompanion.com.View.Home;

import android.app.Activity;
import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.StandardGoalDB;
import fitnesscompanion.com.Model.Goal;
import fitnesscompanion.com.Model.StandardGoal;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.UserRequest;
import fitnesscompanion.com.Util.Validation;

public class GoalDetailActivity extends AppCompatActivity {
@BindView(R.id.textViewGoalNameTitle) TextView goalNameTitle;
@BindView(R.id.descriptionFill) EditText description;
@BindView(R.id.targetNumber) EditText measurement;
@BindView(R.id.textViewFoodIntake) TextView foodIntake;
@BindView(R.id.textViewActivityDuration) TextView activityDuration;
@BindView(R.id.btnCancel) Button btnCancel;
@BindView(R.id.btnSubmitGoal) Button btnSubmit;
@BindView(R.id.rgbType) RadioGroup rgbType;
@BindView(R.id.layoutDescription) TextInputLayout layoutDescription;

private int standardGoalId;
private StandardGoalDB standardGoalDB;
private StandardGoal standardGoal;
private Goal goal;
private UserRequest userRequest;
private Validation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Goal Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        standardGoalId = getIntent().getIntExtra("standardGoalId",0);
        standardGoalDB = new StandardGoalDB(this);
        userRequest = new UserRequest(this);
        validation = new Validation(this);
        standardGoal= standardGoalDB.getStandardGoal(standardGoalId);
        goalNameTitle.setText(standardGoal.getGoalName());
        foodIntake.setText(String.valueOf(standardGoal.getFoodIntake()));
        activityDuration.setText(String.valueOf(standardGoal.getActivityDuration()));

       /* btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            goal=new Goal();

                if(validation()){
                    int measure = Integer.parseInt(measurement.getText().toString());
                    if(rgbType.getCheckedRadioButtonId() == R.id.rbWeight){
                        goal.setType(1);
                    }
                    else if(rgbType.getCheckedRadioButtonId() == R.id.rbFoodIntake){
                        goal.setType(2);
                    }
                    else if(rgbType.getCheckedRadioButtonId() == R.id.rbExercise){
                        goal.setType(3);
                    }
                    String describe = description.getText().toString();
                    goal.setMeasurement(measure);
                    goal.setDescription(describe);
                    userRequest.updateGoal(goal,standardGoal);
                }


            }

        });*/
    }
    public boolean validation(){
        return validation.checkDescription(description,layoutDescription) & validation.checkMeasurement(measurement);
    }
    public void onBack(View view){
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //startActivity(new Intent(this,MenuActivity.class).putExtra("index",0));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void onSubmit(View view){
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        goal=new Goal();

        if(validation()){
            int measure = Integer.parseInt(measurement.getText().toString());
            if(rgbType.getCheckedRadioButtonId() == R.id.rbWeight){
                goal.setType(1);
            }
            else if(rgbType.getCheckedRadioButtonId() == R.id.rbFoodIntake){
                goal.setType(2);
            }
            else if(rgbType.getCheckedRadioButtonId() == R.id.rbExercise){
                goal.setType(3);
            }
            String describe = description.getText().toString();
            goal.setMeasurement(measure);
            goal.setDescription(describe);
            userRequest.insertGoal(goal,standardGoal);
            finish();
            startActivity(new Intent(this,MenuActivity.class).putExtra("index",0));
        }
    }
}
