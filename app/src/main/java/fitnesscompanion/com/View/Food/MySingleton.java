package fitnesscompanion.com.View.Food;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

//server singleton volley upload method
public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySingleton(Context context)
    {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {

        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context){
        if (mInstance == null)
        {
            mInstance = new MySingleton(context);
        }
        return mInstance;

    }

    public <T> void addToRequestQue (Request<T> request){
        getRequestQueue().add(request);
    }

}
