package fitnesscompanion.com.View.Food;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fitnesscompanion.com.Model.Activity;
import fitnesscompanion.com.R;
import fitnesscompanion.com.View.Home.MenuActivity;
import fitnesscompanion.com.View.Profile.ReminderActivity;

import static java.security.AccessController.getContext;

public class UploadFoodActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageToUpload;
    RelativeLayout bChooseImage, bList, bMeal, bDate;
    EditText name;
    TextView mealType;
    Button bUploadImage;
    Bitmap bitmap;
    int traineeId;
String date;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public static SQLiteHelper sqLiteHelper;

    private String UploadUrl = "http://i2hub.tarc.edu.my:8886/FitnessCompanion/ImageUploadApp/updateinfo.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_food);
        traineeId = this.getIntent().getIntExtra("traineeId",0);
        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);

        bUploadImage = (Button) findViewById(R.id.bUploadImage);
        bChooseImage = (RelativeLayout) findViewById(R.id.bChooseImage);
        bList = (RelativeLayout) findViewById(R.id.bList);
        bMeal = (RelativeLayout) findViewById(R.id.bMeal);
        bDate = (RelativeLayout) findViewById(R.id.bDate);

        name = (EditText) findViewById(R.id.name);
        mealType = (TextView) findViewById(R.id.mealType);

        bUploadImage.setOnClickListener(this);
        bChooseImage.setOnClickListener(this);
        bList.setOnClickListener(this);
        bMeal.setOnClickListener(this);
        bDate.setOnClickListener(this);

        mealType.setOnClickListener(this);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(this);

        sqLiteHelper = new SQLiteHelper(this, "FoodDB.sqlite", null, 10);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS FOOD1 (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR (200), mealType VARCHAR (200), date VARCHAR (200), image BLOB)");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bMeal:
                //select meal
                mealtype();
                break;

            case R.id.bChooseImage:
                //choose img to upload
                editImage();
                break;

            case R.id.bUploadImage:
                //upload to server
                uploadImage();
                //upload to sqlite local storage
                uploadImageSQL();
                break;

            case R.id.bList:
                //img archive
                Intent intent = new Intent (UploadFoodActivity.this, FoodList.class);
                startActivity(intent);
                break;

            case R.id.bDate:
                //select date
                datepicker();
                break;

        }

    }

    private void datepicker(){
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);

        //style and design
        DatePickerDialog dialog = new DatePickerDialog(
                UploadFoodActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        //get datepicker text and format
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month +1;
                    Log.d("OnDateSet", "OnDateSet: dd/mm/yyyy: " + dayOfMonth + "/" + month + "/" + year);
                    if(month<10 && dayOfMonth<10)
                    {
                        date = year + "-" + "0"+month + "-" + "0"+dayOfMonth;

                    }
                    else if(month < 10)
                    {
                        date = year + "-" + "0"+month + "-" + dayOfMonth;

                    }
                    else if (dayOfMonth<10)
                    {
                        date = year + "-" + month + "-" + "0"+dayOfMonth;

                    }
                    else {
                        date = year + "-" + month + "-" + dayOfMonth;

                    }
                    mDisplayDate.setText(date);

                }
            });
        }
    }

    //select meal type
    private void mealtype(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(getResources().getString(R.string.mealType)).setItems(getResources().
                getStringArray(R.array.dietOption), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    mealType.setText("Breakfast");

                } else if (which == 1) {
                    mealType.setText("Lunch");

                } else if (which == 2) {
                    mealType.setText("Dinner");

                } else if (which == 3) {
                    mealType.setText("Others");

                }
            }

        });
        AlertDialog dialog = build.create();
        dialog.show();
    }

    //camera dialog and runtime permission
    private void editImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.addPhoto)).setItems(getResources().
                getStringArray(R.array.photoArray), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int option) {
                if (option == 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(UploadFoodActivity.this, new String[]{Manifest.permission.CAMERA}, 10);

                        }
                        else {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePictureIntent, 0);
                        }
                    }


                } else if (option == 1) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //ask for camera permission, if yes enable camera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, 0);
            }
        }
    }

    //display picture on img view for uploading
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {

            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");

            imageToUpload.setImageBitmap(bitmap);
            imageToUpload.setVisibility(View.VISIBLE);


        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);

                imageToUpload.setImageBitmap(bitmap);
                imageToUpload.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //upload to SQLite method
    private void uploadImageSQL() {
        try {
            sqLiteHelper.insertData(
                    name.getText().toString().trim(),
                    mealType.getText().toString().trim(),
                    mDisplayDate.getText().toString().trim(),
                    imageViewToByte(imageToUpload)

            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //quality of sqlite pic, do not exceed <50
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //upload to server method
    private void uploadImage()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            Toast.makeText(UploadFoodActivity.this,Response, Toast.LENGTH_LONG).show();
                            imageToUpload.setImageResource(0);
                            imageToUpload.setVisibility(View.GONE);
                            name.getText().clear();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

            //put data, img to server
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name", name.getText().toString().trim());
                params.put("image",imageToString(bitmap));
                params.put("traineeId",String.valueOf(traineeId));
                params.put("mealType",mealType.getText().toString().trim());

                return params;


            }
        };
        MySingleton.getInstance(UploadFoodActivity.this).addToRequestQue(stringRequest);
    }


    //quality of server pic
        private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    //return to food fragment
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,MenuActivity.class).putExtra("index",2));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

