package fitnesscompanion.com.View.Home;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.R;
import fitnesscompanion.com.Util.DashedCircularProgress;

/**
 * Created by Soon Kok Fung
 */
@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {
    @BindView(R.id.stepProgress) DashedCircularProgress stepProgress;
    @BindView(R.id.txtStep) TextView txtStep;
    @BindView(R.id.runningMap) RelativeLayout runningMap;
    @BindView(R.id.bmistatistic) RelativeLayout bmibutton;
    @BindView(R.id.newsfeed) RelativeLayout newsbutton;
    @BindView(R.id.assignActivity) RelativeLayout assignbutton;

    private Context context;

    private Thread detectorTimeStampUpdaterThread;

    private Handler handler;

    private boolean isRunning = true;
    private long timestamp;


    public HomeFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);

        /*stepProgress.setMax(new GoalDB(context).getCurrentStep());*/
        stepProgress.setValue(MenuActivity.step);
        txtStep.setText(String.valueOf(MenuActivity.step));

        runningMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MapsActivity.class);
                startActivity(intent);
            }
        });

        bmibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BmiActivity.class);
                startActivity(intent);
            }
        });
        newsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,NewsActivity.class);
                startActivity(intent);
            }
        });

        assignbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AssignedActivity.class);
                startActivity(intent);
            }
        });

//        bMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent (context, )
//            }
//        });


        //registerForSensorEvents();
        //setupDetectorTimestampUpdaterThread();
        return rootView;
    }
    /*private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            getActivity().finish();
            switch (position) {
                case 0:
                    startActivity(new Intent(context,IChoiceActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(context,BmiActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(context,GoalActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(context,AssignedActivity.class));
                    break;
                    /*
                case 4:
                    startActivity(new Intent(context,AssignedActivity.class));
                    break;*/
          /* }
        }
    };*/
    /*
    public void registerForSensorEvents() {
        SensorManager sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // Step Counter
        sManager.registerListener(new SensorEventListener() {

                                      @Override
                                      public void onSensorChanged(SensorEvent event) {
                                          float steps = event.values[0];
                                          stepProgress.setValue((int)steps);
                                          viewpercent.setText(String.valueOf((int)steps));
                                      }

                                      @Override
                                      public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                      }
                                  }, sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_UI);

        // Step Detector
        sManager.registerListener(new SensorEventListener() {

                                      @Override
                                      public void onSensorChanged(SensorEvent event) {
                                          // Time is in nanoseconds, convert to millis
                                          timestamp = event.timestamp / 1000000;
                                      }

                                      @Override
                                      public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                      }
                                  }, sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                SensorManager.SENSOR_DELAY_UI);
    }
    private void setupDetectorTimestampUpdaterThread() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        detectorTimeStampUpdaterThread = new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        Thread.sleep(5000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        detectorTimeStampUpdaterThread.start();
    }
    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
        detectorTimeStampUpdaterThread.interrupt();
    }*/

}
