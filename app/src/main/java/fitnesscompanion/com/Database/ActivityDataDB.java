package fitnesscompanion.com.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import fitnesscompanion.com.Model.Achievement;
import fitnesscompanion.com.Model.ActivityData;

/**
 * Created by Soon Kok Fung
 */

public class ActivityDataDB {
    private Context context;
    private FitnessDB fitnessDB;

    public ActivityDataDB(Context context) {
        this.context = context;
        fitnessDB = new FitnessDB(context);
    }
    public void insertData(ArrayList<ActivityData> activityData) {

        SQLiteDatabase db = fitnessDB.getWritableDatabase();

        for(int x=0 ;x<activityData.size();x++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("no",activityData.get(x).getNo());
            contentValues.put("duration",activityData.get(x).getDuration());
            contentValues.put("heartRate",activityData.get(x).getHr());
            contentValues.put("created_at",activityData.get(x).getDate());
            contentValues.put("activityNo",activityData.get(x).getActivityNo());
            db.insert("ActivityData",null,contentValues);
        }

        db.close();
    }
    public void insertData(ActivityData activityData) {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("no",getLastNo()+1);
        contentValues.put("duration",activityData.getDuration());
        contentValues.put("heartRate",activityData.getHr());
        contentValues.put("activityNo",activityData.getActivityNo());

        db.insert("ActivityData",null,contentValues);
        db.close();
    }

    public void deleteData() {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        db.delete("ActivityData",null,null);
        db.close();
    }

    public int getLastNo() {
        int no =0;

        if(getCount()!=0) {
            SQLiteDatabase db = fitnessDB.getReadableDatabase();

            try{
                Cursor cursor = db.rawQuery("SELECT MAX(no) FROM ActivityData",null);

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

    public int getCount(){
        int count =0;

        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        try{
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM ActivityData",null);

            if(cursor!=null) {
                cursor.moveToFirst();
            }
            count = cursor.getInt(0);

        }catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        //db.close();
        return count;
    }
    public int getSize(){
        int count =0;

        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        try{
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM ActivityData",null);

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
    public ArrayList<Achievement> getAchievement() {
        ArrayList<Achievement> achievement  = new ArrayList<Achievement>();


        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT Activity.no,name,image,COUNT(ActivityData.no) " +
                "from Activity LEFT JOIN ActivityData ON Activity.no=activityNo " +
                "GROUP BY Activity.no,name,image ORDER BY COUNT(ActivityData.no) DESC",null);

        if(cursor.moveToFirst()) {
            do{
                achievement.add(new Achievement(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3)));

            }while (cursor.moveToNext());
        }
        db.close();

        return achievement;
    }
    public int getCurrentBurn() {
        int cal =0;
        SQLiteDatabase db = fitnessDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(duration*calories/60) FROM Activity INNER JOIN ActivityData ON activityNo  = Activity.no AND date(ActivityData.created_at) = date('now')",null);
        if(cursor.moveToFirst()) {
            do{
                cal = cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        db.close();

        return cal;
    }
    public ArrayList<BarEntry> getMonthly(String year) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        for(int x=0;x<13;x++) {
            Cursor cursor = db.rawQuery("SELECT COUNT(no) FROM ActivityData WHERE " +
                    "strftime('%Y', created_at) = '"+year+"' AND " +
                    "strftime('%m', created_at) = '"+String.format("%02d",x)+"' " +
                    "ORDER BY created_at ASC",null);
            if(cursor.moveToFirst()) {
                do{

                    if(cursor.getInt(0)!=0) {
                        barEntries.add(new BarEntry(x-1,cursor.getInt(0)));
                    }
                }while ((cursor.moveToNext()));
            }
        }
        db.close();

        return barEntries;
    }
}
