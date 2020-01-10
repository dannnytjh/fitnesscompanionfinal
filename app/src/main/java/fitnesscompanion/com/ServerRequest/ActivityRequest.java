package fitnesscompanion.com.ServerRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fitnesscompanion.com.Database.ActivityDB;
import fitnesscompanion.com.Model.Activity;
import fitnesscompanion.com.Model.ActivityAssigned;
import fitnesscompanion.com.Model.ActivityData;
import fitnesscompanion.com.Model.Ranking;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class ActivityRequest {
    private final String TAG_RESULTS = "Activity";
    private String SERVER_ADDRESS;
    //private final String SERVER_ADDRESS = "http://23f88a78.eu.ngrok.io/FitnessCompanion/ServerRequest/";
    private ProgressDialog dialog;
    private RequestQueue queue;
    private Context context;
    private ArrayList<Activity> activities;
    private ProgressBar progressBar;
    private ArrayList<Ranking> rankings;
    private int id;
    private SharedPreferences preferences;
    private ArrayList<ActivityAssigned> assignedArrayList;

    public ActivityRequest(Context context) {
        SERVER_ADDRESS = context.getString(R.string.server_url);
        this.context=context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.id = preferences.getInt("id",0);
    }

    /*public void getVersion(final VolleyCallVer volleyCallVer){
        queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(SERVER_ADDRESS+"getVersion.php", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {

                    JSONObject jsonObject = (JSONObject) response.get(0);
                    volleyCallVer.onSuccess(jsonObject.getInt("ver"));


                }catch(Exception e) {
                    Toast.makeText(context, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setTag(TAG_RESULTS);
        queue.add(jsonObjectRequest);

    }
*/
    public void getActivity(final VolleyCallActivity callActivity, final ProgressBar progressBar) {
        activities = new ArrayList<Activity>();
        queue = Volley.newRequestQueue(context);

      /*  JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                "http://i2hub.tarc.edu.my:xxxx/Project/View/Web/" + "getActivity.php",*/

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(SERVER_ADDRESS + "getActivity.php", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try{
                    for(int x=0;x<response.length();x++) {

                        JSONObject jsonObject = (JSONObject) response.get(x);
                        activities.add(new Activity(jsonObject.getInt("activityId"),jsonObject.getString("name"),
                                jsonObject.getString("description"),jsonObject.getInt("caloriesBurnPerMin")
                                ,jsonObject.getInt("suggestedDuration"),jsonObject.getString("image")));
                       /* activities.add(new Activity(jsonObject.getInt("activityId"),jsonObject.getString("name"),
                                jsonObject.getString("desc"),jsonObject.getInt("calories")
                                ,jsonObject.getInt("time"),jsonObject.getString("image")));*/
                        progressBar.setProgress(progressBar.getProgress()+ 90/response.length());
                    }
                    ActivityDB activityDB = new ActivityDB(context);
                    activityDB.deleteData();
                    activityDB.insertData(activities);
                    callActivity.onSuccess();


                }catch(Exception e) {
                    Log.i("Here","onResponse");
                    Toast.makeText(context, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Here","onerrorresponse");
                Toast.makeText(context, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        jsonObjectRequest.setShouldCache(false);
        jsonObjectRequest.setTag(TAG_RESULTS);
        queue.add(jsonObjectRequest);
    }

    public void getRanking(final VolleyCallRanking callBack,final int no) {
        rankings =new ArrayList<>();
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);
        queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "getRanking.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.i("Here","Ranking response : "+response);
                    JSONObject json = new JSONObject(response);

                    dialog.dismiss();
                    callBack.onSuccess(json.getInt("result"));
                   /* JSONArray jason = new JSONArray(response);

                    for(int x=0;x<jason.length();x++) {
                        JSONObject jsonObject = jason.getJSONObject(x);
                        rankings.add(new Ranking(jsonObject.getString("traineeId"),jsonObject.getInt("caloriesBurn")));
                    }
                    dialog.dismiss();
                    callBack.onSuccess(rankings);*/

                } catch (JSONException e) {
                    Log.i("Error","Ranking error : "+e.getMessage());
                    Toast.makeText(context, "You did not perform any activity yet", Toast.LENGTH_LONG).show();
                    callBack.onSuccess(0);
                    dialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("activityId", String.valueOf(no));
                params.put("traineeId",String.valueOf(id));
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void addActivityData(final VolleyCallData callBack,final ActivityData activityData) {
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);
        queue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "addActivityLog.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("Here","addActivity response : "+response);
                dialog.dismiss();
                callBack.onSuccess();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("type",String.valueOf(1));
                params.put("activityId", String.valueOf(activityData.getActivityNo()));
                params.put("duration", String.valueOf(activityData.getDuration()));
                params.put("distance",String.valueOf(activityData.getDistance()));
                //params.put("hr", String.valueOf(activityData.getHr()));
                params.put("traineeId", String.valueOf(id));
                return params;

            }
        };
        queue.add(postRequest);
    }

    public void getAssigned(final VolleyCallAssigned callBack) {
        assignedArrayList = new ArrayList<>();
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);
        queue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "getAssignedActivity.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jason = new JSONArray(response);
                    for(int x=0; x<jason.length(); x++) {
                        JSONObject jsonObject = (JSONObject) jason.get(x);
                        ActivityAssigned activityAssigned = new ActivityAssigned();
                        ArrayList<Activity> activityList = new ArrayList<>();
                        activityAssigned.setNo(jsonObject.getInt("activityId"));
                        activityAssigned.setName(jsonObject.getString("description"));
                        Activity activity = new Activity();
                        activity.setNo(jsonObject.getInt("activityId"));
                        activity.setTime(jsonObject.getInt("frequency"));
                        activity.setName(new ActivityDB(context).getActivity(jsonObject.getInt("activityId")).getName());
                      //  activity.setImage(new ActivityDB(context).getActivity(activity.getNo()).getImage());
                        activity.setCalories(new ActivityDB(context).getActivity(jsonObject.getInt("activityId")).getCalories());
                        activityList.add(activity);
                      /*  JSONArray jsonArray = jsonObject.getJSONArray("workout");

                        for (int y=0; y<jsonArray.length(); y++) {
                            JSONObject jsonWorkout = (JSONObject) jsonArray.get(y);
                            Activity activity = new Activity();
                            activity.setNo(jsonWorkout.getInt("activityId"));
                            activity.setTime(jsonWorkout.getInt("frequency"));
                            activity.setName(new ActivityDB(context).getActivity(activity.getNo()).getName());
                            activity.setImage(new ActivityDB(context).getActivity(activity.getNo()).getImage());
                            activity.setCalories(new ActivityDB(context).getActivity(activity.getNo()).getCalories());
                            activityList.add(activity);
                        }*/
                        activityAssigned.setActivities(activityList);

                        assignedArrayList.add(activityAssigned);
                    }
                    callBack.onSuccess(assignedArrayList);
                    dialog.dismiss();


                }catch (JSONException e) {
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
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("traineeId", String.valueOf(id));

                return params;
            }
        };
        queue.add(postRequest);

    }

    public interface VolleyCallVer {
        void onSuccess(int version);
    }

    public interface VolleyCallActivity {
        void onSuccess();
    }

    public interface VolleyCallRanking {
        void onSuccess(int ranking);
    }

    public interface VolleyCallData {
        void onSuccess();
    }

    public interface VolleyCallAssigned {
        void onSuccess(ArrayList<ActivityAssigned> assignedArrayList);
    }
}
