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
import fitnesscompanion.com.Model.Ranking;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class RankingAdapter extends BaseAdapter {

    @BindView(R.id.txtNum) TextView txtNum;
    @BindView(R.id.txtName) TextView txtName;
    @BindView(R.id.txtCal) TextView txtCal;

    private ArrayList<Ranking> rankings;
    private Context context;
    public RankingAdapter(Context context,ArrayList<Ranking> rankings) {
        this.context=context;
        this.rankings=rankings;
    }

    @Override
    public int getCount() {
        return rankings.size();
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
        View rowView = inflater.inflate(R.layout.adapter_ranking,null);
        ButterKnife.bind(this, rowView);

        txtNum.setText(String.valueOf(position+1)+". ");
        txtName.setText(rankings.get(position).getName());
        txtCal.setText(String.valueOf(rankings.get(position).getCalories())+" cal");

        return rowView;
    }
}
