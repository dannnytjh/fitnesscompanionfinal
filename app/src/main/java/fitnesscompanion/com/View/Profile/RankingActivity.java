package fitnesscompanion.com.View.Profile;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.ActivityDB;
import fitnesscompanion.com.Model.Activity;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.ActivityRequest;
import fitnesscompanion.com.View.Home.MenuActivity;

/**
 * Created by Soon Kok Fung
 */
public class RankingActivity extends AppCompatActivity {

    @BindView(R.id.activitySpinner) Spinner spinner;
    //@BindView(R.id.rankingList) ListView listView;

    private ActivityDB activityDB;
    private ArrayList<Activity> activityArrayList;
    private ActivityRequest activityRequest;
    //TODO ranking summary like u are in front of 80% users instead of list down
    private Spinner.OnItemSelectedListener onItemSelectedListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            activityRequest.getRanking(new ActivityRequest.VolleyCallRanking() {
                @Override
                public void onSuccess(int ranking) {
                    /*listView.setAdapter(new RankingAdapter(getApplicationContext(), rankings));
                    if (rankings.size() == 0) {
                        Toast.makeText(getApplicationContext(), getString(R.string.status_ranking), Toast.LENGTH_LONG).show();
                    }*/
                    if(ranking == 0){
                        Toast.makeText(getApplicationContext(), "You did not perform any activity yet", Toast.LENGTH_LONG).show();
                        transaction.replace(R.id.frameLayout,new RankingFragment(getApplicationContext(),ranking)).commit();
                    }
                    else {
                        transaction.replace(R.id.frameLayout, new RankingFragment(getApplicationContext(), ranking)).commit();
                        Toast.makeText(getApplicationContext(), "You are " + ranking + "% better than the top user", Toast.LENGTH_LONG).show();
                    }
                }
            }, activityArrayList.get(i).getNo());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(getResources().getStringArray(R.array.menu_profile)[1]);
        activityDB = new ActivityDB(this);
        activityArrayList = activityDB.getAllData();
        activityRequest = new ActivityRequest(this);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,activityDB.getName());
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,MenuActivity.class).putExtra("index",3));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
