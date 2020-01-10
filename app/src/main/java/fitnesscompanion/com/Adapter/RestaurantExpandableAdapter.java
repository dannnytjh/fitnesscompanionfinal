package fitnesscompanion.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Model.Food;
import fitnesscompanion.com.Model.Restaurant;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class RestaurantExpandableAdapter extends BaseExpandableListAdapter {

    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.txtCal) TextView txtCal;
    @BindView(R.id.txtQty) TextView txtQty;
    @BindView(R.id.imageView)
    ImageView imageView;

    private ArrayList<Restaurant> restaurants;
    private HashMap<Restaurant, ArrayList<Food>> listData;
    private Context context;

    public RestaurantExpandableAdapter() {}

    public RestaurantExpandableAdapter(Context context,ArrayList<Restaurant> restaurants, HashMap<Restaurant, ArrayList<Food>> listData) {
        this.restaurants = restaurants;
        this.listData = listData;
        this.context = context;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.adapter_diet_header,null);
        ((TextView)convertView.findViewById(R.id.txtTitle)).setText(restaurants.get(groupPosition).getName());
        ((TextView)convertView.findViewById(R.id.txtCal)).setText(String.valueOf(restaurants.get(groupPosition).getDistance())+" M");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.adapter_diet,null);
        ButterKnife.bind(this,convertView);

        Food food = (Food) getChild(groupPosition,childPosition);
        System.out.println(food.getName());
        txtTitle.setText(food.getName());
        txtCal.setText(String.valueOf(food.getCalories())+" cal");

        if(food.getImage().length()!=0) {
            imageView.setImageBitmap(food.getImageFromJSon());
        }

        return convertView;
    }
    @Override
    public int getGroupCount() {
        return restaurants.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listData.get(restaurants.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return restaurants.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listData.get(restaurants.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
