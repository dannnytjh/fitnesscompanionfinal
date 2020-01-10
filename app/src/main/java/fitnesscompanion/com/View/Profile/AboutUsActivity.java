package fitnesscompanion.com.View.Profile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.ActivityVersionDB;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */
public class AboutUsActivity extends AppCompatActivity {

    @BindView(R.id.txtVer) TextView txtVer;
    @BindView(R.id.txtVerActivity) TextView txtVerActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setTitle(getResources().getStringArray(R.array.menu_setting)[1]);
        ButterKnife.bind(this);
        try {
            txtVer.setText(getPackageManager().getPackageInfo(getPackageName(),0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        txtVerActivity.setText(String.valueOf((double)new ActivityVersionDB(this).getData()));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.privacy_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_privacy:
                finish();
                startActivity(new Intent(this,PrivacyActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,SettingActivity.class));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
