package com.example.parul.newsdaily;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QueryUtil {
    public static final String LOG_TAG = QueryUtil.class.getSimpleName();

    private static URL createUrl(String stringUrl) {
        URL thisUrl = null;
        try {
            thisUrl = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error! Parsing the URL failed. ", e);
        }
        return thisUrl;
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, "oops! This is a Bad Request. It's Error response code : " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "oops! There is a problem in retrieving the News JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static List<NewsDaily> extractingNewsFromJson(String jsonREsponse) throws JSONException {
        List<NewsDaily> newsDaily = new ArrayList<>();

        if (jsonREsponse == null)
            return null;
        try {
            JSONObject root = new JSONObject(jsonREsponse);
            JSONArray resultsJsonArray = root.getJSONObject("response").getJSONArray("results");

            for (int i = 0; i < resultsJsonArray.length(); i++) {
                JSONObject resultsJsonObject = (JSONObject) resultsJsonArray.get(i);
                String sectionName = resultsJsonObject.getString("sectionName");
                String title = resultsJsonObject.getString("webTitle");
                String date = resultsJsonObject.getString("webPublicationDate");
                String url = resultsJsonObject.getString("webUrl");

                JSONArray tagsJsonArray = resultsJsonObject.getJSONArray("tags");
                if (tagsJsonArray != null && tagsJsonArray.length() > 0) {
                    JSONObject tagsJsonObject = (JSONObject) tagsJsonArray.get(0);
                    String author = tagsJsonObject.getString("webTitle");
                    newsDaily.add(new NewsDaily(sectionName, title, date, author, url));
                } else {
                    newsDaily.add(new NewsDaily(sectionName, title, date, "N/A", url));

                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "oops! There is a problem in extracting news from JSON", e);
        }
        return newsDaily;
    }

    public static List<NewsDaily> fetchNews(String url) throws IOException, JSONException {
        URL thisUrl = createUrl(url);
        String jsonResponse = null;
        jsonResponse = makeHttpRequest(thisUrl);
        if (jsonResponse == null)
            return null;
        return extractingNewsFromJson(jsonResponse);
    }
}
