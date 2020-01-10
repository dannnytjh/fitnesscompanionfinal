package fitnesscompanion.com.View.Profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.HealthProfileDB;
import fitnesscompanion.com.Database.UserDB;
import fitnesscompanion.com.Model.HealthProfile;
import fitnesscompanion.com.Model.User;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.HealthProfileRequest;
import fitnesscompanion.com.ServerRequest.UserRequest;
import fitnesscompanion.com.Util.ConnectionDetector;
import fitnesscompanion.com.Util.ImageScale;
import fitnesscompanion.com.Util.SeekBarPicker;
import fitnesscompanion.com.View.Home.MenuActivity;

/**
 * Created by Soon Kok Fung
 */
public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.imageProfile) ImageView imageProfile;
    @BindView(R.id.txtName) TextView txtName;
    @BindView(R.id.txtGender) TextView txtGender;
    @BindView(R.id.txtDob) TextView txtDob;
    @BindView(R.id.txtAge) TextView txtAge;
    @BindView(R.id.txtAddress) TextView txtAddress;
    @BindView(R.id.txtEmail) TextView txtEmail;
    @BindView(R.id.txtHeight) TextView txtHeight;
    @BindView(R.id.txtWeight) TextView txtWeight;
    @BindView(R.id.txtBmi) TextView txtBmi;

    private User user;
    private UserDB userDB;
    private HealthProfile health;
    private HealthProfileDB healthProfileDB;
    private HealthProfileRequest healthProfileRequest;
    private UserRequest userRequest;
    private ConnectionDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle(getString(R.string.title_profile));
        ButterKnife.bind(this);
        userDB = new UserDB(this);
        healthProfileDB = new HealthProfileDB(this);
        healthProfileRequest = new HealthProfileRequest(this);
        userRequest = new UserRequest(this);
        detector= new ConnectionDetector(this);
        user = userDB.getData();
        health = healthProfileDB.getData();
      //  health.setWeight(healthProfileDB.getCurrentWeight());
        setData();
    }
    private void setData() {
        if(user.getImage().length()>10) {
            imageProfile.setImageBitmap(user.getImageFromJSon());
        }
        txtName.setText(user.getName());

        txtGender.setText(getResources().getStringArray(R.array.genderArray)[user.getGender()]);
        txtDob.setText(user.getDob());
        txtAge.setText(String.valueOf(user.getAge()));
        txtEmail.setText(user.getEmail());
        txtHeight.setText(String.valueOf(health.getHeight()));
        txtWeight.setText(String.valueOf(health.getWeight()));
        txtAddress.setText(user.getAddress());
        txtBmi.setText(getResources().getStringArray(R.array.array_bmi)[health.getBmiString()]);
    }
    public void onEdit(View view) {
        switch (view.getId()) {
            case R.id.imageProfile:
                editImage();
                break;
            case R.id.btnName:
                editName();
                break;
            case R.id.btnGender:
                editGender();
                break;
            //case R.id.btnDob:
               // editDob();
               // break;
            case R.id.btnHeight:
                editHeight();
                break;
            case R.id.btnWeight:
                editWeight();
                break;
            case R.id.btnAddress:
                editAddress();
                break;
        }
    }
    private void editName() {
        UpdateNameDialog updateName = new UpdateNameDialog(this, new UpdateNameDialog.GetName() {
            @Override
            public void onSuccess(String name) {
                if(detector.isConnected()){
                    user.setName(name);
                    userDB.updateName(name);
                    userRequest.updateName(name);
                    setData();
                }
            }
        });
        updateName.show(getFragmentManager(),"Update Name");

    }
    private void editGender() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.gender))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.setSingleChoiceItems(getResources().getStringArray(R.array.genderArray),user.getGender(),new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(detector.isConnected()) {
                    user.setGender(which);
                    userDB.updateGender(which);
                    userRequest.updateGender(which);
                    setData();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    /*private void editDob() {
        DatePicker datePicker = new DatePicker(this, new DatePicker.GetDate() {
            private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            @Override
            public void onSuccess(Date date) {

                if(detector.isConnected()) {
                    user.setDob(dateFormat.format(date));
                    userDB.updateDob(user.getDob());
                    userRequest.updateDob(user.getDob());
                    setData();
                }
            }
        });
        datePicker.setSelectedDate(user.getDob());
        datePicker.setMaxDate(new Date());
        datePicker.show(getFragmentManager(),"Dob Picker");

    }*/
    private void editHeight() {
        SeekBarPicker seekBarPicker = new SeekBarPicker(this, new SeekBarPicker.GetValue() {
            @Override
            public void onSuccess(int value) {
                if(detector.isConnected()) {
                    txtHeight.setText(String.valueOf(value));
                    healthProfileDB.updateHeight(value);
                    //userRequest.updateHeight(value);
                    healthProfileRequest.insertHealthRecord(new HealthProfileRequest.VolleyCall() {
                        @Override
                        public void onSuccess() {
                        }
                    }, userDB.getData().getId(), value, health.getWeight());
                }
            }
        });
        seekBarPicker.show(getFragmentManager(),"Height Picker");
        seekBarPicker.setMax(250);
        seekBarPicker.setTitle(getString(R.string.editHeight));
        seekBarPicker.setUnit(getString(R.string.cm));
        seekBarPicker.setMin(10);
      //  seekBarPicker.setValue(health.getHeight());

    }
    private void editWeight() {
        final int weight = health.getWeight();
        SeekBarPicker seekBarPicker = new SeekBarPicker(this, new SeekBarPicker.GetValue() {
            @Override
            public void onSuccess(int value) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(detector.isConnected()) {
                    HealthProfile healthProfile = new HealthProfile();
                    //healthProfile.setRate(value);
                    health.setWeight(value);
                    healthProfileDB.insertWeight(healthProfile);
                    healthProfileRequest.insertHealthRecord(new HealthProfileRequest.VolleyCall() {
                        @Override
                        public void onSuccess() {
                            setData();
                        }
                    }, userDB.getData().getId(), health.getHeight(), value);
                }
            }
        });


                    /*if(new GoalDB(getApplicationContext()).getCurrentWeight()>=weight){
                        if(new GoalDB(getApplicationContext()).getCurrentWeight()<=health.getWeight())
                            new Notification(getApplicationContext(),getString(R.string.status_weight));
                    }
                    if(new GoalDB(getApplicationContext()).getCurrentWeight()<=weight){
                        if(new GoalDB(getApplicationContext()).getCurrentWeight()>=health.getWeight())
                            new Notification(getApplicationContext(),getString(R.string.status_weight));
                    }*/
        seekBarPicker.show(getFragmentManager(),"Weight Picker");
        seekBarPicker.setMax(250);
        seekBarPicker.setTitle(getString(R.string.editWeight));
        seekBarPicker.setUnit(getString(R.string.kg));
        seekBarPicker.setMin(10);
        seekBarPicker.setValue(health.getWeight());
    }
    private void editImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.addPhoto)).setItems(getResources().
                getStringArray(R.array.photoArray),new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0) {
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
                }
                else if(which==1){
                    startActivityForResult(new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),1);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void editAddress(){
        UpdateAddressDialog updateAddress = new UpdateAddressDialog(this, new UpdateAddressDialog.GetAddress() {
            @Override
            public void onSuccess(String address) {
                if(detector.isConnected()){
                    user.setAddress(address);
                    userDB.updateAddress(address);
                    userRequest.updateAddress(address);
                    setData();
                }
            }
        });
        updateAddress.show(getFragmentManager(),"Update Address");
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean isImage = false;
        Bitmap bitmapProfile=null;
        ImageScale scale = new ImageScale();

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            isImage = true;
            bitmapProfile= scale.getResizedBitmap(bitmap, 400);
        }
        else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            InputStream imageStream = null;
            isImage = true;
            Uri uri = data.getData();
            try {
                imageStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            bitmapProfile=scale.getResizedBitmap(selectedImage, 400);
        }

        if(isImage) {
            if(detector.isConnected()) {
                user.encodeImagetoString(bitmapProfile);
                userDB.updateImage(user.getImage());
                userRequest.updateImage(user.getImage());
                setData();
            }
        }
    }
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
