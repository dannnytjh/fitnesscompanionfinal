package fitnesscompanion.com.Util;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */
public class BarCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(CaptureActivityPortrait.class);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.initiateScan();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String barCode=null;
        if (result != null) {
            barCode = result.getContents();
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        Log.i("Here","Barcode : "+barCode);
        Intent intent=new Intent();
        intent.putExtra("Barcode",barCode);
        setResult(2,intent);
        finish();
    }

}
