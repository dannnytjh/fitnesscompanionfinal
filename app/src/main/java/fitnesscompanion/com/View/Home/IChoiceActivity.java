package fitnesscompanion.com.View.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.choicemmed.a30.A30BLEService;
import com.choicemmed.a30.BleConst;
import com.choicemmed.a30.BleService;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.HealthProfileDB;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.HealthProfileRequest;

/**
 * Created by Soon Kok Fung
 */
public class IChoiceActivity extends AppCompatActivity {

    private static final String PWD = "password";
    private static final String A30_PREFERENCE = "A30sp";
    private static final String SERVICEUUID = "serviceUUID";
    @BindView(R.id.txtStatus) TextView txtStatus;
    @BindView(R.id.txtBattery)TextView txtBattery;
    @BindView(R.id.btnFind) Button btnFind;
    @BindView(R.id.btnLink) Button btnLink;
    @BindView(R.id.btnUnlink) Button btnUnlink;
    private Intent service;
    private A30BLEService a30bleService;
    private int REQUEST_ENABLE_BT = 0x101; // 蓝牙开关返回值
    private Receiver receiver;

    private String serviceCompare;
    private String passwordCompare;
    private HealthProfileDB healthProfileDB;

    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    private Boolean syncAlready=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ichoice);
        getSupportActionBar().setTitle("My Device");
        ButterKnife.bind(this);
        healthProfileDB = new HealthProfileDB(this);
        preferences = getSharedPreferences(A30_PREFERENCE, Context.MODE_PRIVATE);
        serviceCompare = preferences.getString(SERVICEUUID, null);
        passwordCompare = preferences.getString(PWD, null);

        System.out.println(serviceCompare);
        System.out.println(passwordCompare);

        if(serviceCompare!=null) {
            btnFind.setVisibility(View.GONE);
            btnLink.setVisibility(View.VISIBLE);
            btnUnlink.setVisibility(View.VISIBLE);
        }

        service = new Intent(this, BleService.class);
        startService(service);
        a30bleService = A30BLEService.getInstance(this);
        registerBroadcast();

    }

    public void onFindDevice(View view){
        linkDevice();
        System.out.println("find");
    }
    public void onLinkDevice(View view) {
        linkDevice();
        System.out.println("link");
    }
    public void onUnLinkDevice(View view) {
        System.out.println("unlink");

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Are You Sure Want To Unpair Device ?");
        dialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(passwordCompare!=null) {
                    syncAlready=false;

                    btnFind.setVisibility(View.VISIBLE);
                    btnLink.setVisibility(View.GONE);
                    btnUnlink.setVisibility(View.GONE);

                    txtStatus.setText("Disconnected");
                    txtBattery.setText("-");
                    preferences.edit().putString(PWD, null).commit();
                    preferences.edit().putString(SERVICEUUID, null).commit();
                    Toast.makeText(getApplicationContext(),"Success unpair",Toast.LENGTH_LONG).show();
                }

            }
        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogBuilder.show();
    }

    private void linkDevice() {
        if (passwordCompare == null) {
            if(!syncAlready){
                progressDialog = ProgressDialog.show(this, "Finding device", "Please Wait.....", true);
                a30bleService.didFindDeivce();
            }
        }
        else {
            progressDialog = ProgressDialog.show(this, "Sync Data", "Please Wait.....", true);
            a30bleService.didLinkDevice(serviceCompare,passwordCompare);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (Activity.RESULT_OK == resultCode) {
                linkDevice();
            }
        }
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConst.SF_ACTION_DEVICE_CONNECTED_STATE_CONNECTED);
        filter.addAction(BleConst.SF_ACTION_DEVICE_CONNECTED_STATE_DISCONNECTED);
        filter.addAction(BleConst.SF_ACTION_DEVICE_CONNECTED_FAILED);
        filter.addAction(BleConst.SF_ACTION_DEVICE_CONNECTED_SUCCESS);
        filter.addAction(BleConst.SF_ACTION_DEVICE_RETURNDATA_DEVICEID);
        filter.addAction(BleConst.SF_ACTION_DEVICE_RETURNDATA_SERVICEID);
        filter.addAction(BleConst.SF_ACTION_DEVICE_RETURNDATA);
        filter.addAction(BleConst.SF_ACTION_DEVICE_RETURNDATA_STEP);
        filter.addAction(BleConst.SF_ACTION_OPEN_BLUETOOTH);
        filter.addAction(BleConst.SF_ACTION_SEND_PWD);
        filter.addAction(BleConst.SF_ACTION_DEVICE_HISDATA);
        receiver = new Receiver();
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        stopService(service);
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

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
         //   a30bleService.didSetExerciseTarget(new GoalDB(getApplicationContext()).getCurrentStep());
            String extra = intent.getStringExtra("DATA");
            switch (intent.getAction()) {
                case BleConst.SF_ACTION_DEVICE_RETURNDATA:
                    if (extra.contains("密码审核成功")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    a30bleService.didGetDeviceBattery();
                                    a30bleService.didGetVersion();
                                    a30bleService.didGetDeviceID();
                                    a30bleService.didGetTime();
                                    a30bleService.didGetHistoryDate();
                                    syncAlready=true;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();
                            }
                        }).start();
                    }
                    else if (extra.contains("Level")) {
                        String[] battery = extra.split(":");
                        System.out.println(battery[1]);
                        txtBattery.setText(battery[1]);
                    }
                    else if (extra.contains("Device ID")) {
                        String[] deviceID = extra.split(":");
                    }
                    else if (extra.contains("Version")) {
                        String[] versionNumber = extra.split(":");
                    }
                    else if ((extra + "").contains("失败")) {
                        Toast.makeText(getApplicationContext(),"Fail to connect",Toast.LENGTH_LONG).show();
                    }
                    break;
                case BleConst.SF_ACTION_OPEN_BLUETOOTH:// 打开蓝牙操作
                    progressDialog.dismiss();
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    break;
                case BleConst.SF_ACTION_SEND_PWD:
                    preferences.edit().putString(PWD, extra).commit();
                    passwordCompare =extra;
                    break;
                case BleConst.SF_ACTION_DEVICE_RETURNDATA_SERVICEID:
                    preferences.edit().putString(SERVICEUUID, extra).commit();
                    serviceCompare =extra;
                    break;
                case BleConst.SF_ACTION_DEVICE_RETURNDATA_STEP:
                    int step = Integer.parseInt(extra);
                    System.out.println(step);
                  /*  if(step>=new GoalDB(getApplicationContext()).getCurrentStep()){
                        new Notification(getApplicationContext(),getString(R.string.status_step));
                    }*/

                    if(healthProfileDB.checkRecord()==0) {
                        new HealthProfileRequest(getApplicationContext()).insertStep(step);
                        //healthProfileDB.insertStep(new HealthProfile(step));
                    }
                    else{
                        if(healthProfileDB.getCurrentStep()!=step){
                            healthProfileDB.updateStep(step);
                            new HealthProfileRequest(getApplicationContext()).updateStep(step);
                        }
                    }
                    break;
            }
            btnFind.setVisibility(View.GONE);
            btnLink.setVisibility(View.VISIBLE);
            btnUnlink.setVisibility(View.VISIBLE);
            txtStatus.setText("Connected");
        }
    }
}
