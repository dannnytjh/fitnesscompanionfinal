package fitnesscompanion.com.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Model.User;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.UserRequest;
import fitnesscompanion.com.Util.ConnectionDetector;
import fitnesscompanion.com.Util.DatePicker;
import fitnesscompanion.com.Util.ImageScale;
import fitnesscompanion.com.Util.Listener;
import fitnesscompanion.com.Util.Validation;

/**
 * Created by Soon Kok Fung
 */
public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.editName) EditText editName;
    @BindView(R.id.editEmail)  EditText editEmail;
    @BindView(R.id.editAddress) EditText editAddress;
    @BindView(R.id.editPass)  EditText editPass;
    @BindView(R.id.editPass2)  EditText editPass2;
    @BindView(R.id.editDob) EditText editDob;
    @BindView(R.id.imageProfile) ImageView imageProfile;
    @BindView(R.id.rgbGender) RadioGroup rgbGender;

    @BindView(R.id.txtGender) TextView txtGender;
    @BindView(R.id.layoutName) TextInputLayout layoutName;
    @BindView(R.id.layoutEmail)  TextInputLayout layoutEmail;
    @BindView(R.id.layoutPass)  TextInputLayout layoutPass;
    @BindView(R.id.layoutPass2)  TextInputLayout layoutPass2;
    @BindView(R.id.layoutDob)  TextInputLayout layoutDob;

    private Validation validation;
    private Bitmap bitmapProfile;
    private ImageScale imageScale;
    private boolean isImage = false;
    private ConnectionDetector detector;
    private UserRequest userRequest;
    private User user;
    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            checkGender();
        }
    };
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (dobValidation())
                layoutDob.setError(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle(getString(R.string.signUp));
        ButterKnife.bind(this);
        validation = new Validation(this);
        detector = new ConnectionDetector(this);
        userRequest = new UserRequest(this);

        rgbGender.setOnCheckedChangeListener(onCheckedChangeListener);
        new Listener(this).setListenerName(editName,layoutName);
        new Listener(this).setListenerEmail(editEmail,layoutEmail);
        new Listener(this).setListenerPass(editPass,layoutPass);
        new Listener(this).setListenerCPass(editPass2,editPass,layoutPass2);
        editDob.addTextChangedListener(textWatcher);
    }

    private boolean validation() {
        return checkGender() & validation.checkEmail(editEmail,layoutEmail)&
                validation.checkName(editName,layoutName)&
                validation.checkPassword(editPass,layoutPass)&
                validation.checkConfirmPassword(editPass2,editPass,layoutPass2)&dobValidation();
    }

    private boolean dobValidation() {
        layoutDob.setError(null);
        if(editDob.getText().toString()!=null) {
            layoutDob.setError(getString(R.string.error_required));
        }
        return true;
    }

    private boolean checkGender() {
        txtGender.setVisibility(View.GONE);

        if(rgbGender.getCheckedRadioButtonId()==-1){
            txtGender.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    public void signUp() {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        user = new User();
        if(detector.isConnected() & validation()) {
            userRequest.checkMail(new UserRequest.VolleyCallCheckMail() {
                @Override
                public void onSuccess(int count) {
                    switch (count) {
                        case 0:
                            user.setName(editName.getText().toString());
                            user.setEmail(editEmail.getText().toString());
                            user.setPassword(editPass.getText().toString());
                            user.setDob(editDob.getText().toString());
                            user.setAddress(editAddress.getText().toString());

                            if(rgbGender.getCheckedRadioButtonId()==R.id.rbMale)
                                user.setGender(0);
                            else
                                user.setGender(1);


                            if(isImage)
                                user.encodeImagetoString(bitmapProfile);
                            else
                                user.setImage("");

                            userRequest.signUp(user, new UserRequest.VolleyCallAddUser() {
                                @Override
                                public void onSuccess() {
                                    finish();
                                }
                            });

                            break;
                        case 1:
                            editEmail.requestFocus();
                            Toast.makeText(getApplicationContext(),getString(R.string.status_emailUsed),Toast.LENGTH_LONG).show();
                            break;
                    }

                }
            },editEmail.getText().toString());
        }
    }
    public void onSignUp(View view) {
        signUp();
    }
    public void onClear(View view) {
        txtGender.setVisibility(View.GONE);
        imageProfile.setImageDrawable(getResources().getDrawable(R.drawable.logo_drawer));
        editName.setText(null);
        editDob.setText(null);
        editEmail.setText(null);
        editPass.setText(null);
        editPass2.setText(null);
    }
    public void onDateDialog(View view) {

        DatePicker datePicker = new DatePicker(this, new DatePicker.GetDate() {
            @Override
            public void onSuccess(Date date) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                editDob.setText(dateFormat.format(date));
            }
        });
        if(editDob.getText().toString()==null)
            datePicker.setSelectedDate(new Date());
        else
            datePicker.setSelectedDate(editDob.getText().toString());

        datePicker.setMaxDate(new Date());
        datePicker.show(getFragmentManager(),"Date Picker");
        dobValidation();

    }
    public void onPicture(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.addPhoto)).setItems(getResources().
                getStringArray(R.array.photoArray),new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0) {
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),0);
                }
                else if(which==1){
                    startActivityForResult(new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),1);
                }
            }
        });
        builder.show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        isImage = false;
        imageScale = new ImageScale();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            isImage = true;
            bitmapProfile= imageScale.getResizedBitmap(bitmap, 400);
            imageProfile.setImageBitmap(bitmapProfile);
        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            InputStream imageStream = null;
            isImage = true;
            Uri uri = data.getData();
            try {
                imageStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            bitmapProfile=imageScale.getResizedBitmap(selectedImage, 400);
            imageProfile.setImageBitmap(bitmapProfile);

        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
