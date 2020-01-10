package fitnesscompanion.com.Util;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.awareness.snapshot.DetectedActivityResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.awareness.Awareness;

import fitnesscompanion.com.R;
import fitnesscompanion.com.View.MainActivity;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobSchedule extends JobService {
    private ActivityRecognitionClient mActivityRecognitionClient;
    private Intent mJobService;
    NotificationManager mNotifyManager;
    PendingIntent contentPendingIntent;
    private GoogleApiClient mGoogleApiClient;

    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    public void createNotificationChannel() {

        // Define notification manager object.
        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Job Service notification",
                            NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifications from Job Service");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        createNotificationChannel();
        contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
       /* NotificationCompat.Builder builder = new NotificationCompat.Builder
                (this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Job Service")
                .setContentText("Your Job ran to completion!")
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.logo_drawer)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
        mNotifyManager.notify(0, builder.build());*/
        mGoogleApiClient = new GoogleApiClient.Builder(NotificationJobSchedule.this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
        getUserActivity();
        return false;
    }

    //detect the activity of the user
    private void getUserActivity() {
        Awareness.getSnapshotClient(this).getDetectedActivity()
                .addOnSuccessListener(new OnSuccessListener<DetectedActivityResponse>(){

                    @Override
                    public void onSuccess(DetectedActivityResponse dar) {
                        ActivityRecognitionResult ar = dar.getActivityRecognitionResult();
                        DetectedActivity probableActivity = ar.getMostProbableActivity();
                        Log.i("Awareness", probableActivity.toString());
                        int activityName = probableActivity.getType();
                        int confidence = probableActivity.getConfidence();
                        //Still
                        if(activityName == 3 && confidence >=75){

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationJobSchedule.this, PRIMARY_CHANNEL_ID)
                                    .setContentText("Take me out for a walk !")
                                    .setContentIntent(contentPendingIntent)
                                    .setSmallIcon(R.drawable.logo_drawer)
                                    .setAutoCancel(true)
                                    .setContentTitle("Walk");
                            mNotifyManager.notify(0, builder.build());
                        }
                        //Walking
                       /* else if(activityName == 7  && confidence >=75){
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationJobSchedule.this, PRIMARY_CHANNEL_ID)
                                    .setContentText("Keep walking !")
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Walk");
                            mNotifyManager.notify(0, builder.build());
                        }*/

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Awareness", "Could not get the current activity.");
                    }
                });


    }
    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
