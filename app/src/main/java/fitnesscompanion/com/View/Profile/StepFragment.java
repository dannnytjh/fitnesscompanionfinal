package fitnesscompanion.com.View.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class StepFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_OAUTH = 1;
    @BindView(R.id.tvSteps)
    TextView txtSteps;
    @BindView(R.id.txtFrom)
    TextView txtFrom;
    @BindView(R.id.txtTo)
    TextView txtTo;
    private int totalSteps;
    private Context context;
    long endTime,startTime;

    public StepFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public StepFragment(Context context){
        this.context = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepFragment.
     */

    public static StepFragment newInstance(String param1, String param2) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this,rootView);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.CONFIG_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        return rootView;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("Google", "Google client: onConnected");
        // Disable login menu
        // Enable logout menu
        getStepsCount();
    }
    private void getStepsCount() {
        // If no last time stamp previously store in shared preference, means application opened first time so need to get last 30 days totalSteps count from Google FIT.
        // If last time stamp previously store in shared preference, means application opened next time so need to get totalSteps count from Google FIT from last time stamp to till now.
        Calendar cal = Calendar.getInstance();

        Date now = new Date();
        cal.setTime(now);
         endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
         startTime = cal.getTimeInMillis();
        new getStepsCount(mGoogleApiClient,startTime,endTime).execute();

    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OAUTH) {
            if (resultCode == RESULT_OK) {
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("Google", "Google client: RESULT_CANCELED");

            }
        }
    }

    public void onStepUpdate(int steps, long startTime, long endTime) {

        txtSteps.setVisibility(View.VISIBLE);

        txtSteps.setText(String.valueOf(steps));

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        txtFrom.setVisibility(View.VISIBLE);
        txtFrom.setText(dateFormat.format(startTime));

        txtTo.setVisibility(View.VISIBLE);
        txtTo.setText(dateFormat.format(endTime));

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    class getStepsCount extends AsyncTask<Void, Void, Integer> {

        private long startTime, endTime;
        private GoogleApiClient mGoogleApiClient;

        public getStepsCount(GoogleApiClient client,long startTime, long endTime) {
            this.mGoogleApiClient = client;
            this.startTime = startTime;
            this.endTime = endTime;

        }
        @Override
        protected void onPostExecute(Integer steps) {
            super.onPostExecute(steps);
            // Store #endTime in shared preferene for next time use.
            onStepUpdate(steps, startTime, endTime);

        }
        @Override
        protected Integer doInBackground(Void... voids) {
            return getStepsCountFromFIT(startTime,endTime);
        }
        private int getStepsCountFromFIT(long startDateMillis, long endDateMillis) {
            DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                    .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                    .setType(DataSource.TYPE_DERIVED)
                    .setStreamName("estimated_steps")
                    .setAppPackageName("com.google.android.gms")
                    .build();

            DataReadRequest readRequest = new DataReadRequest.Builder()
                    .aggregate(ESTIMATED_STEP_DELTAS, DataType.AGGREGATE_STEP_COUNT_DELTA)
                    .bucketByTime(1, TimeUnit.DAYS)
                    .setTimeRange(startDateMillis, endDateMillis, TimeUnit.MILLISECONDS)
                    .build();

            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);


            //Used for aggregated data
            if (dataReadResult.getBuckets().size() > 0) {
                Log.i("Google", "Get Buckets");
                for (Bucket bucket : dataReadResult.getBuckets()) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet dataSet : dataSets) {
                        showDataSet(dataSet);
                    }
                }
            }
            //Used for non-aggregated data
            else if (dataReadResult.getDataSets().size() > 0) {
                Log.i("Google", "Get DataSet");
                for (DataSet dataSet : dataReadResult.getDataSets()) {
                    showDataSet(dataSet);
                }

            } else {
                Log.i("Google", "No history found for this user");
            }

            Log.i("Google","Total steps : "+totalSteps);
            return totalSteps;
        }
        private void addSteps(int step) {
            Log.e("Google", step + "");
            totalSteps += step;

          /*  for(int i=0;i<10;i++){
                Log.i("Here","Steps : "+step1.get(i));
            }*/
        }
        private void showDataSet(DataSet dataSet) {

            int steps = 0;
            for (DataPoint dp : dataSet.getDataPoints()) {
                for (Field field : dp.getDataType().getFields()) {

                    steps = dp.getValue(field).asInt();
                    //step1.add(new BarEntry(dp.getValue(field).asInt()-1,steps));

                    addSteps(steps);
                }
            }

        }
    }
}
