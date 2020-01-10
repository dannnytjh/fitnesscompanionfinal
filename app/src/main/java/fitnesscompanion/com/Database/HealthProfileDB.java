package fitnesscompanion.com.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import fitnesscompanion.com.Model.HealthProfile;

/**
 * Created by Soon Kok Fung
 */

public class HealthProfileDB {
    private Context context;
    private FitnessDB fitnessDB;
    public HealthProfileDB(Context context){
        this.context=context;
        fitnessDB = new FitnessDB(context);
    }

    public void deteleData() {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        db.delete("HealthProfile",null,null);
        db.close();

    }
  /*  public int getLastWeight() {
        if(getWeightCount()>=2)
            return getPreviousWeight();
        else
            return getCurrentWeight();
    }*/
    public HealthProfile getData(){
        HealthProfile health = new HealthProfile();
        SQLiteDatabase db = fitnessDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM HealthProfile",null);
        if(cursor.moveToFirst()) {
            do{
                health.setNo(cursor.getInt(0));
                health.setWeight(cursor.getInt(1));
                health.setHeight(cursor.getInt(2));

            }while (cursor.moveToNext());
        }
        db.close();
        return health;
    }
    public void insertWeight(HealthProfile healthProfile) {
        insertData(healthProfile);
    }
   /* public void insertStep(HealthProfile healthProfile) {
        healthProfile.setType(1);
        insertData(healthProfile);
    }
    public void insertHeartRate(HealthProfile healthProfile) {
        healthProfile.setType(2);
        insertData(healthProfile);
    }*/
    /*public int getCurrentWeight() {
        return getCurrentData(0);
    }*/
    public void updateHeight(int height) {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("height",height);

        db.update("HealthProfile",contentValues,null,null);
        db.close();
    }
    public void updateWeight(int weight){
        SQLiteDatabase db = fitnessDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("weight",weight);

        db.update("HealthProfile",contentValues,null,null);
        db.close();
    }
    public int getWeight(){
        int weight =0;
        SQLiteDatabase db = fitnessDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT weight from HealthProfile where ",null);
        return weight;
    }
    public int getCurrentStep() {
        int step =0;
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT rate FROM HealthProfile WHERE type = 1"+
                " AND date(created_at)= date('now')",null);

        if(cursor.moveToFirst()) {
            do{
                step = cursor.getInt(0);
            }while (cursor.moveToNext());
        }

        db.close();


