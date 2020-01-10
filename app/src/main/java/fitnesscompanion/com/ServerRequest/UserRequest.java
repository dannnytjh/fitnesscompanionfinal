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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fitnesscompanion.com.Database.ActivityDataDB;
import fitnesscompanion.com.Database.GoalDB;
import fitnesscompanion.com.Database.HealthProfileDB;
import fitnesscompanion.com.Database.StandardGoalDB;
import fitnesscompanion.com.Database.UserDB;
import fitnesscompanion.com.Model.ActivityData;
import fitnesscompanion.com.Model.Goal;
import fitnesscompanion.com.Model.HealthProfile;
import fitnesscompanion.com.Model.StandardGoal;
import fitnesscompanion.com.Model.User;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class UserRequest {
    private static final String TAG_RESULTS = "User";
    private String SERVER_ADDRESS;
    //private final String SERVER_ADDRESS = "http://23f88a78.eu.ngrok.io/FitnessCompanion/ServerRequest/";
    private ProgressDialog dialog;
    private Context context;
    private RequestQueue queue;
    private int id;
    private SharedPreferences preferences;
    private  ArrayList<StandardGoal> standardGoal;
    private ArrayList<Goal> goal;
    private ArrayList<HealthProfile> healthProfiles;
    private ArrayList<ActivityData> activityData;
    public UserRequest(Context context) {
        SERVER_ADDRESS = context.getString(R.string.server_url);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context=context;
        this.id = preferences.getInt("id",0);
    }

    public void checkMail(final VolleyCallCheckMail callBack,final String email) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_email),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS+"getEmail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    Log.i("Here","checkMail Request : "+response);
                    JSONObject json = new JSONObject(response);
                    dialog.dismiss();
                    callBack.onSuccess(json.getInt("email"));

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
                params.put("email", email);

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void resetPass(final String email) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context, context.getString(R.string.status_sync),
                context.getString(R.string.status_forgetPass), true, false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "recoveryPassword.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,context.getString(R.string.success_forgetPass),Toast.LENGTH_LONG).show();
                dialog.dismiss();
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
                params.put("email", email);

                return params;
            }
        };
        queue.add(postRequest);
    }

  /*  public void updateHeight(final int height) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "updateHeight.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    Toast.makeText(context,context.getString(R.string.success_update),Toast.LENGTH_LONG).show();
                dialog.dismiss();
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
                params.put("id", String.valueOf(id));
                params.put("height", String.valueOf(height));

                return params;
            }
        };
        queue.add(postRequest);
    }*/

    public void updateName(final String name) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "updateName.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,context.getString(R.string.success_update),Toast.LENGTH_LONG).show();
                dialog.dismiss();
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
                params.put("traineeId", String.valueOf(id));
                params.put("name", name);

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void updateDob(final String dob) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "updateDob.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,context.getString(R.string.success_update),Toast.LENGTH_LONG).show();
                dialog.dismiss();
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
                params.put("id", String.valueOf(id));
                params.put("dob", dob);

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void updateImage(final String image) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "updateImage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,context.getString(R.string.success_update),Toast.LENGTH_LONG).show();
                dialog.dismiss();
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
                params.put("traineeId", String.valueOf(id));
                params.put("image", image);

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void updateGender(final int gender) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "updateGender.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,context.getString(R.string.success_update),Toast.LENGTH_LONG).show();
                dialog.dismiss();
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
                params.put("traineeId", String.valueOf(id));
                params.put("gender", String.valueOf(gender));

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void updatePassword(final String password) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "updatePassword.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,context.getString(R.string.success_updatePass),Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("traineeId", String.valueOf(id));
                params.put("password", password);

                return params;
            }
        };
        queue.add(postRequest);
    }
    public void updateAddress(final String address){
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "updateAddress.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Here","updateAddress response : "+response);
                Toast.makeText(context,context.getString(R.string.success_update),Toast.LENGTH_LONG).show();
                dialog.dismiss();
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
                params.put("traineeId", String.valueOf(id));
                params.put("address", address);

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void signUp(final User user,final VolleyCallAddUser volleyCallAddUser) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

       // StringRequest postRequest = new StringRequest(Request.Method.POST, "http://i2hub.tarc.edu.my:xxxx/Project/View/Web/" + "registerTrainee.php", new Response.Listener<String>() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "registerTrainee.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Here","Register response : "+response);
                Toast.makeText(context,context.getString(R.string.success_signUp),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                volleyCallAddUser.onSuccess();
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
                params.put("name", user.getName());
                params.put("gender", String.valueOf(user.getGender()));
                params.put("email", user.getEmail());
                params.put("address",user.getAddress());
                params.put("birthDate", user.getDob());
                params.put("password", user.getPassword());
                params.put("image", user.getImage());

                return params;
            }
        };
        queue.add(postRequest);
    }
