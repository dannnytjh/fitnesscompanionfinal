package fitnesscompanion.com.View.Profile;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.R;

public class PrivacyActivity extends AppCompatActivity {
@BindView(R.id.webview)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        getSupportActionBar().setTitle("Privacy Policy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        webView.loadUrl("http://i2hub.tarc.edu.my:8886/FitnessCompanion/View/Home/privacyPolicy.html");
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,AboutUsActivity.class));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
