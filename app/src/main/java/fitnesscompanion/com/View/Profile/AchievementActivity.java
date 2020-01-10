package fitnesscompanion.com.View.Profile;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Adapter.AchievementAdapter;
import fitnesscompanion.com.R;
import fitnesscompanion.com.View.Home.MenuActivity;

/**
 * Created by Soon Kok Fung
 */
public class AchievementActivity extends AppCompatActivity {
    @BindView(R.id.listView) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        getSupportActionBar().setTitle(getResources().getStringArray(R.array.menu_profile)[0]);
        ButterKnife.bind(this);
        listView.setAdapter(new AchievementAdapter(this));
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
