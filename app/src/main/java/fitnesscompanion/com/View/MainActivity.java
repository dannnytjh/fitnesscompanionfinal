package fitnesscompanion.com.View;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.ActivityVersionDB;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.ActivityRequest;
import fitnesscompanion.com.Util.ConnectionDetector;
import fitnesscompanion.com.Util.NotificationJobSchedule;
import fitnesscompanion.com.View.Home.MenuActivity;

/**
 * Created by Soon Kok Fung
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.txtStatus) TextView txtStatus;
    @BindView(R.id.linearLayout) LinearLayout linearLayout;

    private ConnectionDetector detector;
    private Snackbar snackbar;
    private ActivityRequest activityRequest;
    private ActivityVersionDB versionDB;
    private SharedPreferences preferences;
    private static final int JOB_ID = 0;
    JobScheduler mScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        detector = new ConnectionDetector(this);
        activityRequest = new ActivityRequest(this);
        versionDB = new ActivityVersionDB(this);

        checkUpdate();
        //if android lollipop and above then schedule jobscheduler
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scheduleJob();
        }
    }
    //schedule the jobscheduler to send notification activity recognition
    protected void scheduleJob(){
        int selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            ComponentName serviceName = new ComponentName(getPackageName(),NotificationJobSchedule.class.getName());
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName).setRequiredNetworkType(selectedNetworkOption).setPeriodic(60*60*1000);
            JobInfo myJobInfo = builder.build();
            int resultCode = mScheduler.schedule(myJobInfo);
            if(resultCode == JobScheduler.RESULT_SUCCESS){
                Log.i("JobSchedule","Job Scheduled");
            }

            boolean constraintSet = selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE;
       /* if(constraintSet) {
            //Schedule the job and notify the user
            mScheduler.schedule(myJobInfo);
            Toast.makeText(this, "Job Scheduled, job will run when " +
                    "the constraints are met.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Please set at least one constraint",
                    Toast.LENGTH_SHORT).show();
        }*/
        }
    }
    private void checkUpdate() {
        if(detector.haveNetworkConnection()) {
            txtStatus.setText(getString(R.string.status_checkVer));
            // progressBar.setProgress(10);
            /*activityRequest.getVersion(new ActivityRequest.VolleyCallVer() {
                @Override
                public void onSuccess(int version) {
                    if(version==versionDB.getData()) {*/
            progressBar.setProgress(100);
          //  login();
            txtStatus.setText(getString(R.string.status_activity));
            activityRequest.getActivity(new ActivityRequest.VolleyCallActivity() {
                @Override
                public void onSuccess() {
                    login();
                }
            },progressBar);
        }
                   /* else {
                        //versionDB.updateData(version);
                        txtStatus.setText(getString(R.string.status_activity));
                        activityRequest.getActivity(new ActivityRequest.VolleyCallActivity() {
                            @Override
                            public void onSuccess() {
                                login();
                            }
                        },progressBar);
                    }*/



        else{
            snackbar = Snackbar.make(linearLayout,getString(R.string.error_internet),Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkUpdate();
                }
            });
            snackbar.show();
        }
    }
    private void login() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(preferences.getInt("id",0)!=0){
            startActivity(new Intent(this,MenuActivity.class));
            finish();
        }
        else{

            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
    }


}
