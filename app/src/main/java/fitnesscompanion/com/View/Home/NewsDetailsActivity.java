package fitnesscompanion.com.View.Home;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import fitnesscompanion.com.R;


public class
NewsDetailsActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar loader;
    String url = "";

    //load content of website
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetails);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        loader = findViewById(R.id.loader);
        webView = findViewById(R.id.webView);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {
            
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loader.setVisibility(View.VISIBLE);
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                loader.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(url);

    }
}