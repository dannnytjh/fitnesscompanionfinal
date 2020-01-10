package fitnesscompanion.com.View.Food.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Database.UserDB;
import fitnesscompanion.com.Model.User;
import fitnesscompanion.com.R;
import fitnesscompanion.com.Util.BarCodeActivity;
import fitnesscompanion.com.Util.ConnectionDetector;
import fitnesscompanion.com.Util.DatePicker;
import fitnesscompanion.com.Util.OnSwipeTouchListener;
import fitnesscompanion.com.View.Food.FoodList;
import fitnesscompanion.com.View.Food.SQLiteHelper;
import fitnesscompanion.com.View.Food.SearchFoodActivity;
import fitnesscompanion.com.View.Food.UploadFoodActivity;

public class tab2 extends Fragment implements View.OnClickListener{

    //upload img fragment
    private Context context;

    @BindView(R.id.menu_food)
    FloatingActionMenu menu_food;
    @BindView(R.id.fabUpload)
    com.github.clans.fab.FloatingActionButton fabUpload;
    @BindView(R.id.cv1) CardView cv1;
    @BindView(R.id.cv2) CardView cv2;
    @BindView(R.id.cv3) CardView cv3;
    @BindView(R.id.cv4) CardView cv4;
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.btnGo)
    ImageButton btnGo;
    public static SQLiteHelper sqLiteHelper;

    private Date defaultDate;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat dateFormat = new SimpleDateFormat("EEEE , yyyy-MM-dd");
    private String today;
    private Snackbar snackbar;
    private ConnectionDetector detector;
    private UserDB userDB;
    private User user;
    int traineeId;
    private int mealType;
    private String mealDate;

    //Date Control
    private OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(context) {
        @Override
        public void onSwipeRight() {
            super.onSwipeRight();
            if (detector.isConnected()) {
                today = String.valueOf(getDays(-1));
                setDate();
                onResume();

            }
        }

        @Override
        public void onSwipeLeft() {
            super.onSwipeLeft();
            if (detector.isConnected()) {
                if (!DateUtils.isToday(defaultDate.getTime())) {
                    today = String.valueOf(getDays(1));
                    Log.i("Here", "Today : " + today);
                    //defaultDate = getDays(1);
                    setDate();
                    onResume();

                }
            }
        }

        //long click to jump to current dat
        @Override
        public void onLongClick() {
            super.onLongClick();
            if (detector.isConnected()) {
                btnGo.setVisibility(View.GONE);
                defaultDate = new Date();
                txtDate.setText(getString(R.string.today) + " , " + format.format(defaultDate));
                mealDate = format.format(defaultDate).toString().trim();
                onResume();

            }
        }

    };

    //display date middle
    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (detector.isConnected()) {
                btnGo.setVisibility(View.GONE);
                defaultDate = new Date();
                txtDate.setText(getString(R.string.today) + " , " + format.format(defaultDate));
                mealDate = format.format(defaultDate).toString().trim();
                onResume();

                return true;
            }
            onResume();

            return false;
        }
    };

    // left right button date
    private View.OnClickListener onDateControl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (detector.isConnected()) {
                switch (view.getId()) {
                    case R.id.btnGo:
                        defaultDate = getDays(1);
                        today = format.format(defaultDate);
                        break;
                    case R.id.btnBack:

                        defaultDate = getDays(-1);
                        today = format.format(defaultDate);
                        break;
                }
                setDate();
                mealDate = format.format(defaultDate).toString().trim();
                onResume();
            }

        }
    };
    // date picker
    private View.OnClickListener onSelectDate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePicker datePicker = new DatePicker(context, new DatePicker.GetDate() {
                @Override
                public void onSuccess(Date date) {
                    defaultDate = date;
                    today = format.format(defaultDate);
                    setDate();
                    mealDate = format.format(defaultDate).toString().trim();
                }
            });
            datePicker.setSelectedDate(defaultDate);
            datePicker.show(getActivity().getFragmentManager(), "Date Picker");

        }
    };

    //format date
    private void setDate() {
        if (DateUtils.isToday(defaultDate.getTime())) {
            txtDate.setText(getString(R.string.today) + " , " + format.format(defaultDate));
            mealDate = format.format(defaultDate).toString().trim();
            btnGo.setVisibility(View.GONE);
        } else {
            txtDate.setText(dateFormat.format(defaultDate));
            mealDate = format.format(defaultDate).toString().trim();
            btnGo.setVisibility(View.VISIBLE);
        }

    }

    //get cal days
    private Date getDays(int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(defaultDate);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2, container, false);
        ButterKnife.bind(this, rootView);
        UserDB userDB = new UserDB(getContext());
        User user = userDB.getData();
        context = this.getContext();

        traineeId = user.getId();
        fabUpload.setOnClickListener(onMenuControl);

        detector = new ConnectionDetector(getContext());
        btnBack.setOnClickListener(onDateControl);
        btnGo.setOnLongClickListener(onLongClickListener);
        btnGo.setOnClickListener(onDateControl);
        txtDate.setOnClickListener(onSelectDate);
        defaultDate = new Date();
        today = format.format(defaultDate);
        txtDate.setText(getString(R.string.today) + " , " + format.format(defaultDate));

        cv1.setOnClickListener(this);
        cv2.setOnClickListener(this);
        cv3.setOnClickListener(this);
        cv4.setOnClickListener(this);

        //format date to "2019/12/31"
        mealDate = format.format(defaultDate);

        sqLiteHelper = new SQLiteHelper(getContext(), "FoodDB.sqlite", null, 10);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS FOOD1 (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR (200), mealType VARCHAR (200), date VARCHAR (200), image BLOB)");

        return rootView;
    }


    //menu control
    private View.OnClickListener onMenuControl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                //upload img fab
                case R.id.fabUpload:
                    getActivity().finish();
                    startActivity(new Intent(getContext(), UploadFoodActivity.class).putExtra("traineeId", traineeId));
                    break;


            }
            menu_food.close(true);
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //put extra to pass meal type and date to foodlist for query
            case R.id.cv1:
                Intent intent = new Intent (getContext(), FoodList.class);
                intent.putExtra("mealType",1);
                intent.putExtra("mealDate",mealDate);
                startActivity(intent);
                break;

            case R.id.cv2:
                Intent intent1 = new Intent (getContext(), FoodList.class);
                intent1.putExtra("mealType",2);
                intent1.putExtra("mealDate",mealDate);
                startActivity(intent1);
                break;

            case R.id.cv3:
                Intent intent2 = new Intent (getContext(), FoodList.class);
                intent2.putExtra("mealType",3);
                intent2.putExtra("mealDate",mealDate);
                startActivity(intent2);
                break;

            case R.id.cv4:
                Intent intent3 = new Intent (getContext(), FoodList.class);
                intent3.putExtra("mealType",4);
                intent3.putExtra("mealDate",mealDate);
                startActivity(intent3);
                break;

        }

    }
}
