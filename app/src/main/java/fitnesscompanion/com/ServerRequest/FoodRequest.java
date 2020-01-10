package fitnesscompanion.com.ServerRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fitnesscompanion.com.Model.Coordinate;
import fitnesscompanion.com.Model.Food;
import fitnesscompanion.com.Model.Restaurant;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class FoodRequest {

    private final String TAG_RESULTS = "Food";
    private String SERVER_ADDRESS;
    //private final String SERVER_ADDRESS = "http://23f88a78.eu.ngrok.io/FitnessCompanion/ServerRequest/";
    private ProgressDialog dialog;
    private RequestQueue queue;
    private Context context;
    private ArrayList<Food> foodList;
    private ArrayList<Restaurant> restaurantList;

    public FoodRequest(Context context) {
        SERVER_ADDRESS = context.getString(R.string.server_url);
        this.context=context;
    }

    public void nearByRestaurant(final VolleyCallNearBy callBack, final Coordinate coordinate) {
        restaurantList = new ArrayList<>();
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS+"getRestaurant.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray jason = new JSONArray(response);
                    for(int x=0;x<jason.length();x++) {
                        ArrayList<Food> foods = new ArrayList<>();
                        JSONObject jsonObject = jason.getJSONObject(x);

                        JSONArray jsonArrayFood = jsonObject.getJSONArray("detail");
                        for(int y=0;y<jsonArrayFood.length();y++) {
                            JSONObject jsonFood = jsonArrayFood.getJSONObject(y);
                            foods.add(new Food(jsonFood.getInt("no"),jsonFood.getString("name"),jsonFood.getInt("cal"),jsonFood.getString("image")));
                        }
                        restaurantList.add(new Restaurant(jsonObject.getString("name"),jsonObject.getDouble("distance"),foods));
                    }
                    dialog.dismiss();
                    callBack.onSuccess(restaurantList);


                } catch (JSONException e) {
                    Toast.makeText(context, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                //Testing Coordinate(KLCC)
                params.put("lat", "3.1466");
                params.put("lng", "101.6958");
/*
                params.put("lat", String.valueOf(coordinate.getLatitude()));
                params.put("lng", String.valueOf(coordinate.getLongitude()));
*/
                return params;
            }
        };
        queue.add(postRequest);

    }

    public void barcodeSearch(final VolleyCallBarcode callBack , final String barcode) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS+"barcodeSearch.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    //JSONArray jason = new JSONArray(response);

                   JSONObject json = new JSONObject(response);
                    Log.i("Error",response);
                    //JSONObject jsonObject = jason.getJSONObject(0);
                    dialog.dismiss();
                    //callBack.onSuccess(0);
                   callBack.onSuccess(json.getInt("barcode"));

                } catch (Exception e) {
                    Log.i("Error","Error : "+e.getMessage());
                    callBack.onSuccess(0);
                  //  Toast.makeText(context, "The barcode is not recognized", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("barcode", barcode);

                return params;
            }
        };
        queue.add(postRequest);

    }

    public void search(final VolleyCallSearch callBack,final String text){

        foodList = new ArrayList<>();
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS+"searchFood.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    Log.i("Here","Response : "+response);
                    JSONArray jason = new JSONArray(response);
                    for(int x=0;x<jason.length();x++) {
                        Food food = new Food();
                        JSONObject jsonObject = jason.getJSONObject(x);
                        food.setNo(jsonObject.getInt("foodId"));
                        food.setName(jsonObject.getString("name"));
                        food.setCalories(jsonObject.getInt("calories"));
                        foodList.add(food);
                    }
                    callBack.onSuccess(foodList);
                    dialog.dismiss();


                } catch (JSONException e) {
                    Toast.makeText(context, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", text);

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void getFood(final VolleyCall volleyCall , final int no) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS+"getFood.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                   /* JSONArray jason = new JSONArray(response);

                    Food food = new Food();
                    JSONObject jsonObject = jason.getJSONObject(0);
                    food.setName(jsonObject.getString("name"));
                    food.setCalories(jsonObject.getInt("calories"));
                    food.setProtein(jsonObject.getInt("protein"));
                    food.setCarbohydrate(jsonObject.getInt("carbohydrate"));
                    food.setFat(jsonObject.getInt("fat"));*/
            JSONObject json = new JSONObject(response);
                    Food food = new Food();
                    food.setName(json.getString("name"));
                    food.setCalories(json.getInt("calories"));
                    food.setProtein(json.getInt("protein"));
                    food.setCarbohydrate(json.getInt("carbohydrate"));
                    food.setFat(json.getInt("fat"));
                    volleyCall.onSuccess(food);
                    dialog.dismiss();


                } catch (JSONException e) {
                    Toast.makeText(context, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("foodId", String.valueOf(no));

                return params;
            }
        };
        queue.add(postRequest);
    }

    public interface VolleyCall {
        void onSuccess(Food food);
    }

    public interface VolleyCallBarcode {
        void onSuccess(int no);
    }

    public interface VolleyCallSearch {
        void onSuccess(ArrayList<Food> foodList);
    }

    public interface VolleyCallNearBy {
        void onSuccess(ArrayList<Restaurant> restaurants);
    }
}
