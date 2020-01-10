package fitnesscompanion.com.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import fitnesscompanion.com.R;


/**
 * Created by Soon Kok Fung
 */

public class ConnectionDetector {
    private Context context;
    public ConnectionDetector(Context context) {
        this.context=context;
    }


    public boolean isConnected() {
        if(!haveNetworkConnection()) {
            Toast.makeText(context,context.getString(R.string.error_internet),Toast.LENGTH_LONG).show();
            return false;
        }
       return true;
    }
    public boolean haveNetworkConnection()
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
