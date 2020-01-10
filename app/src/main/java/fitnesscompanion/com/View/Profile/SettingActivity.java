package fitnesscompanion.com.View.Profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Adapter.MenuAdapter;
import fitnesscompanion.com.R;
import fitnesscompanion.com.View.Home.MenuActivity;
import fitnesscompanion.com.View.LoginActivity;

/**
 * Created by Soon Kok Fung
 */
public class SettingActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static GoogleApiClient googleApiClient;
    @BindView(R.id.listViewMenu) ListView listViewMenu;
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            switch (position) {
                case 0:
                    new ChangePassDialog(SettingActivity.this, new ChangePassDialog.Check() {
                        @Override
                        public void onSuccess(boolean check) {
                            if(check) {
                                finish();
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().commit();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        }
                    }).show(getFragmentManager(),"Change Password");
                    break;
                case 1:
                    finish();
                    startActivity(new Intent(getApplicationContext(),AboutUsActivity.class));
                    break;
                case 2:
                    new signOutFragment().show(getFragmentManager(),"Sign Out");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Setting");
        ButterKnife.bind(this);
//        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).
//                addApi(Auth.GOOGLE_SIGN_IN_API)
//                .addApi(Fitness.HISTORY_API)
//                .addApi(Fitness.RECORDING_API)
//                .addApi(Fitness.CONFIG_API)
//                .build();
        listViewMenu.setAdapter(new MenuAdapter(this, getResources().getStringArray(R.array.menu_setting)));
        listViewMenu.setOnItemClickListener(onItemClickListener);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //Sign Out Dialog
    @SuppressLint("ValidFragment")
    public static class signOutFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.status_signOut)
                    .setPositiveButton(R.string.signOut, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
//                            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
//                                    new ResultCallback<Status>() {
//                                        @Override
//                                        public void onResult(Status status) {
//
//                                        }
//                                    });
//                            PendingResult<Status> statusPendingResult = Fitness.ConfigApi.disableFit(googleApiClient);
                            getActivity().finish();
                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().commit();

                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //dismiss();
                        }
                    });

            return builder.create();
        }
    }
}
