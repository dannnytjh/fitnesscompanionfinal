package fitnesscompanion.com.View;

import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Birthday;
import com.google.api.services.people.v1.model.Date;
import com.google.api.services.people.v1.model.Gender;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.HealthProfileDB;
import fitnesscompanion.com.Database.UserDB;
import fitnesscompanion.com.Model.HealthProfile;
import fitnesscompanion.com.Model.User;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.UserRequest;
import fitnesscompanion.com.Util.ConnectionDetector;
import fitnesscompanion.com.Util.Listener;
import fitnesscompanion.com.Util.Validation;
import fitnesscompanion.com.View.Home.MenuActivity;

/**
 * Created by Soon Kok Fung
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.editEmail) EditText editEmail;
    @BindView(R.id.editPass)
    EditText editPass;
    @BindView(R.id.layoutEmail)
    TextInputLayout layoutEmail;
    @BindView(R.id.layoutPass)
    TextInputLayout layoutPass;
    SignInButton btnGoogleSignIn;

    // Google Sign In boolean
    Boolean googleSignIn = true;

    private Validation validation;
    private ConnectionDetector detector;
    private User user;
    private UserRequest userRequest;
    private UserDB userDB;
    private HealthProfileDB healthDB;
    private String birthDate,gender,name, email;
    private int backButtonCount = 0;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    static final private int RC_SIGN_IN = 1;
    static final private String TAG = "hole";
    private WeakReference<LoginActivity> weakAct = new WeakReference<>(this);
    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        userDB = new UserDB(this);
        healthDB = new HealthProfileDB(this);
        validation = new Validation(this);
        detector = new ConnectionDetector(this);
        userRequest = new UserRequest(this);

        new Listener(this).setListenerEmail(editEmail, layoutEmail);
        new Listener(this).setListenerPass(editPass, layoutPass);

        editPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_DONE:
                        signIn();
                        break;
                }
                return false;
            }
        });
        //google sign in
        btnGoogleSignIn = (SignInButton) findViewById(R.id.sign_in_google_button);
        btnGoogleSignIn.setColorScheme(SignInButton.COLOR_DARK);
        btnGoogleSignIn.setOnClickListener(this);

        // Google Sign In == true
//        if (googleSignIn) {
//            GoogleSignIn();
//        }
    }

//    private void GoogleSignIn() {
//        googleSignIn = false;
//        Scope myScope = new Scope("https://www.googleapis.com/auth/user.birthday.read");
//        Scope myScope2 = new Scope(Scopes.PLUS_ME);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestScopes(myScope, myScope2)
//                .requestEmail()
//                .build();
//        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).
//                addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_google_button:
                onGoogleSignIn();
        }
    }
    private boolean validation() {
        return validation.checkEmail(editEmail, layoutEmail) &
                validation.checkPassword(editPass, layoutPass);
    }

    private void signIn() {
        if (validation() && detector.isConnected()) {

            userRequest.loginIn(new UserRequest.VolleyCallCheckActive() {
                @Override
                public void onSuccess(int active) {
                    switch (active) {
                        case 2:
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.status_active), Toast.LENGTH_LONG).show();
                            break;
                        case 0:
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.status_loginFail), Toast.LENGTH_LONG).show();
                            break;
                        case 1:

                            userRequest.getUser(new UserRequest.VolleyCallgetUser() {
                                @Override
                                public void onSuccess() {
                                    User user = userDB.getData();
                                    HealthProfile health = healthDB.getData();
                                    Log.i("Here","Height : "+health.getHeight());
                                    if (health.getHeight() != 0.0) {
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                                .edit().putInt("id", user.getId()).commit();

                                        finish();
                                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                                    } else {
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), RecordActivity.class));
                                    }

                                }
                            }, editEmail.getText().toString());
                            break;


                    }
                }
            }, editEmail.getText().toString(), editPass.getText().toString());

        }
    }

    public void onSignIn(View view) {
        signIn();
    }

    public void onGoogleSignIn() {

        //Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        //startActivityForResult(signInIntent, REQ_CODE);
    }

    public void onForget(View view) {

        PassRecovery passRecovery = new PassRecovery(this);
        passRecovery.show(getFragmentManager(), "Password Recovery");
    }

    public void onSignUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            System.exit(0);
        } else {
            Toast.makeText(this, getString(R.string.status_exit), Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQ_CODE) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleResult(result);
//        }

    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            account = result.getSignInAccount();
            name = account.getDisplayName();
            email = account.getEmail();
            userRequest.loginIn(new UserRequest.VolleyCallCheckActive() {
                @Override
                public void onSuccess(int active) {

                    switch (active) {
                        case 2:
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.status_active), Toast.LENGTH_LONG).show();
                            break;
                        case 0:
                            signUpGoogle();
                           /* userRequest.getUser(new UserRequest.VolleyCallgetUser() {
                                @Override
                                public void onSuccess() {
                                    User user = userDB.getData();

                                    HealthProfile health = healthDB.getData();
                                    Log.i("Here","Height : "+health.getHeight());
                                    if (health.getHeight() != 0) {
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                                .edit().putInt("id", user.getId()).commit();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                                    } else {
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), RecordActivity.class));
                                    }

                                }
                            }, email);*/
                            /*User user = userDB.getData();
                            if (user.getHeight() != 0) {
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                        .edit().putInt("id", user.getId()).commit();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                            } else {
                                finish();
                                startActivity(new Intent(getApplicationContext(), RecordActivity.class));
                            }*/
                            break;
                        case 1:

                            userRequest.getUser(new UserRequest.VolleyCallgetUser() {
                                @Override
                                public void onSuccess() {
                                    User user = userDB.getData();
                                    HealthProfile health = healthDB.getData();
                                    if (health.getHeight() != 0) {
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                                .edit().putInt("id", user.getId()).commit();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                                    } else {
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), RecordActivity.class));
                                    }

                                }
                            }, email);
                            break;


                    }
                }
            }, email, "");
           /* nameDisplay.setText(name);
            emailDisplay.setText(email);*/
           /* SharedPreferences sharedPref = getSharedPreferences(account.getId(), MODE_PRIVATE);
            new GetProfileDetails(account, weakAct, TAG).execute();
            if (sharedPref.contains("gender")) {

                printAdvanced();
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.i("BirthDate","Signed out");
                    }
                    });

            } else {
                Log.i("Here", "Don't have SharedPref");
                new GetProfileDetails(account, weakAct, TAG).execute();
            }*/
        }

    }
    private void signUpGoogle(){
        SharedPreferences sharedPref = getSharedPreferences(account.getId(), MODE_PRIVATE);
         new GetProfileDetails(account, weakAct, TAG).execute();
        user = new User();
        user.setName(name);
        user.setImage("");
        user.setEmail(email);
        user.setPassword("");
        user.setAddress("");

        /*userRequest.googleSignUp(user, new UserRequest.VolleyCallAddUser() {
            @Override
            public void onSuccess() {
                finish();
                User user = userDB.getData();
                if (user.getHeight() != 0) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit().putInt("id", user.getId()).commit();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                } else {
                    finish();
                    startActivity(new Intent(getApplicationContext(), RecordActivity.class));
                }

            }
        });*/

    }
    private void signUp(){
        userRequest.signUp(user, new UserRequest.VolleyCallAddUser() {
            @Override
            public void onSuccess() {
                User user = userDB.getData();

                finish();
                startActivity(new Intent(getApplicationContext(), RecordActivity.class));


            }
        });
    }
    private void saveAdvanced(Person meProfile) {
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            SharedPreferences sharedPref = getSharedPreferences(account.getId(), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            List<Gender> genders = meProfile.getGenders();
            if (genders != null && genders.size() > 0) {
                 gender = genders.get(0).getValue();
                Log.i("Here","gender : "+gender);
                editor.putString("gender", gender);
                if(gender.equals("male"))
                    user.setGender(1);
                else
                    user.setGender(2);
            } else {
                Log.d(TAG, "onPostExecute no gender if set to private ");
                editor.putString("gender", ""); //save as main key to know pref saved
            }
            List<Birthday> birthdays = meProfile.getBirthdays();
            if (birthdays != null && birthdays.size() > 0) {
                Log.i("Here","Birthday");
                for (Birthday b : birthdays) { //birthday still able to get even private, unlike gender
                    Date bdate = b.getDate();
                    if (bdate != null) {
                        String bday, bmonth, byear;
                        if (bdate.getDay() != null) bday = bdate.getDay().toString();
                        else bday = "";
                        if (bdate.getMonth() != null) bmonth = bdate.getMonth().toString();
                        else bmonth = "";
                        if (bdate.getYear() != null) byear = bdate.getYear().toString();
                        else byear = "";
                        Log.i("BirthDate","bday : "+bday);
                        Log.i("BirthDate","bmonth : "+bmonth);
                        editor.putString("bday", bday);
                        editor.putString("bmonth", bmonth);
                        editor.putString("byear", byear);
                    }
                }
            } else {
                Log.w("BirthDate", "saveAdvanced no birthday");
            }
            editor.commit();  //next instruction is print from pref, so don't use apply()
        } else {
            Log.w(TAG, "saveAdvanced no acc");
        }
    }
    private void printAdvanced() {
        Log.i("Here","Reached printadvance");
        if(account !=null) {
            SharedPreferences sharedPref = getSharedPreferences(account.getId(), MODE_PRIVATE);
            Log.i("Here","Further printadvance");
            if(sharedPref != null){
                Log.i("BirthDate","Sharedpref is not null");
            }
            if (sharedPref.contains("gender")) { //this checking works since null still saved
                String gender = sharedPref.getString("gender", "");
                //genderDisplay.setText(gender);
                Log.d("BirthDate", "gender: " + gender);
                if (sharedPref.contains("bday")) { //this checking works since null still saved
                    String bday = sharedPref.getString("bday", "");
                    String bmonth = sharedPref.getString("bmonth", "");
                    String byear = sharedPref.getString("byear", "");
                    Log.d("BirthDate", bday + "/" + bmonth + "/" + byear);
                     birthDate = byear + "-" + bmonth + "-" + bday;
                    user.setDob(birthDate);
                    signUp();

                    //birthDisplay.setText(birthDate);
                } else {
                    user.setDob("");
                    signUp();
                    Log.w("BirthDate", "failed to get birthday from pref");
                }
            } else {

                Log.w("BirthDate", "failed ot get data from pref -2");
            }
        }else{
            Log.i("BirthDate","No account");
        }


    }
   /* public void onSignOut(View view){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Log.i("Here","Logout successful");
            }
        });
    }*/

    static class GetProfileDetails extends AsyncTask<Void, Void, Person> {

        private PeopleService ps;
        private int authError = -1;
        private WeakReference<LoginActivity> weakAct;
        private String TAG;

        GetProfileDetails(GoogleSignInAccount account, WeakReference<LoginActivity> weakAct, String TAG) {
            this.TAG = TAG;
            this.weakAct = weakAct;
            //Collection<String> scopes = new ArrayList<>(Collections.singletonList(Scopes.PROFILE));
            GoogleAccountCredential credential =
                    GoogleAccountCredential.usingOAuth2(this.weakAct.get(), Collections.singleton(Scopes.PROFILE));
            credential.setSelectedAccount(
                    new Account(account.getEmail(), "com.google"));
            HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
            JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
            ps = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("Google Sign In Quickstart")
                    .build();
        }

        @Override
        protected com.google.api.services.people.v1.model.Person doInBackground(Void... params) {
            Person meProfile = null;
            try {
                meProfile = ps
                        .people()
                        .get("people/me")
                        .setPersonFields("genders,birthdays")
                        .execute();
            } catch (UserRecoverableAuthIOException e) {
                e.printStackTrace();
                authError = 0;
            } catch (GoogleJsonResponseException e) {
                e.printStackTrace();
                authError = 1;
            } catch (IOException e) {
                e.printStackTrace();
                authError = 2;
            }
            return meProfile;
        }

        @Override
        protected void onPostExecute(com.google.api.services.people.v1.model.Person meProfile) {
            LoginActivity mainAct = weakAct.get();
            if (authError == 1) {
                Log.w("Here", "People API might not enable at" +
                        " https://console.developers.google.com/apis/library/people.googleapis.com/?project=<project name>");
            } else if (authError == 2) {
                Log.w("Here", "API io error");
            } else {
                if (meProfile != null) {
                    Log.i("Here","I'm onpostexecute");
                    mainAct.saveAdvanced(meProfile);
                    mainAct.printAdvanced();
                }
            }
        }
    }

}
