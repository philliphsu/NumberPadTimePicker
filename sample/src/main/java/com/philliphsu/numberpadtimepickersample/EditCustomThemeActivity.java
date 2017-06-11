package com.philliphsu.numberpadtimepickersample;

import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.view.Menu;
import android.view.MenuItem;

public class EditCustomThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new EditCustomThemeFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_custom_theme_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.reset_to_defaults:
                CustomThemeModel.get(this).resetToDefaults(this);
                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class EditCustomThemeFragment extends PreferenceFragment {
        private static final String PREF_SHOW_FAB_POLICY = "pref_show_fab_policy";
        private static final String PREF_ANIMATE_FAB_ENTRY = "pref_animate_fab_entry";
        private static final String PREF_ANIMATE_FAB_COLOR = "pref_animate_fab_color";

        private ListPreference mShowFabPolicyPreference;
        private CheckBoxPreference mAnimateFabEntryPreference;
        private CheckBoxPreference mAnimateFabColorPreference;

        private String mShowFabAlwaysValue;
        private boolean mInitialAnimateFabEntryValue;
        private boolean mInitialAnimateFabColorValue;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
            mShowFabPolicyPreference = (ListPreference) findPreference(PREF_SHOW_FAB_POLICY);
            mAnimateFabEntryPreference = (CheckBoxPreference) findPreference(PREF_ANIMATE_FAB_ENTRY);
            mAnimateFabColorPreference = (CheckBoxPreference) findPreference(PREF_ANIMATE_FAB_COLOR);

            mShowFabAlwaysValue = getResources().getString(R.string.show_fab_policy_default_value);

            mShowFabPolicyPreference.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean dependentPrefEnabled = isShowFabAlways(newValue);
                    if (!dependentPrefEnabled) {
                        // These preferences do not apply for the new PREF_SHOW_FAB_POLICY value.
                        // Save the preferences' checked states before we clear them, in case
                        // the user sets the correct value again in this Fragment instance.
                        mInitialAnimateFabEntryValue = mAnimateFabEntryPreference.isChecked();
                        mInitialAnimateFabColorValue = mAnimateFabColorPreference.isChecked();
                        mAnimateFabEntryPreference.setChecked(false);
                        mAnimateFabColorPreference.setChecked(false);
                    } else {
                        mAnimateFabEntryPreference.setChecked(mInitialAnimateFabEntryValue);
                        mAnimateFabColorPreference.setChecked(mInitialAnimateFabColorValue);
                    }
                    setPreferencesDependentOnShowFabPolicyEnabled(dependentPrefEnabled);
                    return true;  // Write to storage
                }
            });
            // Initialize the enabled states of the dependent Preferences.
            setPreferencesDependentOnShowFabPolicyEnabled(isShowFabAlways(
                    mShowFabPolicyPreference.getValue()));
        }

        /**
         * Returns whether the given preference value for PREF_SHOW_FAB_POLICY is
         * {@code SHOW_FAB_ALWAYS}.
         */
        private boolean isShowFabAlways(Object showFabPolicy) {
            return mShowFabAlwaysValue.equals(showFabPolicy);
        }

        /**
         * Sets the enabled states of the {@code Preference}s that depend on PREF_SHOW_FAB_POLICY
         * to be {@code SHOW_FAB_ALWAYS}.
         */
        private void setPreferencesDependentOnShowFabPolicyEnabled(boolean enabled) {
            mAnimateFabEntryPreference.setEnabled(enabled);
            mAnimateFabColorPreference.setEnabled(enabled);
        }
    }
}
