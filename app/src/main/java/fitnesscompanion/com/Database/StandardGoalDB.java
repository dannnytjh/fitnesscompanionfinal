package fitnesscompanion.com.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fitnesscompanion.com.Model.StandardGoal;

public class StandardGoalDB{
        private Context context;
        private FitnessDB fitnessDB;

        public StandardGoalDB(Context context){
            this.context = context;
            fitnessDB = new FitnessDB(context);
        }
    public void deleteData() {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        db.delete("standardGoal",null,null);
        db.close();
    }
    public ArrayList<StandardGoal> getAllData() {
        ArrayList<StandardGoal> arrayList = getAllData("SELECT * FROM standardGoal ORDER BY goalName ASC");
        return arrayList;
    }
    public ArrayList<StandardGoal> getAllData(String query) {
        ArrayList<StandardGoal> arrayList = new ArrayList<StandardGoal>();
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()) {
            do{

                arrayList.add(new StandardGoal(cursor.getInt(0),cursor.getString(1),
                        cursor.getString(2),cursor.getInt(3),cursor.getInt(4)));
            }while (cursor.moveToNext());
        }
        db.close();
        return arrayList;
    }
    public void insertData(ArrayList<StandardGoal> arrayList) {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int x=0; x<arrayList.size();x++) {
            contentValues.put("standardGoalId",arrayList.get(x).getStandardGoalId());
            contentValues.put("goalName",arrayList.get(x).getGoalName());
            contentValues.put("foodIntake",arrayList.get(x).getFoodIntake());
            contentValues.put("activityDuration",arrayList.get(x).getActivityDuration());

            db.insert("standardGoal",null,contentValues);
        }
        db.close();

    }
    public StandardGoal getStandardGoal(int standardGoalId) {
        StandardGoal goal = new StandardGoal();
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM standardGoal WHERE standardGoalId = "+standardGoalId,null);

        if(cursor.moveToFirst()) {
            do{

                goal=new StandardGoal(cursor.getInt(0),cursor.getString(1),
                        cursor.getString(2),cursor.getInt(3),cursor.getInt(4));
            }while (cursor.moveToNext());
        }
        db.close();
        return goal;
    }

}
