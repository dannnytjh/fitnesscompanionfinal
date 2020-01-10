package fitnesscompanion.com.View.Home;

import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DailyTotalResult;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.HealthProfileDB;
import fitnesscompanion.com.R;
import fitnesscompanion.com.Util.BottomNavigationViewHelper;
import fitnesscompanion.com.View.Activity.ActivityFragment;

import fitnesscompanion.com.View.Food.FoodImage;
import fitnesscompanion.com.View.Profile.ProfileFragment;


/**
 * Created by Soon Kok Fung
 */
public class MenuActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String PWD = "password";
    private static final String A30_PREFERENCE = "A30sp";
    private static final String SERVICEUUID = "serviceUUID";
    @BindView(R.id.navigation) BottomNavigationView navigation;
    @BindView(R.id.frameLayout) FrameLayout frameLayout;
    private GoogleApiClient mGoogleApiClient;
    private int[] navigationIndex ={R.id.navigation_home,R.id.navigation_activity,
            R.id.navigation_food,R.id.navigation_profile};
    private int backButtonCount=0;
    private HealthProfileDB healthProfileDB;
    private SharedPreferences preferences;
    private String serviceCompare;
    private String passwordCompare;
    private boolean isShow = false;
    private static final int JOB_ID = 0;
    public static int step;
    JobScheduler mScheduler;

   /* private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            new GetStep().execute();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(0);
                    break;
                case R.id.navigation_activity:
                    loadFragment(1);
                    break;
                case R.id.navigation_food:
                    loadFragment(2);
                    break;
                case R.id.navigation_profile:
                    loadFragment(3);
                    break;
            }

            return true;
        }
    };
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        healthProfileDB = new HealthProfileDB(this);

        preferences = getSharedPreferences(A30_PREFERENCE, Context.MODE_PRIVATE);
        serviceCompare = preferences.getString(SERVICEUUID, null);
        passwordCompare = preferences.getString(PWD, null);

        //connecting with google fit
//       mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Fitness.HISTORY_API)
//                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
//                .addConnectionCallbacks(this)
//                .enableAutoManage(this, 0, this)
//                .build();


        navigation.setSelectedItemId(navigationIndex[getIntent().getIntExtra("index",0)]);
       // loadFragment(getIntent().getIntExtra("index",0));


    }
private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
//            new GetStep().execute();
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    loadFragment(0);
                    break;
                case R.id.navigation_activity:
                    loadFragment(1);
                    break;
                case R.id.navigation_food:
                    loadFragment(2);
                    break;
                case R.id.navigation_profile:
                    loadFragment(3);
                    break;
            }

            return true;
        }
    };
    public void loadFragment(int index){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (index) {
            case 0:
//                new GetStep().execute();
                transaction.replace(R.id.frameLayout,new HomeFragment(this)).commit();
                break;
            case 1:
                transaction.replace(R.id.frameLayout,new ActivityFragment(this)).commit();
                break;
            case 2:
                transaction.replace(R.id.frameLayout,new FoodImage(this)).commit();
                break;
            case 3:
                transaction.replace(R.id.frameLayout,new ProfileFragment(this)).commit();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if(backButtonCount >= 1) {
            Intent intent = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            System.exit(0);
        }
        else
        {
            Toast.makeText(this,getString(R.string.status_exit), Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("HistoryAPI", "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("HistoryAPI", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("HistoryAPI", "onConnectionFailed");
    }
    private int  displayStepDataForToday() {
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal( mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA ).await(1, TimeUnit.MINUTES);
        return showDataSet(result.getTotal());
    }
    private int showDataSet(DataSet dataSet) {

            step = 0;
            Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
            if (!dataSet.isEmpty()) {
                DataPoint dp = dataSet.getDataPoints().get(0);
                Field field = dp.getDataType().getFields().get(0);
                step = dp.getValue(field).asInt();
            }
            System.out.println("Current Step :" + step);
           // System.out.println("Goal Step :" + new GoalDB(this).getCurrentStep());

            /*if (new HealthProfileDB(this).getCurrentStep() >= new GoalDB(this).getCurrentStep() & !isShow) {
                isShow = true;
                new Notification(this, getString(R.string.status_step));
            }*/
           /* if (serviceCompare == null & passwordCompare == null) {
                if (healthProfileDB.checkRecord() == 0) {
                    healthProfileDB.insertStep(new HealthProfile(step));
                    new HealthProfileRequest(this).insertStep(step);
                } else {
                    if (healthProfileDB.getCurrentStep() != step) {
                        healthProfileDB.updateStep(step);
                        new HealthProfileRequest(this).updateStep(step);
                    }
                }
            }*/


        return step;
    }
    private class GetStep extends AsyncTask<Void,Void,Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            return displayStepDataForToday();
        }
    }

}
