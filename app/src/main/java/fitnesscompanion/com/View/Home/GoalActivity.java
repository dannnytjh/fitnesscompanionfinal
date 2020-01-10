package fitnesscompanion.com.View.Home;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Soon Kok Fung
 */
/*public class GoalActivity extends AppCompatActivity {

    @BindView(R.id.listView) ListView listView;

    private ArrayList<GoalUI> goalUI;
    private ConnectionDetector detector;
    private HealthProfileDB healthProfileDB;
    private HealthProfileRequest healthProfileRequest;
    private GoalDB goalDB;
    private UserRequest userRequest;
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            switch (position) {
                case 0:
                    editWeight(position);
                    break;
                case 1:
                    editStep(position);
                    break;
                case 2:
                    editCal(position);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        getSupportActionBar().setTitle("Goal");
        ButterKnife.bind(this);
        detector = new ConnectionDetector(this);
        healthProfileDB = new HealthProfileDB(this);
        healthProfileRequest = new HealthProfileRequest(this);
        userRequest = new UserRequest(this);
        goalDB = new GoalDB(this);
        getData();
        listView.setOnItemClickListener(onItemClickListener);
    }

    private void getData() {

        int weight =0,step=0,cal=0;

        if(new GoalDB(getApplicationContext()).getCurrentWeight()<=new HealthProfileDB(this).getLastWeight()){
            if(new GoalDB(getApplicationContext()).getCurrentWeight()>=new HealthProfileDB(this).getCurrentWeight())
                weight=1;
        }
        else if(new GoalDB(getApplicationContext()).getCurrentWeight()>=new HealthProfileDB(this).getLastWeight()){
            if(new GoalDB(getApplicationContext()).getCurrentWeight()<=new HealthProfileDB(this).getCurrentWeight())
                weight=1;
        }

        if(new HealthProfileDB(this).getCurrentStep()>=goalDB.getCurrentStep()){
            step=1;
        }
        if(new ActivityDataDB(this).getCurrentBurn()>=goalDB.getCurrentCal()) {
            cal=1;
        }

        goalUI = new ArrayList<>();
        GoalDB goalDB = new GoalDB(this);

        goalUI.add(new GoalUI(1,0,goalDB.getCurrentWeight(),weight));
        goalUI.add(new GoalUI(2,1,goalDB.getCurrentStep(),step));
        goalUI.add(new GoalUI(3,2,goalDB.getCurrentCal(),cal));
        listView.setAdapter(new GoalAdapter(this,goalUI));
    }

    private void editWeight(int position) {
        SeekBarPicker seekBarPicker = new SeekBarPicker(this, new SeekBarPicker.GetValue() {
            @Override
            public void onSuccess(int value) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(detector.isConnected()) {
                    new GoalDB(getApplicationContext()).insertWeight(new Goal(value));
                    Goal goal= new Goal();
                    goal.setType(0);
                    goal.setRate(value);
                    if(detector.isConnected()) {
                        userRequest.updateGoal(goal);
                        getData();
                    }
                }
            }
        });
        seekBarPicker.show(getFragmentManager(),"Weight Picker");
        seekBarPicker.setMax(250);
        seekBarPicker.setTitle(getString(R.string.editWeight));
        seekBarPicker.setUnit(getString(R.string.kg));
        seekBarPicker.setMin(10);
        seekBarPicker.setValue(goalUI.get(position).getRate());
    }
    private void editStep(int position) {
        SeekBarPicker seekBarPicker = new SeekBarPicker(this, new SeekBarPicker.GetValue() {
            @Override
            public void onSuccess(int value) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(detector.isConnected()) {
                    new GoalDB(getApplicationContext()).insertStep(new Goal(value));
                    Goal goal= new Goal();
                    goal.setType(1);
                    goal.setRate(value);
                    if(detector.isConnected()) {
                        userRequest.updateGoal(goal);
                        getData();
                    }
                }
            }
        });
        seekBarPicker.show(getFragmentManager(),"Step Picker");
        seekBarPicker.setMax(30000);
        seekBarPicker.setTitle("Edit Step");
        seekBarPicker.setUnit("Step");
        seekBarPicker.setMin(500);
        seekBarPicker.setRange(500);
        seekBarPicker.setValue(goalUI.get(position).getRate());
    }
    private void editCal(int position) {
        SeekBarPicker seekBarPicker = new SeekBarPicker(this, new SeekBarPicker.GetValue() {
            @Override
            public void onSuccess(int value) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                new GoalDB(getApplicationContext()).insertCal(new Goal(value));
                Goal goal= new Goal();
                goal.setType(2);
                goal.setRate(value);
                if(detector.isConnected()) {
                    userRequest.updateGoal(goal);
                    getData();
                }
            }
        });
        seekBarPicker.show(getFragmentManager(),"brun Picker");
        seekBarPicker.setMax(1000);
        seekBarPicker.setTitle("Edit Calories Burn");
        seekBarPicker.setUnit("cal");
        seekBarPicker.setMin(100);
        seekBarPicker.setRange(100);
        seekBarPicker.setValue(goalUI.get(position).getRate());
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,MenuActivity.class).putExtra("index",0));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}*/
