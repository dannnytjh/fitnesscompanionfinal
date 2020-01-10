package fitnesscompanion.com.View.Food;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Adapter.RestaurantExpandableAdapter;
import fitnesscompanion.com.Model.Coordinate;
import fitnesscompanion.com.Model.Food;
import fitnesscompanion.com.Model.Restaurant;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.FoodRequest;
import fitnesscompanion.com.View.Home.MenuActivity;

/**
 * Created by Soon Kok Fung
 */
public class RestaurantActivity extends AppCompatActivity {
    private final int REQUEST_LOCATION = 1;
    @BindView(R.id.expandableList) ExpandableListView expandableList;
    private RestaurantExpandableAdapter expandableAdapter;
    private LocationManager locationManager;
    private ExpandableListView.OnChildClickListener onChildClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
            Food food = (Food) expandableAdapter.getChild(groupPosition, childPosition);
            finish();
            startActivity(new Intent(getApplicationContext(), NewFoodActivity.class).putExtra("no", food.getNo()));
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        getSupportActionBar().setTitle("Near By Restaurant");
        ButterKnife.bind(this);
        expandableList.setOnChildClickListener(onChildClickListener);

        nearBy();
    }

    private void nearBy() {
        final Coordinate coordinate = getCurrentLocation();
        if(coordinate.getLongitude()!=0) {
            new FoodRequest(this).nearByRestaurant(new FoodRequest.VolleyCallNearBy() {
                @Override
                public void onSuccess(ArrayList<Restaurant> restaurants) {
                    if(restaurants.size()==0)
                        Toast.makeText(getApplicationContext(),getString(R.string.status_restaurant),Toast.LENGTH_LONG).show();

                    System.out.println(restaurants.size());
                    expandableAdapter = new RestaurantExpandableAdapter(getApplicationContext(),restaurants,getData(restaurants));
                    expandableList.setAdapter(expandableAdapter);

                }
            },coordinate);
        }

    }

    public Coordinate getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else{
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location!=null) {
                return new Coordinate(location.getLatitude(),location.getLongitude());
            }
            else{
                Toast.makeText(this,"Please turn on GPS",Toast.LENGTH_LONG).show();
            }
        }
        return new Coordinate(0,0);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                getCurrentLocation();
                break;
        }
    }
    /*
    private Coordinate getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return new Coordinate(location.getLatitude(),location.getLongitude());
    }*/
    private HashMap<Restaurant, ArrayList<Food>> getData(ArrayList<Restaurant> restaurants) {
        HashMap<Restaurant, ArrayList<Food>> hashMap= new HashMap<>();

        for(int x=0;x<restaurants.size();x++) {
            hashMap.put(restaurants.get(x),restaurants.get(x).getFoodList());
        }

        return hashMap;
    }
    public void onNearBy(View view) {
        nearBy();
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