        return step;
    }
    public int getCurrentHeartRate() {
        return getCurrentData(2);
    }

    public void updateStep(int step) {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rate",step);
        db.update("HealthProfile",contentValues,"date(created_at)= date('now') AND type=1",null);

        db.close();
    }
    public int checkRecord() {
        int count =0;
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        try{
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM HealthProfile WHERE date(created_at)= date('now') AND type = 1",null);

            if(cursor!=null) {
                cursor.moveToFirst();
            }
            count = cursor.getInt(0);

        }catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();


        return count;
    }
    public int[] getAllWeight() {
        int[] weight =new int[getCount()];
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        try{

        }catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();


        return weight;
    }

    public ArrayList<BarEntry> getAllYearStep(String year) {
        ArrayList<BarEntry> step = new ArrayList<>();
        SQLiteDatabase db = fitnessDB.getReadableDatabase();
        int number=1;

        try{
            for(int x=0;x<13;x++) {
                Cursor cursor = db.rawQuery("SELECT SUM(rate) FROM HealthProfile WHERE type = '"+number+ "' AND " +
                        "strftime('%Y', created_at) = '"+year+"' AND " +
                        "strftime('%m', created_at) = '"+String.format("%02d",x)+"' " +
                        "ORDER BY created_at ASC",null);
                if(cursor.moveToFirst()) {
                    do{
                        if(cursor.getInt(0)!=0) {
                            step.add(new BarEntry(x-1,cursor.getInt(0)));
                        }
                    }while ((cursor.moveToNext()));
                }
            }
        }catch (SQLException e) {
            Log.e("Here","Error : "+e.toString());
            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
        }
        db.close();
        return step;
    }

    public ArrayList<BarEntry> getAllYearCalories(String year) {
        ArrayList<BarEntry> calories = new ArrayList<>();
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        try{
            for(int x=0;x<13;x++) {
                //Cursor cursor = db.rawQuery("SELECT SUM(duration*calories/60) FROM Activity INNER JOIN ActivityData ON activityNo  = Activity.no WHERE " + "strftime('%Y', created_at) = '"+year+"' AND " + "strftime('%m', created_at) = '"+String.format("%02d",x)+"' " + "ORDER BY created_at ASC",null);
                Cursor cursor = db.rawQuery("SELECT SUM(duration*calories/60) FROM Activity INNER JOIN ActivityData ON activityNo  = Activity.no WHERE " + "strftime('%Y', created_at) = '"+year+"' AND " + "strftime('%m', created_at) = '"+String.format("%02d",x)+"' " + "ORDER BY created_at ASC",null);
// Cursor cursor = db.rawQuery("SELECT SUM(heartrate) FROM ActivityData WHERE " + "strftime('%Y', created_at) = '"+year+"' AND " + "strftime('%m', created_at) = '"+String.format("%02d",x)+"' " + "ORDER BY created_at ASC",null);
                if(cursor.moveToFirst()) {
                    do{
                        if(cursor.getInt(0)!=0) {
                            calories.add(new BarEntry(x-1,cursor.getInt(0)));
                        }
                    }while ((cursor.moveToNext()));
                }
            }
        }catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return calories;
    }

    public int getSize(){
        int count =0;
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        try{
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM HealthProfile",null);

            if(cursor!=null) {
                cursor.moveToFirst();
            }
            count = cursor.getInt(0);

        }catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();


        return count;
    }

    public int getCount(){
        int count =0;
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        try{
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM HealthProfile",null);

            if(cursor!=null) {
                cursor.moveToFirst();
            }
            count = cursor.getInt(0);

        }catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        return count;
    }
    private void insertData(HealthProfile healthProfile) {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("no",getLastNo()+1);
        /*contentValues.put("type",healthProfile.getType());
        contentValues.put("rate",healthProfile.getRate());*/

        db.insert("HealthProfile",null,contentValues);
        db.close();
    }
    public void insertData(ArrayList<HealthProfile> healthProfiles) {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int x=0; x<healthProfiles.size();x++) {
            contentValues.put("no",healthProfiles.get(x).getNo());
            contentValues.put("weight",healthProfiles.get(x).getWeight());
            contentValues.put("height",healthProfiles.get(x).getHeight());
           /* contentValues.put("type",healthProfiles.get(x).getType());
            contentValues.put("rate",healthProfiles.get(x).getRate());*/
            contentValues.put("created_at",healthProfiles.get(x).getCreated_at());

            db.insert("HealthProfile",null,contentValues);
        }
        db.close();
    }

    private int getWeightCount() {
        int count =0;
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        try{
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM HealthProfile WHERE type =0",null);

            if(cursor!=null) {
                cursor.moveToFirst();
            }
            count = cursor.getInt(0);

        }catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return count;
    }
    private int getPreviousWeight() {
        int weight =0;
        int count =0;
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT rate FROM HealthProfile WHERE type = 0 " +
                "ORDER BY created_at DESC LIMIT 2",null);

        if(cursor.moveToFirst()) {
            do{
                if(count==1) {
                    weight = cursor.getInt(0);
                }
                count++;
            }while (cursor.moveToNext());
        }

        db.close();
        return weight;
    }
    public int getCurrentData(int type) {
        int weight =0;
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT rate FROM HealthProfile WHERE type = "+type+
                " ORDER BY created_at DESC LIMIT 1",null);

        if(cursor.moveToFirst()) {
            do{
                weight = cursor.getInt(0);
            }while (cursor.moveToNext());
        }

        db.close();
        return weight;
    }

    public int getLastNo() {
        int no=0;

        if(getCount()!=0) {
            SQLiteDatabase db = fitnessDB.getReadableDatabase();

            try{
                Cursor cursor = db.rawQuery("SELECT MAX(no) FROM HealthProfile",null);

                if(cursor!=null) {
                    cursor.moveToFirst();
                }
                no = cursor.getInt(0);

            }catch (SQLException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return no;
    }
}
