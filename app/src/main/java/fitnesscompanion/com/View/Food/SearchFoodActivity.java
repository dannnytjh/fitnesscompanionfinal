package fitnesscompanion.com.View.Food;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fitnesscompanion.com.Adapter.searchFoodAdapter;
import fitnesscompanion.com.Model.Food;
import fitnesscompanion.com.R;
import fitnesscompanion.com.ServerRequest.FoodRequest;
import fitnesscompanion.com.View.Home.MenuActivity;

/**
 * Created by Soon Kok Fung
 */
public class SearchFoodActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.listView) ListView listView;
    private SearchView searchView;
    private FoodRequest foodRequest;
    private ArrayList<Food> foodArrayList;
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            finish();
            startActivity(new Intent(getApplicationContext(), NewFoodActivity.class).putExtra("no", foodArrayList.get(position).getNo()));
        }
    };
    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {

            foodRequest.search(new FoodRequest.VolleyCallSearch() {
                @Override
                public void onSuccess(ArrayList<Food> foodList) {
                    foodArrayList = foodList;
                    listView.setAdapter(new searchFoodAdapter(getApplicationContext(), foodList));
                    if (foodArrayList.size() == 0) {
                        Toast.makeText(getApplicationContext(), "No any food record found", Toast.LENGTH_LONG).show();
                    }
                }
            }, query);

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        ButterKnife.bind(this);
        foodRequest = new FoodRequest(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        listView.setOnItemClickListener(onItemClickListener);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setFocusable(true);
        searchView.setQueryHint(getString(R.string.fab_searchFood));
        searchView.setOnQueryTextListener(onQueryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,MenuActivity.class).putExtra("index",2));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
