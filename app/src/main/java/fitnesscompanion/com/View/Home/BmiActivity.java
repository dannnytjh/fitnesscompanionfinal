package fitnesscompanion.com.View.Home;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.HealthProfileDB;
import fitnesscompanion.com.Database.UserDB;
import fitnesscompanion.com.Model.HealthProfile;
import fitnesscompanion.com.Model.User;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */
public class BmiActivity extends AppCompatActivity {
    @BindView(R.id.txtBmi) TextView txtBmi;
    @BindView(R.id.txtBmiDesc) TextView txtBmiDesc;

    private String[] statusArray;
    private UserDB userDB;
    private User user;
    private int weight;
    private double height;
    private HealthProfile health;
    private HealthProfileDB healthProfileDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        getSupportActionBar().setTitle("Body mass index");
        ButterKnife.bind(this);
        userDB = new UserDB(this);
        healthProfileDB = new HealthProfileDB(this);
        statusArray = getResources().getStringArray(R.array.bmi_status);
        user = userDB.getData();
        health = healthProfileDB.getData();
        weight = health.getWeight();
        height = health.getHeight();
        //user.setWeight(healthProfileDB.getCurrentWeight());
        DecimalFormat format = new DecimalFormat("0.0");
        txtBmi.setText(format.format(health.getBmi()));
        //txtBmi.setText(String.valueOf(health.getBmi()));
        txtBmiDesc.setText(statusArray[health.getBmiString()]);
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,MenuActivity.class).putExtra("index",0));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
