package fitnesscompanion.com.View.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.R;


public class RankingFragment extends Fragment {

    private Context context;
    @BindView(R.id.txtRanking)
    TextView txtRank;
    private int ranking;

public RankingFragment(){

}
    @SuppressLint("ValidFragment")
    public RankingFragment(Context context, int ranking) {
        this.context = context;
        this.ranking = ranking;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);
        ButterKnife.bind(this,rootView);
        if(ranking != 0) {
            txtRank.setText(String.valueOf(ranking)+"%");
        }
        else{
            txtRank.setText("");
        }
        return rootView;
    }


}