/*public void googleSignUp(final User user,final VolleyCallAddUser volleyCallAddUser){
    queue = Volley.newRequestQueue(context);
    dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
            context.getString(R.string.status_loading),true,false);

    StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "addGoogleUser.php", new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //Toast.makeText(context,context.getString(R.string.success_signUp),Toast.LENGTH_LONG).show();
            dialog.dismiss();
            volleyCallAddUser.onSuccess();
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
            params.put("name", user.getName());
            params.put("gender", String.valueOf(user.getGender()));
            params.put("image", user.getImage());
            params.put("email", user.getEmail());
            params.put("dob", user.getDob());


            return params;
        }
    };
    queue.add(postRequest);

}*/
    public void loginIn(final VolleyCallCheckActive callBack ,final String email,final String pass) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "loginRequest.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject json = new JSONObject(response);
                    Log.i("Here","login Request : "+response);
                //    int value = json.getInt("status");
                    //Toast.makeText(context,value+ " is ok", Toast.LENGTH_LONG).show();
                    //Log.i("Here",jason.getString(0));
                   // JSONObject jsonObject = new JSONObject(response).getJSONObject("status");
                    // JSONObject jsonObject = new JSONObject(response);
                 // JSONObject jsonObject = jason.getJSONObject(0);
                    dialog.dismiss();
                   callBack.onSuccess(json.getInt("status"));

                } catch (Exception e) {
                    Log.i("Here","Error : "+e.getMessage());
                    //Toast.makeText(context,  "Invalid Email or Password", Toast.LENGTH_LONG).show();
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
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", pass);
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void getUser(final VolleyCallgetUser callBack,final String email) {
        queue = Volley.newRequestQueue(context);

        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.status_login));
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setMax(30);
        dialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "getTrainee.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                    User user = new User();
                    Log.i("Here","getUser response : "+response);
                    JSONArray jason = new JSONArray(response);
                    JSONObject jsonObject = jason.getJSONObject(0);

                    user.setId(jsonObject.getInt("traineeId"));
                    user.setName(jsonObject.getString("name"));
                    user.setAddress(jsonObject.getString("address"));
                    user.setGender(jsonObject.getInt("gender"));
                    user.setDob(jsonObject.getString("birthDate"));
                    user.setImage(jsonObject.getString("image"));

                    user.setPassword(jsonObject.getString("password"));
                    user.setEmail(email);

                    UserDB userDB = new UserDB(context);
                    userDB.deleteData();
                    userDB.insertData(user);
                    dialog.setProgress(10);


                        getHealthProfile(callBack,userDB.getData().getId());


                        dialog.setProgress(30);
                        dialog.dismiss();
                       // callBack.onSuccess();

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
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", email);

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void getHealthProfile(final VolleyCallgetUser callBack,final int id) {
        queue = Volley.newRequestQueue(context);
        healthProfiles = new ArrayList<HealthProfile>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "getHealthRecord.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            dialog.setProgress(dialog.getProgress()+10);
                try{
                    JSONArray jason = new JSONArray(response);
                    Log.i("Here","Id : "+id);
                    Log.i("Here","getHealthRecord response : "+response);
                    for(int x=0;x<jason.length();x++) {
                        JSONObject jsonObject = (JSONObject) jason.get(x);

                      /*  healthProfiles.add(new HealthProfile(jsonObject.getInt("traineeId"),
                                jsonObject.getInt("weight"),jsonObject.getDouble("height"),
                                jsonObject.getString("createAt")));*/
                        healthProfiles.add(new HealthProfile(id,jsonObject.getInt("weight"),jsonObject.getDouble("height"),
                                jsonObject.getString("createAt")));
                    }
                    HealthProfileDB healthProfileDB = new HealthProfileDB(context);
                    healthProfileDB.deteleData();
                    if(jason.length()!=0) {
                        healthProfileDB.insertData(healthProfiles);
                    }
                    dialog.setProgress(20);
                    dialog.dismiss();
                    callBack.onSuccess();
                  //  getActivityData(callBack, id);

                }catch (JSONException e) {
                    Toast.makeText(context, "1Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("traineeId", String.valueOf(id));
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void getActivityData(final VolleyCallgetUser callBack,final int id) {
        queue = Volley.newRequestQueue(context);
        activityData = new ArrayList<ActivityData>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "getActivityData.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.setProgress(dialog.getProgress()+10);
                try{
                    JSONArray jason = new JSONArray(response);

                    for(int x=0;x<jason.length();x++) {
                        JSONObject jsonObject = (JSONObject) jason.get(x);

                        activityData.add(new ActivityData(jsonObject.getInt("no"),
                                jsonObject.getInt("duration"),jsonObject.getString("date"),
                                jsonObject.getInt("hr"),jsonObject.getInt("activityNo"),jsonObject.getDouble("distance")));
                    }
                    ActivityDataDB activityDataDB = new ActivityDataDB(context);
                    activityDataDB.deleteData();
                    if(jason.length()!=0) {

                        activityDataDB.insertData(activityData);
                    }
                    dialog.setProgress(30);
                    dialog.dismiss();
                    //getGoalData(callBack, id);

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
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id", String.valueOf(id));

                return params;
            }
        };
        queue.add(postRequest);
    }
public void getStandardGoal(final VolleyCallgetUser callBack){
    standardGoal = new ArrayList<StandardGoal>();
    queue = Volley.newRequestQueue(context);
  //  JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(SERVER_ADDRESS + "getActivity.php", new Response.Listener<JSONArray>() {
    JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(SERVER_ADDRESS + "getStandardGoal.php", new Response.Listener<JSONArray>() {
    @Override
        public void onResponse(JSONArray response) {

            try{
                for(int x=0;x<response.length();x++) {
                    JSONObject jsonObject = (JSONObject) response.get(x);
                        /*activities.add(new Activity(jsonObject.getInt("activityId"),jsonObject.getString("name"),
                                jsonObject.getString("description"),jsonObject.getInt("caloriesBurnPerMin")
                                ,jsonObject.getInt("suggestedDuration"),jsonObject.getString("image")));*/
                    standardGoal.add(new StandardGoal(jsonObject.getInt("standardGoalId"),jsonObject.getString("goalName"),
                            jsonObject.getString("createAt"),jsonObject.getInt("foodIntake")
                            ,jsonObject.getInt("activityDuration")));
                }


                StandardGoalDB standardGoalDB = new StandardGoalDB(context);
                standardGoalDB.deleteData();
                standardGoalDB.insertData(standardGoal);
                callBack.onSuccess();

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
    public void getGoalData(final VolleyCallgetUser callBack) {
        goal = new ArrayList<Goal>();
        standardGoal = new ArrayList<StandardGoal>();
        queue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "getGoal.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               /* int step=0;
                int weight =0;
                int cal =0;*/
               // dialog.setProgress(dialog.getProgress()+10);
                try{
                    JSONArray jason = new JSONArray(response);
                    Log.i("Here","getGoal response : "+response);
                    for(int x=0;x<jason.length();x++) {
                        JSONObject jsonObject = (JSONObject) jason.get(x);
                        goal.add(new Goal(jsonObject.getInt("goalId"),jsonObject.getInt("type"),jsonObject.getString("description"),jsonObject.getInt("standardGoalId")));
                     //  standardGoal.add(new StandardGoal(jsonObject.getInt("standardGoalId")));

                        GoalDB goalDB = new GoalDB(context);
                        goalDB.deteleData();
                        goalDB.insertData(goal);
                       // step = jsonObject.getInt("step");
                        //weight = jsonObject.getInt("weight");
                        //cal = jsonObject.getInt("cal");
                    }

                  /*  if(step!=0&weight!=0&cal!=0) {
                        GoalDB goalDB = new GoalDB(context);
                        goalDB.insertWeight(new Goal(weight));
                        goalDB.insertStep(new Goal(step));
                        goalDB.insertCal(new Goal(cal));
                    }*/
                   /* dialog.setProgress(40);
                    dialog.dismiss();*/
                   getStandardGoal(callBack);
                    callBack.onSuccess();

                }catch (JSONException e) {
                    GoalDB goalDB = new GoalDB(context);
                    goalDB.deteleData();
                    callBack.onSuccess();
                    //Toast.makeText(context, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                   // dialog.dismiss();
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
                params.put("traineeId", String.valueOf(id));

                return params;
            }
        };
        queue.add(postRequest);
    }
public void insertGoal(final Goal goal,final StandardGoal standardGoal){
    queue = Volley.newRequestQueue(context);
    dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
            context.getString(R.string.status_loading),true,false);

    StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "registerGoal.php", new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i("Here",response);
            Log.i("Here", String.valueOf(id));
            Toast.makeText(context,context.getString(R.string.success_update),Toast.LENGTH_LONG).show();
            dialog.dismiss();
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
            params.put("traineeId",String.valueOf(id));
            params.put("standardGoalId", String.valueOf(standardGoal.getStandardGoalId()));
            params.put("measurement", String.valueOf(goal.getMeasurement()));
            params.put("type", String.valueOf(goal.getType()));
            params.put("description",goal.getDescription());
            return params;
        }
    };
    queue.add(postRequest);

}
    public void updateGoal(final Goal goal,final StandardGoal standardGoal){
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context,context.getString(R.string.status_sync),
                context.getString(R.string.status_loading),true,false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "updateGoal.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,context.getString(R.string.success_update),Toast.LENGTH_LONG).show();
                dialog.dismiss();
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
                params.put("traineeId",String.valueOf(id));
                params.put("standardGoalId", String.valueOf(standardGoal.getStandardGoalId()));
                params.put("measurement", String.valueOf(goal.getMeasurement()));
                params.put("type", String.valueOf(goal.getType()));
                params.put("description",goal.getDescription());
                return params;
            }
        };
        queue.add(postRequest);

    }

    public interface VolleyCallAddUser {
        void onSuccess();
    }

    public interface VolleyCallCheckMail {
        void onSuccess(int count);
    }

    public interface VolleyCallCheckActive {
        void onSuccess(int active);
    }

    public interface VolleyCallgetUser {
        void onSuccess();
    }
}
