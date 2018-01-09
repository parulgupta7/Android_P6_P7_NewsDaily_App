package com.example.parul.newsdaily;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import android.util.Log;

public class NewsDailyLoader extends AsyncTaskLoader<List<NewsDaily>> {
    String url;
    public static final String LOG_TAG = QueryUtil.class.getSimpleName();

    public NewsDailyLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsDaily> loadInBackground() {
        List<NewsDaily> list = null;
        if (url == null)
            return null;

        try {

            list = QueryUtil.fetchNews(url);

        } catch (Exception e) {
            Log.e(LOG_TAG, "oops! There is a problem in the loading of the Url.", e);
        }
        return list;
    }
}
