package fitnesscompanion.com.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fitnesscompanion.com.Model.Activity;
/**
 * Created by Soon Kok Fung
 */

public class ActivityDB {
    private Context context;
    private FitnessDB fitnessDB;

    public ActivityDB(Context context) {
        this.context=context;
        fitnessDB = new FitnessDB(context);
    }
    public void deleteData() {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        db.delete("Activity",null,null);
        db.close();
    }
    public void insertData(ArrayList<Activity> arrayList) {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int x=0; x<arrayList.size();x++) {
            contentValues.put("no",arrayList.get(x).getNo());
            contentValues.put("name",arrayList.get(x).getName());
            contentValues.put("description",arrayList.get(x).getDescription());
            contentValues.put("calories",arrayList.get(x).getCalories());
            contentValues.put("recommandTime",arrayList.get(x).getTime());
            contentValues.put("image",arrayList.get(x).getImage());

            db.insert("Activity",null,contentValues);
        }
        db.close();

    }
    public ArrayList<Activity> getAllData() {
        ArrayList<Activity> arrayList = getAllData("SELECT * FROM Activity ORDER BY name ASC");
        return arrayList;
    }
    public ArrayList<Activity> getCommon() {
        ArrayList<Activity> arrayList = getAllData("SELECT DISTINCT * FROM Activity WHERE no NOT IN " +
                "(SELECT DISTINCT activityNo FROM ActivityData LIMIT 3 ) ORDER BY name ASC ");
        return arrayList;
    }
    public ArrayList<Activity> getFavourite() {
        ArrayList<Activity> arrayList = getAllData("SELECT DISTINCT * FROM Activity WHERE no IN " +
                "(SELECT DISTINCT activityNo FROM ActivityData) ORDER BY name ASC LIMIT 3");
        return arrayList;
    }
    public ArrayList<Activity> getAllData(String query) {
        ArrayList<Activity> arrayList = new ArrayList<Activity>();
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()) {
            do{

                arrayList.add(new Activity(cursor.getInt(0),cursor.getString(1),
                        cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getString(5)));
            }while (cursor.moveToNext());
        }
        db.close();
        return arrayList;
    }
    public Activity getActivity(int activityNo) {
        Activity activity = new Activity();
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Activity WHERE no = "+activityNo,null);

        if(cursor.moveToFirst()) {
            do{

                activity=new Activity(cursor.getInt(0),cursor.getString(1),
                        cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getString(5));
            }while (cursor.moveToNext());
        }
        db.close();
        return activity;
    }

    public String[] getName() {
        ArrayList<Activity> arrayList = getAllData();
        String[] name = new String[arrayList.size()];

        for(int x=0; x<arrayList.size();x++) {
            name[x] = arrayList.get(x).getName();
        }
        return name;
    }
}
