package com.shongywong.mewviecat;

import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by shongywong on 11/3/2016.
 */
public class FilterActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

        if(savedInstance == null)
        {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new FilterFragment())
                    .commit();
        }
    }

    public static class FilterFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener
    {
        @Override
        public void onCreate(Bundle savedInstance)
        {
            super.onCreate(savedInstance);
            addPreferencesFromResource(R.xml.pref_general);
            bindPreferenceSummaryToValue(findPreference("filter"));
        }

        private void bindPreferenceSummaryToValue(Preference preference)
        {
            preference.setOnPreferenceChangeListener(this);
            onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                    .getString(preference.getKey(), ""));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o)
        {
            String value = o.toString();

            if (preference instanceof ListPreference)
            {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(value);
                if (index >= 0)
                {
                    preference.setSummary(listPreference.getEntries()[index]);
                }
            }

            return true;
        }
    }
}
