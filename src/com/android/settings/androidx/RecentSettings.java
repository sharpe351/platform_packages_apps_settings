/*
 * Copyright (C) 2012 CyanogenMod
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.androidx;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.Gravity;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class RecentSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "RecentSettings";

    private static final String RECENT_PANEL_LEFTY_MODE = "recent_panel_lefty_mode";
    private static final String RECENT_PANEL_SCALE = "recent_panel_scale";
    private static final String RECENT_PANEL_EXPANDED_MODE = "recent_panel_expanded_mode";
    private static final String RECENT_PANEL_SHOW_TOPMOST = "recent_panel_show_topmost";

    private CheckBoxPreference mRecentPanelLeftyMode;
    private ListPreference mRecentPanelScale;
    private ListPreference mRecentPanelExpandedMode;
    private CheckBoxPreference mRecentsShowTopmost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.recent_settings);

        mRecentPanelLeftyMode = (CheckBoxPreference) findPreference(RECENT_PANEL_LEFTY_MODE);
        mRecentPanelLeftyMode.setOnPreferenceChangeListener(this);

        mRecentPanelScale = (ListPreference) findPreference(RECENT_PANEL_SCALE);
        mRecentPanelScale.setOnPreferenceChangeListener(this);

        mRecentPanelExpandedMode = (ListPreference) findPreference(RECENT_PANEL_EXPANDED_MODE);
        mRecentPanelExpandedMode.setOnPreferenceChangeListener(this);

        boolean enableRecentsShowTopmost = Settings.System.getInt(getContentResolver(),
                                      Settings.System.RECENT_PANEL_SHOW_TOPMOST, 0) == 1;
        mRecentsShowTopmost = (CheckBoxPreference) findPreference(RECENT_PANEL_SHOW_TOPMOST);
        mRecentsShowTopmost.setChecked(enableRecentsShowTopmost);
        mRecentsShowTopmost.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAllPreferences();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mRecentPanelExpandedMode) {
            int value = Integer.parseInt((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.RECENT_PANEL_EXPANDED_MODE, value);
            return true;
        } else if (preference == mRecentPanelScale) {
            int value = Integer.parseInt((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.RECENT_PANEL_SCALE_FACTOR, value);
            return true;
        } else if (preference == mRecentPanelLeftyMode) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.RECENT_PANEL_GRAVITY,
                    ((Boolean) newValue) ? Gravity.LEFT : Gravity.RIGHT);
            return true;
        } else if (preference == mRecentsShowTopmost) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.RECENT_PANEL_SHOW_TOPMOST,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
        }
        return false;
    }

    private void updateSystemPreferences() {
    final boolean recentLeftyMode = Settings.System.getInt(getContentResolver(),
            Settings.System.RECENT_PANEL_GRAVITY, Gravity.RIGHT) == Gravity.LEFT;
    mRecentPanelLeftyMode.setChecked(recentLeftyMode);

    final int recentScale = Settings.System.getInt(getContentResolver(),
            Settings.System.RECENT_PANEL_SCALE_FACTOR, 100);
    mRecentPanelScale.setValue(recentScale + "");

    final int recentExpandedMode = Settings.System.getInt(getContentResolver(),
            Settings.System.RECENT_PANEL_EXPANDED_MODE, 0);
    mRecentPanelExpandedMode.setValue(recentExpandedMode + "");
    }

    private void updateAllPreferences() {
        updateSystemPreferences();
    }
}
