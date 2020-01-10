package fitnesscompanion.com.View;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.GoalDB;
import fitnesscompanion.com.Database.HealthProfileDB;
import fitnesscompanion.com.Database.UserDB;
import fitnesscompanion.com.Model.HealthProfile;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.HealthProfileRequest;
import fitnesscompanion.com.Util.SeekBarController;
import fitnesscompanion.com.View.Home.MenuActivity;

/**
 * Created by Soon Kok Fung
 */
public class RecordActivity extends AppCompatActivity {
    @BindView(R.id.txtHeight)TextView txtHeight;
    @BindView(R.id.txtWeight)TextView txtWeight;
 //   @BindView(R.id.txtStep)TextView txtStep;
    //@BindView(R.id.txtGoal)TextView txtGoal;
   // @BindView(R.id.txtCalories)TextView txtCalories;
    @BindView(R.id.seekWeight)SeekBar seekWeight;
    @BindView(R.id.seekHeight)SeekBar seekHeight;
    /*@BindView(R.id.seekStep)SeekBar seekStep;
    @BindView(R.id.seekGoal)SeekBar seekGoal;
    @BindView(R.id.seekCalories)SeekBar seekCalories;*/
    private int backButtonCount=0;

    private UserDB userDB;
    private GoalDB goalDB;
    private HealthProfileDB healthProfileDB;
    private HealthProfileRequest healthProfileRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(getString(R.string.record_setting));
        userDB = new UserDB(this);
        goalDB = new GoalDB(this);
        healthProfileDB = new HealthProfileDB(this);
        goalDB.deteleData();
        healthProfileDB.deteleData();
        healthProfileRequest = new HealthProfileRequest(this);

        new SeekBarController(30,250,160,1,seekHeight,txtHeight);
        new SeekBarController(3,200,60,1,seekWeight,txtWeight);
        /*new SeekBarController(500,30000,8000,500,seekStep,txtStep);
        new SeekBarController(3,200,60,1,seekGoal,txtGoal);
        new SeekBarController(100,800,400,100,seekCalories,txtCalories);*/
    }
    public void onClick(View view) {
        int height = Integer.parseInt(txtHeight.getText().toString());
        int weight = Integer.parseInt(txtWeight.getText().toString());
       /* int goalWeight = Integer.parseInt(txtGoal.getText().toString());
        int goalStep = Integer.parseInt(txtStep.getText().toString());
        int goalCal = Integer.parseInt(txtCalories.getText().toString());*/

        /*goalDB.insertWeight(new Goal(goalWeight));
        goalDB.insertStep(new Goal(goalStep));
        goalDB.insertCal(new Goal(goalCal));*/
        healthProfileDB.insertWeight(new HealthProfile(weight));
        healthProfileDB.updateWeight(weight);
        healthProfileDB.updateHeight(height);

        healthProfileRequest.insertHealthRecord(new HealthProfileRequest.VolleyCall() {
            @Override
            public void onSuccess() {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .edit().putInt("id",userDB.getData().getId()).commit();
                finish();
                startActivity(new Intent(getApplicationContext(),MenuActivity.class));
            }
        }, userDB.getData().getId(), height, weight);

    }
    @Override
    public void onBackPressed() {
        if(backButtonCount >= 1)
        {
            System.exit(0);
        }
        else
        {
            Toast.makeText(this,getString(R.string.status_exit), Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
