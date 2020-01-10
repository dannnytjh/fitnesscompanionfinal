package fitnesscompanion.com.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.R;
import fitnesscompanion.com.View.Home.MenuActivity;

/**
 * Created by Soon Kok Fung
 */
public class CountDownActivity extends AppCompatActivity {

    @BindView(R.id.txtCount) TextView txtCount;
    private TextToSpeech textToSpeech;
    private int activityNo;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        activityNo = getIntent().getIntExtra("no",0);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
        new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textToSpeech.speak(getString(R.string.ready),TextToSpeech.QUEUE_FLUSH, null);
                if(millisUntilFinished<4000) {
                    txtCount.setText(String.valueOf((millisUntilFinished)/1000));
                    textToSpeech.speak(String.valueOf((millisUntilFinished)/1000),TextToSpeech.QUEUE_FLUSH, null);
                }
            }

            @Override
            public void onFinish() {

                txtCount.setText(getString(R.string.letStart));
                textToSpeech.speak(getString(R.string.letStart),TextToSpeech.QUEUE_FLUSH, null);
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        textToSpeech.stop();
                        finish();
                        startActivity(new Intent(getApplicationContext(),ExerciseActivity.class).putExtra("no",activityNo));
                    }
                }, 1500);
            }
        }.start();
    }
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,MenuActivity.class).putExtra("index",1));
    }
    @Override
    protected void onDestroy() {

        if(timer!=null) {
            timer.cancel();
            timer.purge();
            timer= null;
        }
        textToSpeech.stop();
        textToSpeech.shutdown();
        super.onDestroy();
    }
}
