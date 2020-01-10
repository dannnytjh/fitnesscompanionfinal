package fitnesscompanion.com.View.Food;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Model.Diet;
import fitnesscompanion.com.Model.Food;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.DietRequest;
import fitnesscompanion.com.ServerRequest.FoodRequest;
import fitnesscompanion.com.Util.SeekBarController;
import fitnesscompanion.com.View.Home.MenuActivity;

/**
 * Created by Soon Kok Fung
 */
public class NewFoodActivity extends AppCompatActivity {

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.txtName) TextView txtName;
    @BindView(R.id.txtNum) TextView txtNum;
    @BindView(R.id.txtCalories) TextView txtCalories;
    @BindView(R.id.txtProtein) TextView txtProtein;
    @BindView(R.id.txtFat) TextView txtFat;
    @BindView(R.id.txtCarbohydrate) TextView txtCarbohydrate;
    @BindView(R.id.seekBar) SeekBar seekBar;
    @BindView(R.id.spinner) Spinner spinner;
    @BindView(R.id.btnAdd)
    Button btnAdd;

    private int foodNo;
    private Food foodList;
    private FoodRequest foodRequest;
    private DietRequest dietRequest;
    private int value=1;
    private int action =0;
    private int dietNo;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            int num = Integer.parseInt(editable.toString());
            txtCalories.setText(String.valueOf(foodList.getCalories() * num) + "  cal");
            txtProtein.setText(String.valueOf(foodList.getProtein() * num) + " g");
            txtFat.setText(String.valueOf(foodList.getFat() * num) + " g");
            txtCarbohydrate.setText(String.valueOf(foodList.getCarbohydrate() * num) + " g");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food);
        ButterKnife.bind(this);
        foodRequest = new FoodRequest(this);
        dietRequest = new DietRequest(this);
        getSupportActionBar().setTitle("Add new diet");
        foodNo = getIntent().getIntExtra("no",0);
        if(getIntent().getIntExtra("dietNo",0)!=0) {
            getSupportActionBar().setTitle("Update diet record");
            action =1;
            dietNo =getIntent().getIntExtra("dietNo",0);
            value = getIntent().getIntExtra("value",0);
            spinner.setSelection(getIntent().getIntExtra("type",0));
            btnAdd.setText("Update");
        }

        new SeekBarController(1,99,value,1,seekBar,txtNum);

        txtNum.addTextChangedListener(textWatcher);

        foodRequest.getFood(new FoodRequest.VolleyCall() {
            @Override
            public void onSuccess(Food food) {
                foodList =food;
                txtName.setText(foodList.getName());
                txtCalories.setText(String.valueOf(foodList.getCalories()*value)+ "cal");
                txtProtein.setText(String.valueOf(foodList.getProtein()*value)+ "g");
                txtFat.setText(String.valueOf(foodList.getFat()*value)+ "g");
                txtCarbohydrate.setText(String.valueOf(foodList.getCarbohydrate()*value)+ "g");
            }
        },foodNo);
    }

    public void onClick(View view) {
        String cal = txtCalories.getText().toString();
        String calNum = cal.substring(0,3);
        Diet diet = new Diet();
        diet.setFoodNo(foodNo);
        diet.setName(txtName.getText().toString());
        diet.setQty(Integer.parseInt(txtNum.getText().toString()));
        diet.setType(spinner.getSelectedItemPosition());
        try{
            diet.setCalories(Integer.parseInt(calNum.trim()));
        }catch (Exception ex){
            calNum = cal.substring(0,1);
            diet.setCalories(Integer.parseInt(calNum));
        }

      //  diet.setCalories(Integer.parseInt(calNum.trim()));
        diet.setNo(dietNo);
        switch (action){
            case 0:
                dietRequest.addDiet(new DietRequest.VolleyCallAction() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"Added Successful",Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                },diet);
                break;
            case 1:
                dietRequest.updateDiet(new DietRequest.VolleyCallAction() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"Updated Successful",Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                },diet);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,MenuActivity.class).putExtra("index",2));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
