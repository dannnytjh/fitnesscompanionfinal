package fitnesscompanion.com.View.Food.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Adapter.DietExpandableAdapter;
import fitnesscompanion.com.Database.UserDB;
import fitnesscompanion.com.Model.Diet;
import fitnesscompanion.com.Model.HealthProfile;
import fitnesscompanion.com.Model.User;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.DietRequest;
import fitnesscompanion.com.ServerRequest.FoodRequest;
import fitnesscompanion.com.Util.BarCodeActivity;
import fitnesscompanion.com.Util.ConnectionDetector;
import fitnesscompanion.com.Util.DatePicker;
import fitnesscompanion.com.Util.OnSwipeTouchListener;
import fitnesscompanion.com.Util.ProgressBarAnimation;
import fitnesscompanion.com.Util.ProgressBarFood;
import fitnesscompanion.com.View.Food.DetailFoodActivity;
import fitnesscompanion.com.View.Food.FoodImage;
import fitnesscompanion.com.View.Food.NewFoodActivity;
import fitnesscompanion.com.View.Food.SearchFoodActivity;
import fitnesscompanion.com.View.Food.UploadFoodActivity;

@SuppressLint("ValidFragment")
public class tab1 extends Fragment {

    //prev food fragment (tab layout)

    private Context context;
    private final int BREAKFAST = 0;
    private final int LUNCH = 1;
    private final int DINNER = 2;
    private final int OTHER = 3;

