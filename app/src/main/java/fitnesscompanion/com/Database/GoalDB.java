package fitnesscompanion.com.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import fitnesscompanion.com.Model.Goal;

/**
 * Created by Soon Kok Fung
 */

public class GoalDB {
    private Context context;
    private FitnessDB fitnessDB;
    public GoalDB(Context context) {
        this.context=context;
        fitnessDB = new FitnessDB(context);
    }
    public void deteleData() {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        db.delete("Goal",null,null);
        db.close();
    }
    public ArrayList<Goal> getAllData() {
        ArrayList<Goal> arrayList = getAllData("SELECT * FROM goal");
        return arrayList;
    }
    public ArrayList<Goal> getAllData(String query) {
        ArrayList<Goal> arrayList = new ArrayList<Goal>();
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()) {
            do{

                arrayList.add(new Goal(cursor.getInt(0),cursor.getInt(1),
                        cursor.getString(2),cursor.getInt(3)));
            }while (cursor.moveToNext());
        }
        db.close();
        return arrayList;
    }
   /* public void insertWeight(Goal goal) {
        goal.setType(0);
        insertData(goal);
    }
    public void insertStep(Goal goal) {
        goal.setType(1);
        insertData(goal);
    }
    public void insertCal(Goal goal) {
        goal.setType(2);
        insertData(goal);
    }*/
   /* public int getCurrentWeight() {
        return getCurrentData(0);
    }
    public int getCurrentStep() {
        return getCurrentData(1);
    }
    public int getCurrentCal() {
        return getCurrentData(2);
    }*/
    public void insertData(ArrayList<Goal> arrayList) {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int x=0; x<arrayList.size();x++) {
            contentValues.put("goalId", arrayList.get(x).getGoalId());
            contentValues.put("type", arrayList.get(x).getType());
            contentValues.put("description", arrayList.get(x).getDescription());
            contentValues.put("standardGoalId",arrayList.get(x).getStandardGoalId());
            //contentValues.put("measurement",arrayList.get(x).getActivityDuration());
            //     contentValues.put("rate",goal.getRate());

            db.insert("Goal", null, contentValues);
            db.close();
        }
    }
   /* public int getCurrentData(int type) {
        int rate =0;

        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT rate FROM Goal WHERE type = "+type+
                " ORDER BY created_at DESC LIMIT 1",null);

        if(cursor.moveToFirst()) {
            do{
                rate = cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        db.close();

        return rate;
    }*/

    public int getSize(){
        int count =0;
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        try{
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Goal",null);

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

    private int getCount(){
        int count =0;
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        try{
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Goal",null);

            if(cursor!=null) {
                cursor.moveToFirst();
            }
            count = cursor.getInt(0);

        }catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        return count;
    }
    private int getLastNo() {
        int no=0;

        if(getCount()!=0) {
            SQLiteDatabase db = fitnessDB.getReadableDatabase();

            try{
                Cursor cursor = db.rawQuery("SELECT MAX(goalId) FROM Goal",null);

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
