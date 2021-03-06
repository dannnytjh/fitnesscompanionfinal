package fitnesscompanion.com.View.Home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "running.db";
    private static final int DB_VERSION = 3;
    private static final String TABLE_NAME = "RunHistoryTable";
    private static final String COL_ID = "ID"; // Column 0, cols start indexing at 0
    private static final String COL1 = "Distance";
    private static final String COL2 = "Time";
    private static final String COL3 = "AvgPace";
    private static final String COL4 = "Date";
    private static final String COL5 = "UID";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Craft SQL statement to create database table
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " FLOAT, " +
                COL2 + " VARCHAR(8), " +
                COL3 + " FLOAT, " +
                COL4 + " VARCHAR(10)," +
                COL5 + " INTEGER);";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String dropTable = " DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(dropTable);
        onCreate(sqLiteDatabase);
    }


    /**
     * Add a new row to a database table (In this case, only one table created)
     * @param dist - Float for the total distance traveled in k,
     * @param time - String of total time taken in format "hh:mm:ss"
     * @param pace - Float for the pace of the activity in min/km
     * @param date - String of date the activity happened in format "MM/DD/YYYY"
     * @return - Boolean whether the row insertion was successful or not
     */
    boolean addData( float dist, String time, float pace, String date, int userID){
        SQLiteDatabase db = this.getWritableDatabase();

        // Mapping for database values
        ContentValues contentValues = new ContentValues();
        // Key: column name
        // Value: column value
        contentValues.put(COL1, dist);
        contentValues.put(COL2, time);
        contentValues.put(COL3, pace);
        contentValues.put(COL4, date);
        contentValues.put(COL5,userID);

        // If data is inserted incorrectly, db.insert() returns -1
        if (db.insert(TABLE_NAME, null, contentValues) != -1)
            return true;
        else
            return false;
    }


    /**
     * Get all rows from a table via SQL select statement
     * @return - Contents from the db table
     */
    Cursor getContentsByID(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectStmt = "SELECT * FROM RunHistoryTable WHERE UID = '"+id+"'" ;
        return db.rawQuery(selectStmt, null);
    }

    Cursor getContents(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectStmt = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(selectStmt, null);
    }
}
