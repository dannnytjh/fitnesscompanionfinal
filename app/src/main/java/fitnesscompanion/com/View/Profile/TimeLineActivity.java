package fitnesscompanion.com.View.Profile;

import android.content.Intent;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.R;
import fitnesscompanion.com.View.Home.MenuActivity;
import fitnesscompanion.com.View.Profile.Graph.HorizontalBarChartFragment;

public class TimeLineActivity extends AppCompatActivity {
    @BindView(R.id.spinnerMode) Spinner spinnerMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        getSupportActionBar().setTitle(getResources().getStringArray(R.array.menu_profile)[3]);
        ButterKnife.bind(this);

        spinnerMode.setOnItemSelectedListener(onItemSelectedListener);
//        spinnerOption.setOnItemSelectedListener(onItemSelectedListener);
    }

    Spinner.OnItemSelectedListener onItemSelectedListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
           //transaction.replace(R.id.frameLayout,new HorizontalBarChartFragment(getApplicationContext(),spinnerMode.getSelectedItemPosition())).commit();
            switch (spinnerMode.getSelectedItemPosition()) {

                case 0:

                    transaction.replace(R.id.frameLayout,new StepFragment(getApplicationContext())).commit();
                    break;
                case 1:

                    transaction.replace(R.id.frameLayout,new HorizontalBarChartFragment(getApplicationContext(),spinnerMode.getSelectedItemPosition())).commit();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


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
