package fitnesscompanion.com.View.Food;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Model.Food;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.FoodRequest;
import fitnesscompanion.com.View.Home.MenuActivity;

/**
 * Created by Soon Kok Fung
 */
public class DetailFoodActivity extends AppCompatActivity {
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.txtQty) TextView txtQty;
    @BindView(R.id.txtCalories) TextView txtCalories;
    @BindView(R.id.txtProtein) TextView txtProtein;
    @BindView(R.id.txtFat) TextView txtFat;
    @BindView(R.id.txtCarbohydrate) TextView txtCarbohydrate;
    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        value = getIntent().getIntExtra("qty",0);
        txtQty.setText(String.valueOf(value));
        int foodNo = getIntent().getIntExtra("no",0);
        new FoodRequest(this).getFood(new FoodRequest.VolleyCall() {
            @Override
            public void onSuccess(Food food) {
               /* if(food.getImage().length()!=0) {
                    imageView.setImageBitmap(food.getImageFromJSon());
                }*/
                txtCalories.setText(String.valueOf(food.getCalories()*value)+ " cal");
                txtProtein.setText(String.valueOf(food.getProtein()*value)+ " g");
                txtFat.setText(String.valueOf(food.getFat()*value)+ " g");
                txtCarbohydrate.setText(String.valueOf(food.getCarbohydrate()*value)+ " g");
            }
        },foodNo);
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
