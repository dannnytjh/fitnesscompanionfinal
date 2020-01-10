package fitnesscompanion.com.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Soon Kok Fung
 */

public class   FitnessDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FitnessDb";
    private static final int DATABASE_VERSION = 1;

    private static final String queryCreateActivityVer = "CREATE TABLE ActivityVersion(ver INTEGER);";
    private static final String queryCreateTableActivity="CREATE TABLE Activity(no INTEGER " +
            "PRIMARY KEY ,name VARCHAR(255) ,description VARCHAR(255)," +
            "calories INTEGER,recommandTime INTEGER,image MEDIUMTEXT);";
    private static final String queryCreateTableUser = "CREATE TABLE User (id int primary key,name varchar(255)," +
            "gender INTEGER,dob VARCHAR(10),image MEDIUMTEXT,email VARCHAR(255),address varchar(100)," +
            "password VARCHAR(20),active INTEGER,createdDate VARCHAR(50));";
    private static final String queryCreateTableHealthProfile = "CREATE TABLE HealthProfile " +
            "(no INTEGER PRIMARY KEY NOT NULL, weight INTEGER , height DOUBLE, description, created_at DATETIME DEFAULT CURRENT_TIMESTAMP);";
    private static final String getQueryCreateActivityData = "CREATE TABLE ActivityData " +
            "(no INTEGER PRIMARY KEY NOT NULL,duration INTEGER,heartRate INTEGER ,created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
            "activityNo INTEGER,FOREIGN KEY(activityNo) REFERENCES Activity(no));";
    private static final String getQueryCreateGoal ="CREATE TABLE Goal(goalId INTEGER PRIMARY KEY ,type INTEGER,description VARCHAR(255),standardGoalId INTEGER,measurement INTEGER,created_at DATETIME DEFAULT CURRENT_TIMESTAMP);";
    private static final String queryCreateTableStandardGoal = "CREATE TABLE standardGoal(standardGoalId INTEGER PRIMARY KEY, goalName VARCHAR(50), createAt DATETIME ,foodIntake INTEGER,activityDuration INTEGER);";


    private static final String queryInsertActivityVer = "INSERT INTO ActivityVersion VALUES (0);";
    private static final String dropTableActivityVersion = "DROP TABLE IF EXISTS ActivityVersion;";
    private static final String dropTableActivity = "DROP TABLE IF EXISTS Activity;";
    private static final String dropTableUser = "DROP TABLE IF EXISTS User;";
    private static final String dropTableHealthProfile = "DROP TABLE IF EXISTS HealthProfile";
    private static final String getDropTableActivityData = "DROP TABLE IF EXISTS ActivityData";
    private static final String getDropTableGoal = "DROP TABLE IF EXISTS Goal";
    private static final String getDropTableStandardGoal = "DROP TABLE IF EXISTS standardGoal";


    public FitnessDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(queryCreateActivityVer);
        db.execSQL(queryCreateTableActivity);
        db.execSQL(queryCreateTableUser);
        db.execSQL(queryCreateTableHealthProfile);
        db.execSQL(getQueryCreateActivityData);
        db.execSQL(getQueryCreateGoal);
        db.execSQL(queryCreateTableStandardGoal);

        db.execSQL(queryInsertActivityVer);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTableActivityVersion);
        db.execSQL(dropTableActivity);
        db.execSQL(dropTableUser);
        db.execSQL(dropTableHealthProfile);
        db.execSQL(getDropTableActivityData);
        db.execSQL(getDropTableGoal);
        db.execSQL(getDropTableStandardGoal);
        onCreate(db);

    }

}
