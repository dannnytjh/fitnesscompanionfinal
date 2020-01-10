package fitnesscompanion.com.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.R;

/**
 * Created by Soon Kok Fung
 */

public class MenuAdapter extends BaseAdapter {

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    private Context context;
    private String[] title;

    public MenuAdapter(Context context, String[] title) {
        this.context = context;
        this.title = title;
    }

    @Override
    public int getCount() {
        return title.length;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             View rowView = inflater.inflate(R.layout.adapter_menu,null);
            ButterKnife.bind(this, rowView);

            txtTitle.setText(title[position]);

        return rowView;
    }
}
