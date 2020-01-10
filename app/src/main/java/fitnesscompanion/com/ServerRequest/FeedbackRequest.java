package fitnesscompanion.com.ServerRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import fitnesscompanion.com.Model.Feedback;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class FeedbackRequest {

    private static final String TAG_RESULTS = "Feedback";
    private final String SERVER_ADDRESS = "https://taruc.000webhostapp.com/FitnessV2/";
    private ProgressDialog dialog;
    private Context context;
    private RequestQueue queue;
    private SharedPreferences preferences;

    public FeedbackRequest(Context context) {
        this.context=context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void addFeedback(final Feedback feedback) {
        queue = Volley.newRequestQueue(context);
        dialog = ProgressDialog.show(context, context.getString(R.string.status_sync),
                context.getString(R.string.status_loading), true, false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "addFeedback.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,context.getString(R.string.success_feedback),Toast.LENGTH_LONG).show();
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
                params.put("desc", feedback.getDesc());
                params.put("rate", String.valueOf(feedback.getRate()));
                params.put("id",String.valueOf(preferences.getInt("id",0)));
                return  params;
            }
        };
        queue.add(postRequest);
    }
}
