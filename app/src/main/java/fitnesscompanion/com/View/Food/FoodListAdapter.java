package fitnesscompanion.com.View.Food;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fitnesscompanion.com.R;

public class FoodListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Food> foodList;

    public FoodListAdapter(Context context, int layout, ArrayList<Food> foodList) {
        this.context = context;
        this.layout = layout;
        this.foodList = foodList;
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder
    {
        ImageView imageView;
        TextView txtname, txtmeal, txtdate;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        //declare placeholder
        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtname = (TextView) row.findViewById(R.id.tvFoodName);
            holder.txtmeal = (TextView) row.findViewById(R.id.tvFoodType);
            holder.txtdate = (TextView) row.findViewById(R.id.tvFoodDate);
            holder.imageView = (ImageView) row.findViewById(R.id.imgFood);

            row.setTag(holder);
        }
        else{
            holder = (ViewHolder) row.getTag();
        }

        Food food = foodList.get(position);

        //place holder for sqlite data
        holder.txtname.setText(food.getName());
        holder.txtmeal.setText(food.getmealType());
        holder.txtdate.setText(food.getDate());
        byte[] foodImage = food.getImage();
        Bitmap bitmap= BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
        holder.imageView.setImageBitmap(bitmap);

//      Glide.with(holder.imageView.getContext()).asBitmap().load(foodImage).apply(new RequestOptions().override(300,300)).into(holder.imageView);

        return row;
    }

    @GlideModule
    public final class MyAppGlideModule extends AppGlideModule {
        @Override
        public void applyOptions(Context context, GlideBuilder builder)
        {
            builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888));
        }
    }
}
