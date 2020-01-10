package fitnesscompanion.com.View.Profile.Graph;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fitnesscompanion.com.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LineChartFragment extends Fragment {


    public LineChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_line_chart, container, false);
    }

}
