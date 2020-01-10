package fitnesscompanion.com.View.Food;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import fitnesscompanion.com.R;
import fitnesscompanion.com.View.Food.ui.main.tab2;

public class FoodList extends AppCompatActivity {

    ListView listView;

    ArrayList<Food> list;
    FoodListAdapter adapter = null;
    private Bitmap bitmap;
    Cursor cursor;
    ImageView imageViewFood;
    private String mealDate;
    int mealType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new FoodListAdapter(this, R.layout.food_list_items, list);
        listView.setAdapter(adapter);

        //get custom query sqlite, ignore default value as always 0
        mealType = this.getIntent().getIntExtra("mealType",0);
        mealDate = this.getIntent().getStringExtra("mealDate");

        //get all data from SQLite
        if(mealType == 0) {
             cursor = UploadFoodActivity.sqLiteHelper.getData1("SELECT * FROM FOOD1");
        }
        //query according to date and meal type
        else if (mealType == 1)
        {
            cursor = tab2.sqLiteHelper.getData1("SELECT * FROM FOOD1 WHERE mealType = 'Breakfast'"+" AND date = '"+ mealDate + "'");
        }
        else if (mealType == 2)
        {
            cursor = tab2.sqLiteHelper.getData1("SELECT * FROM FOOD1 WHERE mealType = 'Lunch'"+" AND date = '"+ mealDate + "'");
        }
        else if (mealType == 3)
        {
            cursor = tab2.sqLiteHelper.getData1("SELECT * FROM FOOD1 WHERE mealType = 'Dinner'"+" AND date = '"+ mealDate + "'");
        }
        else if (mealType == 4)
        {
            cursor = tab2.sqLiteHelper.getData1("SELECT * FROM FOOD1 WHERE mealType = 'Others'"+" AND date = '"+ mealDate + "'");
        }


        list.clear();
        while (cursor.moveToNext()) {

            //cursor to retrieve data from table col
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String mealType = cursor.getString(2);
            String date = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            list.add(new Food(id, name, mealType, date, image));
        }
        adapter.notifyDataSetChanged();
    }
}

//
//        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                CharSequence[] items = {"Update", "Delete"};
//                final AlertDialog.Builder dialog = new AlertDialog.Builder(FoodList.this);
//
//                dialog.setTitle("Choose an action");
//                dialog.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0)
//                        {
//                           // update
//                            Cursor cursor = UploadFoodActivity.sqLiteHelper.getData("SELECT id FROM FOOD");
//                            ArrayList<Integer> arrayId = new ArrayList<Integer>();
//
//                            while (cursor.moveToNext())
//                            {
//                                arrayId.add(cursor.getInt(0));
//
//                            }
//
//
//                            showDialogUpdate(FoodList.this, arrayId.get(position));
//
//
//                        }else{
//                            //delete
//                            Toast.makeText(getApplicationContext(), "Delete...", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    }
//                });
//                dialog.show();
//                return true;
//            }
//        });
//    }
//
//    private void showDialogUpdate(Activity activity, final int position)
//    {
//        final Dialog dialog = new Dialog(activity);
//        dialog.setContentView(R.layout.update_food_list_items);
//        dialog.setTitle("Update Food Image Details");
//
//        imageViewFood = (ImageView) dialog.findViewById(R.id.updateimgFood);
//        final EditText etName = (EditText)dialog.findViewById(R.id.etFoodName);
//        final EditText etPrice = (EditText) dialog.findViewById(R.id.etFoodPrice);
//        Button bUpdate = (Button) dialog.findViewById(R.id.bUpdate);
//
//        //set dialog width & height
//        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
//        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
//        dialog.getWindow().setLayout(width, height);
//
//        dialog.show();
//
//        imageViewFood.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //req image
//                editImage();
//
//            }
//        });
//
//        bUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    UploadFoodActivity.sqLiteHelper.updateData(
//                            etName.getText().toString().trim(),
//                            etPrice.getText().toString().trim(),
//                            UploadFoodActivity.imageViewToByte(imageViewFood),
//                            position
//                    );
//                    dialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
//
//                }
//                catch (Exception error){
//                    Log.e("Update error" , error.getMessage());
//                }
//                updateFoodList();
//
//            }
//        });
//
//    }
//    private void updateFoodList()
//    {
//        //get data from SQLite
//        Cursor cursor = UploadFoodActivity.sqLiteHelper.getData("SELECT * FROM FOOD");
//        list.clear();
//        while (cursor.moveToNext()){
//            int id = cursor.getInt(0);
//            String name = cursor.getString(1);
//            String price = cursor.getString(2);
//            byte[] image = cursor.getBlob(3);
//
//            list.add(new Food( id, name, price, image));
//        }
//        adapter.notifyDataSetChanged();
//
//    }
//
//    private void editImage() {
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//        builder.setTitle(getResources().getString(R.string.addPhoto)).setItems(getResources().
//                getStringArray(R.array.photoArray), new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int option) {
//                if (option == 0) {
//
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takePictureIntent, 0);
//                    }
//
//                } else if (option == 1) {
//                    startActivityForResult(new Intent(Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
//                }
//            }
//        });
//        android.app.AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
//
//            Bundle extras = data.getExtras();
//            bitmap = (Bitmap) extras.get("data");
//            imageViewFood.setImageBitmap(bitmap);
//            imageViewFood.setVisibility(View.VISIBLE);
//
//
//        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
//
//            Uri path = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
//                imageViewFood.setImageBitmap(bitmap);
//                imageViewFood.setVisibility(View.VISIBLE);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//}
