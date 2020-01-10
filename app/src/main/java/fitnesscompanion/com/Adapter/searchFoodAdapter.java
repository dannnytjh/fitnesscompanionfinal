package fitnesscompanion.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Model.Food;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class searchFoodAdapter extends BaseAdapter {

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.txtName) TextView txtName;
    @BindView(R.id.txtCal) TextView txtCal;

    private ArrayList<Food> foodArrayList;
    private Context context;

    public searchFoodAdapter() {}
    public searchFoodAdapter(Context context,ArrayList<Food> foodArrayList ) {
        this.context=context;
        this.foodArrayList=foodArrayList;
    }

    @Override
    public int getCount() {
        return foodArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.adapter_search_food,null);
        ButterKnife.bind(this,convertView);

        txtName.setText(foodArrayList.get(position).getName());
        txtCal.setText(String.valueOf(foodArrayList.get(position).getCalories())+" cal");

        return convertView;
    }
}