    @BindView(R.id.menu_food)
    FloatingActionMenu menu_food;
    //@BindView(R.id.fabAdd) FloatingActionButton fabAdd;
    @BindView(R.id.fabBarcode)
    com.github.clans.fab.FloatingActionButton fabBarcode;
    @BindView(R.id.fabSearch)
    com.github.clans.fab.FloatingActionButton fabSearch;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.txtIntake)
    TextView txtIntake;
    @BindView(R.id.txtRemaining)
    TextView txtRemaining;
    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.btnGo)
    ImageButton btnGo;
    @BindView(R.id.expandedListFood)
    ExpandableListView expandedListFood;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    private ProgressBarFood progressBarFood;
    private Date defaultDate;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat dateFormat = new SimpleDateFormat("EEEE , yyyy-MM-dd");
    private String today;
    private Snackbar snackbar;
    private ConnectionDetector detector;
    private UserDB userDB;
    private User user;
    private HealthProfile health;
    private FoodRequest foodRequest;
    private HashMap<String, ArrayList<Diet>> arrayListHashMap;
    private DietExpandableAdapter dietExpandableAdapter;

    //Date Control
    private OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(context) {
        @Override
        public void onSwipeRight() {
            super.onSwipeRight();
            if (detector.isConnected()) {
                today = String.valueOf(getDays(-1));
                setDate();
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
                }
            }
        }

        @Override
        public void onLongClick() {
            super.onLongClick();
            if (detector.isConnected()) {
                btnGo.setVisibility(View.GONE);
                defaultDate = new Date();
                txtDate.setText(getString(R.string.today) + " , " + format.format(defaultDate));
                getDietData();
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
                getDietData();
                return true;
            }
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
                }
            });
            datePicker.setSelectedDate(defaultDate);
            datePicker.setMaxDate(new Date());
            datePicker.show(getActivity().getFragmentManager(), "Date Picker");

        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    //List View Control
    private ExpandableListView.OnChildClickListener onChildClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
            Diet diet = (Diet) dietExpandableAdapter.getChild(groupPosition, childPosition);
            //Toast.makeText(context,"short"+diet.getName(),Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, DetailFoodActivity.class);
            intent.putExtra("no", diet.getFoodNo());
            intent.putExtra("name", diet.getName());
            intent.putExtra("qty", diet.getQty());
            getActivity().finish();
            startActivity(intent);
            return true;
        }
    };
    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

            if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                int childPosition = ExpandableListView.getPackedPositionChild(id);
                final Diet diet = (Diet) dietExpandableAdapter.getChild(groupPosition, childPosition);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getResources().getString(R.string.action)).setItems(getResources().
                        getStringArray(R.array.actionArray), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(context, NewFoodActivity.class);
                                intent.putExtra("no", diet.getFoodNo());
                                intent.putExtra("dietNo", diet.getNo());
                                intent.putExtra("value", diet.getQty());
                                intent.putExtra("type", diet.getType());
                                getActivity().finish();
                                startActivity(intent);
                                break;
                            case 1:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(getString(R.string.status_delete))
                                        .setNegativeButton("No", null)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                new DietRequest(context).deleteDiet(new DietRequest.VolleyCallAction() {
                                                    @Override
                                                    public void onSuccess() {
                                                        Toast.makeText(context, "Deleted Successful", Toast.LENGTH_LONG).show();
                                                        getDietData();
                                                    }
                                                }, diet.getNo());
                                            }
                                        }).show();
                                break;
                        }
                    }
                });
                builder.show();

            }
            return true;
        }
    };
    
    //menu control
    private View.OnClickListener onMenuControl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                /*
                case R.id.fabAdd :
                    Toast.makeText(context,"Add",Toast.LENGTH_LONG).show();
                    break;*/
                case R.id.fabBarcode:
                    Intent intent = new Intent(getContext(), BarCodeActivity.class);
                    startActivityForResult(intent, 2);
                    break;
                /*case R.id.fabNearBy:
                    getActivity().finish();
                    startActivity(new Intent(getContext(), RestaurantActivity.class));
                    break;*/
                case R.id.fabSearch:
                    getActivity().finish();
                    startActivity(new Intent(getContext(), SearchFoodActivity.class));
                    break;


            }
            menu_food.close(true);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);
        ButterKnife.bind(this, rootView);
        context = this.getContext();

        rootView.setOnTouchListener(onSwipeTouchListener);
        detector = new ConnectionDetector(getContext());
        userDB = new UserDB(getContext());
        foodRequest = new FoodRequest(getContext());
        user = userDB.getData();
        // user.setWeight(new HealthProfileDB(context).getCurrentWeight());
        menu_food.setClosedOnTouchOutside(true);
        btnBack.setOnClickListener(onDateControl);
        btnGo.setOnLongClickListener(onLongClickListener);
        btnGo.setOnClickListener(onDateControl);
        //fabAdd.setOnClickListener(onMenuControl);
        fabBarcode.setOnClickListener(onMenuControl);
        //fabNearBy.setOnClickListener(onMenuControl);
        fabSearch.setOnClickListener(onMenuControl);
        txtDate.setOnClickListener(onSelectDate);

        expandedListFood.setOnChildClickListener(onChildClickListener);
        expandedListFood.setOnItemLongClickListener(onItemLongClickListener);
        defaultDate = new Date();
        today = format.format(defaultDate);
        if (detector.isConnected()) {
            getDietData();
        }
        txtDate.setText(getString(R.string.today) + " , " + format.format(defaultDate));
        createCustomAnimation();
        return rootView;
    }

    private void setDate() {
        if (DateUtils.isToday(defaultDate.getTime())) {
            txtDate.setText(getString(R.string.today) + " , " + format.format(defaultDate));
            btnGo.setVisibility(View.GONE);
        } else {
            txtDate.setText(dateFormat.format(defaultDate));
            btnGo.setVisibility(View.VISIBLE);
        }

        getDietData();
    }

    private Date getDays(int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(defaultDate);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menu_food.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menu_food.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menu_food.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menu_food.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menu_food.getMenuIconView().setImageResource(menu_food.isOpened()
                        ? R.drawable.ic_menu : R.drawable.ic_close);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menu_food.setIconToggleAnimatorSet(set);
    }

    //barcode scanner
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            String barCode = data.getStringExtra("Barcode");
            if (barCode != null) {
                foodRequest.barcodeSearch(new FoodRequest.VolleyCallBarcode() {
                    @Override
                    public void onSuccess(int no) {
                        if (no == 0) {
                            //Toast.makeText(context,getString(R.string.status_noRecord),Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(getString(R.string.status_noRecord));
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                        } else {
                            getActivity().finish();
                            startActivity(new Intent(context, NewFoodActivity.class).putExtra("no", no));
                        }

                    }
                }, barCode);

            }
        }
    }

    //get food data from server
    private void getDietData() {
        expandedListFood.invalidateViews();
        new DietRequest(context).getDiet(new DietRequest.VolleyCall() {
            @Override
            public void onSuccess(ArrayList<Diet> dietList) {
                dietExpandableAdapter = new DietExpandableAdapter(context, getHeader(), getData(dietList));
                expandedListFood.setAdapter(dietExpandableAdapter);
            }
        }, today);
    }

    //display parent list, meal type
    private ArrayList<String> getHeader() {
        ArrayList<String> header = new ArrayList<String>();
        String[] headerArray = context.getResources().getStringArray(R.array.dietOption);
        for (int x = 0; x < headerArray.length; x++) {
            header.add(headerArray[x]);
        }
        return header;
    }

    //put data into specific meal type
    private HashMap<String, ArrayList<Diet>> getData(ArrayList<Diet> dietList) {
        arrayListHashMap = new HashMap<String, ArrayList<Diet>>();

        arrayListHashMap.put(getHeader().get(0), getSelectedDiet(BREAKFAST, dietList));
        arrayListHashMap.put(getHeader().get(1), getSelectedDiet(LUNCH, dietList));
        arrayListHashMap.put(getHeader().get(2), getSelectedDiet(DINNER, dietList));
        arrayListHashMap.put(getHeader().get(3), getSelectedDiet(OTHER, dietList));
        return arrayListHashMap;
    }

    private ArrayList<Diet> getSelectedDiet(int index, ArrayList<Diet> dietList) {
        ArrayList<Diet> diets = new ArrayList<>();
        //  setProgressBar(dietList);
        for (Diet diet : dietList) {
            if (diet.getType() == index) {
                diets.add(diet);
            }
        }

        //progressBarFood= new ProgressBarFood(context,progressBar,txtIntake,txtRemaining);
        return diets;
    }

    //progress bar

    private void setProgressBar(ArrayList<Diet> dietList) {

        int max = health.getBmr();

        int value = 0;
        for (Diet diet : dietList) {
            value += diet.getCalories();
        }
        int remaining = max - value;

        String text = context.getString(R.string.remaining) + " " + String.valueOf(remaining) + " " + context.getString(R.string.cal);

        txtIntake.setText(String.valueOf(value) + " " + context.getString(R.string.cal) + " " + context.getString(R.string.intake));

        if (remaining < 0) {
            text = context.getString(R.string.over) + " " + String.valueOf(value - max) + " " + context.getString(R.string.cal);
        }
        txtRemaining.setText(text);
        progressBar.setMax(max);

        ProgressBarAnimation animation = new ProgressBarAnimation(progressBar, 0, value);
        animation.setDuration(1000);
        progressBar.startAnimation(animation);
        setProgressBarStyle(value, max);
    }

    private void setProgressBarStyle(int value, int max) {
        int color = Color.GREEN;

        int percentage = (value * 100) / max;

        if (percentage > 50 & percentage <= 80)
            color = Color.YELLOW;
        if (percentage > 80)
            color = Color.RED;

        progressBar.getProgressDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
    }
}
