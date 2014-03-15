package com.android.settings.androidx;

import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.os.Bundle;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class NotificationSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "NotificationSettings";

    private static final String KEY_PEEK = "notification_peek";

    private CheckBoxPreference mNotificationPeek;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.notification_settings);

        mNotificationPeek = (CheckBoxPreference) findPreference(KEY_PEEK);
        mNotificationPeek.setPersistent(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePeekCheckbox();
    }

    private void updatePeekCheckbox() {
        boolean enabled = Settings.System.getInt(getContentResolver(),
                Settings.System.PEEK_STATE, 0) == 1;
        mNotificationPeek.setChecked(enabled);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mNotificationPeek) {
            Settings.System.putInt(getContentResolver(), Settings.System.PEEK_STATE,
                    mNotificationPeek.isChecked() ? 1 : 0);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        return true;
    }
}
