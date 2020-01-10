package fitnesscompanion.com.View.Food;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    public void queryData(String sql)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //insert data according to col, index 0 for id, auto increment
    public void insertData (String name, String mealType, String date, byte[] image)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO FOOD1 VALUES (NULL, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, mealType);
        statement.bindString(3, date);
        statement.bindBlob(4, image);

        statement.executeInsert();
    }

    //not used
    public Cursor getData (String sql)
    {
        SQLiteDatabase database = getReadableDatabase();
        String[] selection = {"name","mealType","Id","image", "date"};
        return database.rawQuery(sql,null);

    }

    public Cursor getData1 (String sql)
    {
        SQLiteDatabase database = getReadableDatabase();
        String[] selection = {"name","mealType","Id","image", "date"};
        return database.rawQuery(sql,null);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
