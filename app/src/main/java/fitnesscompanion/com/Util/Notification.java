package fitnesscompanion.com.Util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

import fitnesscompanion.com.R;
//import fitnesscompanion.com.View.Home.GoalActivity;
import fitnesscompanion.com.View.Home.NewGoalActivity;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

/**
 * Created by Soon Kok Fung
 */

public class Notification {
    private Context context;
    private String msg;

    public Notification(Context context,String msg) {
        this.context=context;
        this.msg=msg;
        sendNotification();
    }

    private void sendNotification() {
        Intent intent = new Intent(context, NewGoalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo_drawer)
                        .setContentTitle("Congratulations")
                        .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setContentText(msg);



        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());

    }
}
