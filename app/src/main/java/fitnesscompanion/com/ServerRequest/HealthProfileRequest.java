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

import java.util.HashMap;
import java.util.Map;

import fitnesscompanion.com.Model.HealthProfile;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class HealthProfileRequest {
    private final String TAG_RESULTS = "Activity";
    private String SERVER_ADDRESS;
    //private final String SERVER_ADDRESS = "http://23f88a78.eu.ngrok.io/FitnessCompanion/ServerRequest/";
    private ProgressDialog dialog;
    private RequestQueue queue;
    private Context context;
    private SharedPreferences preferences;

    public HealthProfileRequest(Context context) {
        SERVER_ADDRESS = context.getString(R.string.server_url);
        this.context=context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void insertWeight(HealthProfile healthProfile) {
        insertData(healthProfile);
    }

    public void updateStep(final int step){
        queue = Volley.newRequestQueue(context);
        Log.v("server","good");
        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "updateStep.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("server",String.valueOf(preferences.getInt("id",0)));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("step", String.valueOf(step));
                params.put("id",String.valueOf(preferences.getInt("id",0)));
                return  params;
            }
        };
        queue.add(postRequest);

    }

    public void insertStep(final int step){
        queue = Volley.newRequestQueue(context);
        Log.v("server","good");
        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "updateHealthProfile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("server",String.valueOf(preferences.getInt("id",0)));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("rate", String.valueOf(step));
                params.put("type", String.valueOf(1));
                params.put("id",String.valueOf(preferences.getInt("id",0)));
                return  params;
            }
        };
        queue.add(postRequest);

    }

    public void insertHeartRate(HealthProfile healthProfile) {

    }

    private void insertData(final HealthProfile healthProfile) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context, context.getString(R.string.status_sync),
                context.getString(R.string.status_loading), true, false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "updateHealthProfile.php", new Response.Listener<String>() {
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
        })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
               /* params.put("rate", String.valueOf(healthProfile.getRate()));
                params.put("type", String.valueOf(healthProfile.getType()));*/
                params.put("id",String.valueOf(preferences.getInt("id",0)));
                return  params;
            }
        };
        queue.add(postRequest);
    }

    public void insertHealthRecord(final VolleyCall callBack,final int id,final double height,final int weight) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context, context.getString(R.string.status_sync),
                context.getString(R.string.status_loading), true, false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "registerHealthRecord.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,context.getString(R.string.success_update),Toast.LENGTH_LONG).show();
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
                params.put("height", String.valueOf(height));
                params.put("weight", String.valueOf(weight));
                params.put("traineeId",String.valueOf(id));
                return  params;
            }
        };
        queue.add(postRequest);
    }

    public interface VolleyCall {
        void onSuccess();
    }
}
