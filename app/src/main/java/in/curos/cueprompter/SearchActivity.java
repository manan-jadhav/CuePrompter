package in.curos.cueprompter;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ProgressBar loading;
    TextView status;
    RecyclerView list;
    SearchResultAdapter adapter;

    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.search));

        loading = (ProgressBar) findViewById(R.id.loading);
        status = (TextView) findViewById(R.id.status);
        list = (RecyclerView) findViewById(R.id.list);

        adapter = new SearchResultAdapter(this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_item));

        searchView.setOnQueryTextListener(this);

        searchView.setIconified(false);
        searchView.setQuery(getString(R.string.default_search_value), true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.query = query;
        (new SearchTask()).execute();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class SearchTask extends AsyncTask<Void, Void, JSONArray> {

        // PetScan is a service which allows searching for titles on MediaWiki based sites.
        // We use it to search on WikiSource's Speeches Category
        String url = "https://petscan.wmflabs.org/?psid=606068&format=json&output_compatability=quick-intersection";

        private String appendSearchParameter(String url)
        {
            return url.concat("&regexp_filter=^(.*"+query+".*)$");
        }

        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);
            status.setVisibility(View.GONE);
            list.setVisibility(View.GONE);
        }

        @Override
        protected JSONArray doInBackground(Void... strings) {
            try {
                Log.d("FUCK YOU", appendSearchParameter(url));
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(appendSearchParameter(url))
                        .get()
                        .build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    JSONObject data = new JSONObject(response.body().string());
                    return data.getJSONArray("pages");
                } else {
                    return null;
                }

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            loading.setVisibility(View.GONE);

            if (jsonArray == null) {
                status.setText(getString(R.string.search_error));
                status.setVisibility(View.VISIBLE);
            } else {
                adapter.setArray(jsonArray);
                list.setVisibility(View.VISIBLE);
                status.setVisibility(View.GONE);
            }
        }
    }
}
