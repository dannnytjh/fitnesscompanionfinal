package fitnesscompanion.com.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.ActivityDB;
import fitnesscompanion.com.Model.Activity;
import fitnesscompanion.com.R;
import fitnesscompanion.com.View.Home.MenuActivity;

/**
 * Created by Soon Kok Fung
 */
public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.txtDesc) TextView txtDesc;
    @BindView(R.id.txtTime) TextView txtTime;
    @BindView(R.id.txtCal) TextView txtCal;

    private int activityNo;
    private ActivityDB activityDB;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        activityDB = new ActivityDB(this);
        activityNo = getIntent().getIntExtra("no",0);
        activity=activityDB.getActivity(activityNo);
        getSupportActionBar().setTitle(activity.getName());
        if(activity.getImage()!=null) {
            imageView.setImageBitmap(activity.getImageFromJSon());
        }

        txtDesc.setText(activity.getDescription());
        txtCal.setText(String.valueOf(activity.getCalories())+" cal");
        txtTime.setText(String.valueOf(activity.getTime()));
    }
    public void onPlay(View view) {
        finish();
        startActivity(new Intent(this,CountDownActivity.class).putExtra("no",activityNo));

    }
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,MenuActivity.class).putExtra("index",1));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
