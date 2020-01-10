package fitnesscompanion.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Model.GoalUI;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class GoalAdapter extends BaseAdapter {
    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.txtValue) TextView txtValue;
    @BindView(R.id.txtUnit) TextView txtUnit;

    private Context context;
    private ArrayList<GoalUI> goalUI;

    public GoalAdapter(){}
    public GoalAdapter(Context context,ArrayList<GoalUI> goalUI) {
        this.context = context;
        this.goalUI=goalUI;
    }

    @Override
    public int getCount() {
        return goalUI.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_goal,null);
        ButterKnife.bind(this, rowView);

        switch (goalUI.get(position).getType()) {
            case 0:
                txtTitle.setText("Weight");
                txtUnit.setText("KG");
                break;
            case 1:
                txtTitle.setText("Step");
                txtUnit.setText("STEP");
                break;
            case 2:
                txtTitle.setText("Calories Burn");
                txtUnit.setText("CAL");
                break;
        }
        txtValue.setText(String.valueOf(goalUI.get(position).getRate()));
        if(goalUI.get(position).getStatus()==1) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_name));
        }


        return rowView;
    }

}
