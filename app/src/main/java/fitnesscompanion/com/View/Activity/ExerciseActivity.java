package fitnesscompanion.com.View.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.ActivityDB;
import fitnesscompanion.com.Database.ActivityDataDB;
import fitnesscompanion.com.Database.HealthProfileDB;
import fitnesscompanion.com.Database.UserDB;
import fitnesscompanion.com.Model.Activity;
import fitnesscompanion.com.Model.ActivityData;
import fitnesscompanion.com.Model.HealthProfile;
import fitnesscompanion.com.Model.User;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.ActivityRequest;
import fitnesscompanion.com.View.Home.MenuActivity;
import com.github.anastr.speedviewlib.AwesomeSpeedometer;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Soon Kok Fung
 */
public class ExerciseActivity extends AppCompatActivity implements LocationListener {

    @BindView(R.id.chronometer) Chronometer chronometer;
    @BindView(R.id.btnControl) ImageButton btnControl;
    @BindView(R.id.txtCalories) TextView txtCalories;
    @BindView(R.id.imageView) ImageView imageView;

    private ActivityDB activityDB;
    private Activity activity;
    private HealthProfileDB healthProfileDB;
    private HealthProfile health;
    private ActivityDataDB activityDataDB;
    private ActivityRequest activityRequest;
    private User user;
    private UserDB userDB;
    private int weight;
    private float mySpeed;
    private long timeWhenStopped = 0;
    private float walkingMin = (float) 1.0; //maybe 2.0
    private float runMin = (float) 5.0;
    public static int MY_PERMISSIONS_REQUEST_LOCATION =1;
    private long startTime,pause;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager lm;
    Location crntLocation,newLocation;
    Toast mToast;

    private boolean isPlay = true;
    private double burned = 0,distance =0;
    private int activityNo;
    private Chronometer.OnChronometerTickListener onChronometerTickListener = new Chronometer.OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {

            //startTime=SystemClock.elapsedRealtime();
           /* double burned = ((double) activity.getCalories() / 60.0) *
                    (double) getSec(SystemClock.elapsedRealtime() - chronometer.getBase());*/
           //Running
           if(activityNo == 2) {
               if(mySpeed >= runMin) {
                 //  chronometer.start();
                   chronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
                   //chronometer.start();
                  // burned = (((double) activity.getCalories() / 60.0) * weight) + (((mySpeed * mySpeed) / (height / 100)) * 0.029 * weight) * (double) getSec(SystemClock.elapsedRealtime() - chronometer.getBase());
                  // burned = (0.1 * mySpeed +1.8*mySpeed+3.5) *weight* (double) getSec(SystemClock.elapsedRealtime() - chronometer.getBase()) * 60 * 5/1000;

                  /* burned = ((double) activity.getCalories() / 60.0) *
                           (double) getSec(SystemClock.elapsedRealtime() - chronometer.getBase());*/
                   burned = distance * weight * 1.036;
                   //Log.i("Weight","Weight : "+healthProfileDB.getCurrentWeight());
                   Log.i("Weight", "Speed : " + mySpeed);
               }
               else{

                  // chronometer.stop();
                  // timeWhenStopped=SystemClock.elapsedRealtime() - chronometer.getBase();
                   if(mToast != null){
                       mToast.cancel();
                   }
                  mToast=  Toast.makeText(ExerciseActivity.this,"Are u running?",Toast.LENGTH_SHORT);
                   mToast.show();
                  // chronometer.setBase(SystemClock.elapsedRealtime());
               }
           }
           //Walking
           else if(activityNo == 4){
               if(mySpeed >= walkingMin) {
                   //burned = (((double) activity.getCalories() / 60.0) * weight) + (((mySpeed * mySpeed) / (height / 100)) * 0.029 * weight) * (double) getSec(SystemClock.elapsedRealtime() - chronometer.getBase());
                   //burned = (0.1 * mySpeed +1.8*mySpeed+3.5) *weight* (double) getSec(SystemClock.elapsedRealtime() - chronometer.getBase()) * 60 * 5/1000;
                   /*chronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
                   chronometer.start();*/
                 /* burned = ((double) activity.getCalories() / 60.0) *
                           (double) getSec(SystemClock.elapsedRealtime() - chronometer.getBase());*/
                   //Log.i("Weight","Weight : "+healthProfileDB.getCurrentWeight());
                   //get the location

                   //burned calories

                   burned = distance * weight * 1.036;
                   Log.i("Weight", "Speed : " + mySpeed);
               }
               else{
                   //chronometer.stop();
                   burned = 0;
                  // timeWhenStopped=SystemClock.elapsedRealtime() - chronometer.getBase();
                    if(mToast != null){
                        mToast.cancel();
                    }
                   mToast = Toast.makeText(ExerciseActivity.this,"Are u walking?",Toast.LENGTH_SHORT);
                   mToast.show();
               }
           }
           else {
                burned = ((double) activity.getCalories() / 60.0) *
                       (double) getSec(SystemClock.elapsedRealtime() - chronometer.getBase());
           }
            if ((int) burned == 0) {
                txtCalories.setText("--");
            } else {
                txtCalories.setText(String.valueOf((int) burned));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        ButterKnife.bind(this);
        activityDataDB = new ActivityDataDB(this);
        activityRequest = new ActivityRequest(this);
        activityNo = getIntent().getIntExtra("no",0);
        activityDB= new ActivityDB(this);
        activity = activityDB.getActivity(activityNo);
        getSupportActionBar().setTitle(activity.getName());
        if(activity.getImage()!=null) {
            imageView.setImageBitmap(activity.getImageFromJSon());

        }

        healthProfileDB = new HealthProfileDB(this);
        health = healthProfileDB.getData();
        userDB = new UserDB(this);
        user = userDB.getData();
        weight = health.getWeight();
        Log.i("Weight","Weight : "+health.getWeight());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            crntLocation = new Location("crntlocation");
                            crntLocation.setLatitude(location.getLatitude());
                            crntLocation.setLongitude(location.getLongitude());
                            Log.i("Weight", " Current Latitude : " + location.getLatitude() + "Longitude : " + location.getLongitude());
                        }
                    }
                });
        checkLocationPermission();
     //   checkLocationDistance();
