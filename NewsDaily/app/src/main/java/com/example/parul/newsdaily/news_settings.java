package com.example.parul.newsdaily;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

public class news_settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.news_settings_main);

            Preference id = findPreference(getString(R.string.type_key));
            bindPreferenceSummaryToValue(id);

            Preference query = findPreference(getString(R.string.query_key));
            bindPreferenceSummaryToValue(query);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String string = newValue.toString();
            preference.setSummary(string);
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), getString(R.string.Null_string));
            onPreferenceChange(preference, preferenceString);
        }

    }
}
