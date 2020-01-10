package fitnesscompanion.com.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Created by Soon Kok Fung
 */

public class ActivityVersionDB {

    private Context context;
    private FitnessDB fitnessDB;

    public ActivityVersionDB(Context context) {
        this.context=context;
        fitnessDB = new FitnessDB(context);
    }

    public int getData(){

        int version=0;
        SQLiteDatabase db = fitnessDB.getReadableDatabase();

        try{
            Cursor cursor = db.rawQuery("SELECT ver FROM ActivityVersion",null);

            if(cursor!=null) {
                cursor.moveToFirst();
            }
            version = cursor.getInt(0);

        }catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return version;
    }
    public int updateData(int version) {
        SQLiteDatabase db = fitnessDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ver",version);

        return db.update("ActivityVersion",contentValues,null,null);
    }
 }