//        weight = healthProfileDB.getCurrentWeight();
        //Log.i("Weight","Weight : "+healthProfileDB.getCurrentWeight());

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setOnChronometerTickListener(onChronometerTickListener);
        chronometer.animate();
        chronometer.start();


    }

    public void checkLocationPermission(){
     lm= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

        ActivityCompat.requestPermissions(ExerciseActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);

        Toast.makeText(ExerciseActivity.this,"Error",Toast.LENGTH_LONG).show();
        return;
    }
    LocationRequest mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(10000);
    mLocationRequest.setFastestInterval(5000);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest);
    SettingsClient client = LocationServices.getSettingsClient(this);
    Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
    task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
        @Override
        public void onComplete(Task<LocationSettingsResponse> task) {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                // All location settings are satisfied. The client can initialize location
                // requests here.
            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                    ExerciseActivity.this,
                                    100);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        } catch (ClassCastException e) {
                            // Ignore, should be an impossible error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            }
        }
    });
    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

   // this.onLocationChanged(null);
}
    public void onClick(View view){
        if(isPlay) {
            btnControl.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
            isPlay=false;
            chronometer.stop();
            timeWhenStopped=SystemClock.elapsedRealtime() - chronometer.getBase();
            isPlay=false;
            startTime=SystemClock.elapsedRealtime();
            chronometer.stop();
        }
        else{
            btnControl.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
            isPlay=true;
          //  chronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
            chronometer.setBase(chronometer.getBase()+ SystemClock.elapsedRealtime()-startTime);
            //chronometer.setBase(chronometer.getBase());
            chronometer.start();
        }
    }
    public void onEnd(View view) {
        stopDialog();
    }
    public void onBackPressed() {
        stopDialog();
    }
    public void stopDialog() {
        isPlay=false;
        startTime=SystemClock.elapsedRealtime();
        chronometer.stop();

        AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);
        builder.setMessage(getString(R.string.status_stopActivity))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{
                            int cal = Integer.parseInt(txtCalories.getText().toString());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(SystemClock.elapsedRealtime()-chronometer.getBase());
                            int sec = (int)getSec(startTime - chronometer.getBase());
                            Log.i("Weight","Sec : "+sec);
                            Log.i("Weight","Distance : "+distance);
                            ActivityData activityData = new ActivityData();
                            activityData.setHr(0);
                            activityData.setDuration(sec);
                            activityData.setDistance(distance);
                            activityData.setActivityNo(activityNo);

                            activityDataDB.insertData(activityData);
                            activityRequest.addActivityData(new ActivityRequest.VolleyCallData() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(ExerciseActivity.this,getString(R.string.status_save),Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(ExerciseActivity.this,MenuActivity.class).putExtra("index",1));
                                }
                            },activityData);

                          /*  if(new ActivityDataDB(getApplicationContext()).getCurrentBurn()>=new GoalDB(getApplicationContext()).getCurrentCal()) {
                                new fitnesscompanion.com.Util.Notification(getApplicationContext(),getString(R.string.status_cal));
                            }*/

                        }catch(NumberFormatException e) {
                            Log.i("Here","Error exercise : "+e.getMessage());
                            Toast.makeText(ExerciseActivity.this,getString(R.string.status_noSave),Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(ExerciseActivity.this,MenuActivity.class).putExtra("index",1));
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isPlay=true;
                        chronometer.setBase(chronometer.getBase()+ SystemClock.elapsedRealtime()-startTime);
                        chronometer.start();
                    }
                })
                .show();

    }

    private long getSec(long millis) {
        return TimeUnit.MILLISECONDS.toSeconds(millis);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {

            AwesomeSpeedometer speedometer1 = (AwesomeSpeedometer) findViewById(R.id.awesomeSpeedometer);
            mySpeed = location.getSpeed();
            float nCurrentSpeed = location.getSpeed() * 3.6f;
            speedometer1.setWithTremble(false);
            speedometer1.speedTo(nCurrentSpeed);
            Location newLocation=new Location("newlocation");
            newLocation.setLatitude(location.getLatitude());
            newLocation.setLongitude(location.getLongitude());
            //calculate distance
           distance =crntLocation.distanceTo(newLocation) / 1000;
           /* if(activityNo == 1) {
                if (mySpeed >= runMin) {
                    chronometer.start();
                }
                else {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }
            }*/
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    protected void onPause() {
        if(mToast != null) {
            mToast.cancel();
        }

        super.onPause();
    }
}
