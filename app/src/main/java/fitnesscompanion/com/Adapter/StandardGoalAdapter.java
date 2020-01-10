package fitnesscompanion.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Model.StandardGoal;
import fitnesscompanion.com.R;

public class StandardGoalAdapter extends BaseAdapter {
    @BindView(R.id.standardGoalName) TextView txtStandardGoalName;
    private Context context;
    private ArrayList<StandardGoal> standardGoal;

    public StandardGoalAdapter(Context context, ArrayList<StandardGoal> goal) {
        this.context = context;
        this.standardGoal=goal;
    }

    @Override
    public int getCount() {
        return standardGoal.size();

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.adapter_standard_goal,null);
        ButterKnife.bind(this, convertView);
        txtStandardGoalName.setText(standardGoal.get(position).getGoalName());
        return convertView;
    }
}
