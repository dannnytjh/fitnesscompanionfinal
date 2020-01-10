package fitnesscompanion.com.View.Home;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;

import fitnesscompanion.com.R;

public class JournalActivity extends AppCompatActivity {

    // Database Columns
    // Columns are indexed starting with 0, COL 0 is ID
    private static final int COL0 = 0;
    private static final int COL1 = 1;
    private static final int COL2 = 2;
    private static final int COL3 = 3;
    private static final int COL4 = 4;
    private static final int COL5 = 5;
    private int trainid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        trainid = this.getIntent().getIntExtra("traineeid",0);
        ListView listView = findViewById(R.id.listView);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        ArrayList<Integer> colList = new ArrayList<>();
        ArrayList<Float> distList = new ArrayList<>();
        ArrayList<String> timeList = new ArrayList<>();
        ArrayList<Float> avgPaceList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<Integer> traineeIDList = new ArrayList<>();

        //traineeId to refer to each of the trainee run activity
        Cursor contents = databaseHelper.getContentsByID(trainid);

        if (contents.getCount() == 0) {
            Toast.makeText(this, "No activities saved", Toast.LENGTH_SHORT).show();

        } else {
            //get data from sqlite
            while (contents.moveToNext()) {
                colList.add(contents.getInt(COL0));
                distList.add(contents.getFloat(COL1));
                timeList.add(contents.getString(COL2));
                avgPaceList.add(contents.getFloat(COL3));
                dateList.add(contents.getString(COL4));
                traineeIDList.add(contents.getInt(COL5));
            }
        }

        for(int i = 0;i<colList.size();i++) {
            ItemAdapter itemAdapter = new ItemAdapter(this, colList, distList, timeList,
                    avgPaceList, dateList,traineeIDList);
            listView.setAdapter(itemAdapter);
        }
    }

}