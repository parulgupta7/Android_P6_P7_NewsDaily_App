package com.example.parul.newsdaily;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.LoaderManager.LoaderCallbacks;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsDaily>> {

    final String newsRequestUrl = "http://content.guardianapis.com/search?q=pokemon&api-key=test&show-tags=contributor";
    NewsDailyAdapter adapter;
    TextView nullTextView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isInternetConnectionActive = ((networkInfo != null) && (networkInfo.isConnectedOrConnecting()));

        nullTextView = (TextView) findViewById(R.id.loading_text_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        if (isInternetConnectionActive == false) {
            progressBar.setVisibility(View.GONE);
            nullTextView.setText(getString(R.string.no_Internet_Connection));
            return;
        }

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this);

        ListView listView = (ListView) findViewById(R.id.news_list);
        adapter = new NewsDailyAdapter(this, new ArrayList<NewsDaily>());
        listView.setAdapter(adapter);
    }

    @Override
    public Loader<List<NewsDaily>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String idType = sharedPreferences.getString(getString(R.string.type_key), getString(R.string.type_default_value));
        String query = sharedPreferences.getString(getString(R.string.query_key), getString(R.string.default_query_value));

        Uri baseUri = Uri.parse(newsRequestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter(getString(R.string.type_parameter), idType);
        uriBuilder.appendQueryParameter(getString(R.string.query_parameter), query);

        return new NewsDailyLoader(this, uriBuilder.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu thisMenu) {
        getMenuInflater().inflate(R.menu.menu, thisMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int menuId = menuItem.getItemId();
        if (menuId == R.id.action_settings) {
            Intent settingIntent = new Intent(MainActivity.this, news_settings.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void onLoadFinished(Loader<List<NewsDaily>> loader, List<NewsDaily> data) {
        if (data == null) {
            nullTextView.setText(getString(R.string.no_Any_News));
            progressBar.setVisibility(View.GONE);
            return;
        }

        progressBar.setVisibility(View.GONE);
        adapter.clear();

        if (data != null && data.isEmpty() == false) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsDaily>> loader) {
        adapter.clear();
    }
}
