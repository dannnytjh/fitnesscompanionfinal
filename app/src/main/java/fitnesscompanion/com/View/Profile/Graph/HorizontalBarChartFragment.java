package fitnesscompanion.com.View.Profile.Graph;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.HealthProfileDB;
import fitnesscompanion.com.R;
import fitnesscompanion.com.Util.MyXAxisVaueFormatter;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class HorizontalBarChartFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.txtYear) TextView txtYear;
    @BindView(R.id.horizontalBarChart) HorizontalBarChart horizontalBarChart;
    @BindView(R.id.btnBack) ImageButton btnBack;
    @BindView(R.id.btnGo) ImageButton btnGo;

    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_OAUTH = 1;
    private int totalSteps;
    ArrayList<BarEntry> step1 = new ArrayList<>();
   // private StepUpdateListener stepUpdateListener;

    private Context context;
    private int mode;
    private int year;
    private int minYear;

    public HorizontalBarChartFragment(Context context,int mode) {
        this.context=context;
        this.mode=mode;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        year = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_horizontal_bar_chart, container, false);
        ButterKnife.bind(this, rootView);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.CONFIG_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        txtYear.setText(String.valueOf(year));
        txtTitle.setText(" Data");
        btnBack.setOnClickListener(onClickListener);
        btnGo.setOnClickListener(onClickListener);
        //GetStepsCount();
        barChartController();

        return rootView;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    txtYear.setText(String.valueOf(--year));
                    btnGo.setVisibility(View.VISIBLE);
                    break;
                case R.id.btnGo:
                    txtYear.setText(String.valueOf(++year));
                    if(year==Calendar.getInstance().get(Calendar.YEAR)){
                        btnGo.setVisibility(View.GONE);
                    }
                    break;
            }
           barChartController();

        }
    };
    private void barChartController() {

        horizontalBarChart.invalidate();
        horizontalBarChart.clear();
        horizontalBarChart.fitScreen();

        horizontalBarChart.setDrawBarShadow(false);
        horizontalBarChart.setDrawValueAboveBar(true);
        horizontalBarChart.getDescription().setEnabled(false);
        horizontalBarChart.getLegend().setEnabled(false);
        horizontalBarChart.setPinchZoom(false);
        horizontalBarChart.setDrawGridBackground(false);
        horizontalBarChart.setFitBars(true);
        horizontalBarChart.animateY(2500);

        horizontalBarChart.setData(getBarData());

        XAxis xAxis = horizontalBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new MyXAxisVaueFormatter(getResources().getStringArray(R.array.monthArray)));
        xAxis.setLabelCount((getResources().getStringArray(R.array.monthArray)).length-1);

        YAxis left = horizontalBarChart.getAxisLeft();
        left.setDrawAxisLine(true);
        left.setDrawGridLines(true);
        left.setAxisMinimum(0f);
        left.setGranularity(1f);

        YAxis right = horizontalBarChart.getAxisRight();
        right.setDrawAxisLine(true);
        right.setDrawGridLines(true);
        right.setAxisMinimum(0f);
        right.setGranularity(1f);

    }
    private ArrayList<BarEntry> getStepData() {
        ArrayList<BarEntry> barEntrie = new HealthProfileDB(context).getAllYearStep(String.valueOf(year));
        if(barEntrie.size()==0) {
            Toast.makeText(context,"no",Toast.LENGTH_LONG).show();
        }
        return barEntrie;
    }
    private void GetStepsCount(){
            Calendar cal = Calendar.getInstance();

            Date now = new Date();
            cal.setTime(now);
            long endTime = cal.getTimeInMillis();
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            long startTime = cal.getTimeInMillis();
            new getStepsCount(mGoogleApiClient,startTime,endTime).execute();

            //new GetStepsCountTask(mGoogleApiClient, startTime, endTime, this).execute();

        }

    private ArrayList<BarEntry> getCaloriesData() {
        ArrayList<BarEntry> barEntrie = new HealthProfileDB(context).getAllYearCalories(String.valueOf(year));
        if(barEntrie.size()==0) {
            Toast.makeText(context,"no",Toast.LENGTH_LONG).show();
        }
        return barEntrie;
    }
    private BarData getBarData() {
        BarDataSet set=null;

        switch (mode) {
            case 0:

               // set = new BarDataSet(getStepData(),"My Step Data Set");
                Log.i("Google","arraysize : "+step1.size());
               set = new BarDataSet(step1,"My Step Data Set");
                for(int i =0;i<step1.size();i++){
                    Log.i("Google","in for loop : "+step1.get(i).toString());
                }
                break;
            case 1:
                set = new BarDataSet(getCaloriesData(), "My Calories Data Set");
        }

        BarData barData = new BarData(set);
        barData.setValueTextSize(12f);
        barData.setValueFormatter(new DefaultValueFormatter(0));

        return barData;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
       // GetStepsCount();

        //barChartController();
        //getStepData();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Google", "Google client: onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        try {
            connectionResult.startResolutionForResult(getActivity(), REQUEST_OAUTH);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
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

    class getStepsCount extends AsyncTask<Void, Void, Integer> {

        private long startTime, endTime;
        private GoogleApiClient mGoogleApiClient;

        public getStepsCount(GoogleApiClient client,long startTime, long endTime) {
            this.mGoogleApiClient = client;
        this.startTime = startTime;
        this.endTime = endTime;

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
        public ArrayList<BarEntry> showDataSet(DataSet dataSet) {

            int steps = 0;
            for (DataPoint dp : dataSet.getDataPoints()) {
                for (Field field : dp.getDataType().getFields()) {

                    steps = dp.getValue(field).asInt();
                    step1.add(new BarEntry(dp.getValue(field).asInt()-1,steps));

                    addSteps(steps);
                }
            }

            return step1;
        }
    }
}
