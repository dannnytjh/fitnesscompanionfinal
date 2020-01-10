package fitnesscompanion.com.ServerRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import fitnesscompanion.com.Model.Diet;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class DietRequest {
    //private final String TAG_RESULTS = "Diet";
    private String SERVER_ADDRESS;
    //private final String SERVER_ADDRESS = "http://23f88a78.eu.ngrok.io/FitnessCompanion/ServerRequest/";
    private ProgressDialog dialog;
    private RequestQueue queue;
    private Context context;
    private ArrayList<Diet> dietList;
    private int id;
    private SharedPreferences preferences;

    public DietRequest() {
    }
    public DietRequest(Context context) {
        SERVER_ADDRESS = context.getString(R.string.server_url);
        this.context=context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.id = preferences.getInt("id",0);
    }

    public void getDiet(final VolleyCall volleyCall,final String date) {
        dietList = new ArrayList<>();
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "getDietLog.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jason = new JSONArray(response);
                    Log.i("Here","getDietLog : "+response);
                    for (int x=0;x<jason.length();x++) {
                        JSONObject jsonObject = (JSONObject) jason.get(x);
                        Diet diet= new Diet();
                        //diet.setNo(jsonObject.getInt("no"));
                        diet.setType(jsonObject.getInt("type"));
                        diet.setQty(jsonObject.getInt("quantity"));
                        diet.setCalories(jsonObject.getInt("calories"));
                        diet.setName(jsonObject.getString("foodName"));

                        //diet.setFoodNo(jsonObject.getInt("foodNo"));
                        dietList.add(diet);
                    }
                    volleyCall.onSuccess(dietList);
                    dialog.dismiss();

                } catch (JSONException e) {
                    Log.i("Error","getDietLog Error : "+e.getMessage());
                    Diet diet= new Diet();
                    //diet.setNo(jsonObject.getInt("no"));
                    diet.setType(0);
                    diet.setQty(0);
                    diet.setCalories(0);
                    diet.setName("");
                    dietList.add(diet);
                    volleyCall.onSuccess(dietList);
                   // Toast.makeText(context, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("date", date);
                params.put("traineeId", String.valueOf(id));
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void addDiet(final VolleyCallAction callBack, final Diet diet){
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "newDietLog.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                callBack.onSuccess();
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
                params.put("foodName", String.valueOf(diet.getName()));
                params.put("quantity", String.valueOf(diet.getQty()));
                params.put("type", String.valueOf(diet.getType()));
                params.put("calories",String.valueOf(diet.getCalories()));
                params.put("traineeId", String.valueOf(id));

                return params;
            }
        };
        queue.add(postRequest);

    }

    public void updateDiet(final VolleyCallAction callBack,final Diet diet) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "updateDiet.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                callBack.onSuccess();
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
                params.put("qty", String.valueOf(diet.getQty()));
                params.put("type", String.valueOf(diet.getType()));
                params.put("no", String.valueOf(diet.getNo()));

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void deleteDiet(final VolleyCallAction callBack,final int no) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "deleteDiet.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                callBack.onSuccess();
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
                params.put("no", String.valueOf(no));
                return params;
            }
        };
        queue.add(postRequest);
    }

    public interface VolleyCall {
        void onSuccess(ArrayList<Diet> dietList);
    }

    public interface VolleyCallAction {
        void onSuccess();
    }
}
