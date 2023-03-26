package ie.wit.carerpatient.ui.settings


import android.content.Intent
import android.net.Uri

import android.os.Bundle

import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ie.wit.carerpatient.R




class SettingsFragment : PreferenceFragmentCompat() {
    //lazy values only initialised when accessed for the first time
  //  private val settingsViewModel: SettingsViewModel by activityViewModels()

    private val themeProvider by lazy { ThemeProvider(requireContext()) }
    private val themePreference by lazy {
        findPreference<ListPreference>(getString(R.string.theme_preferences_key))
    }

    private val repositorySetting by lazy {
        findPreference<Preference>(getString(R.string.repository_preference_key))
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        setThemePreference()
        openRepository()

    }

    private fun setThemePreference() {
        themePreference?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                if (newValue is String) {
                    val theme = themeProvider.getTheme(newValue)
                    AppCompatDelegate.setDefaultNightMode(theme)
                }
                true
            }
        themePreference?.summaryProvider = getThemeSummaryProvider()
    }

    private fun getThemeSummaryProvider() =
        Preference.SummaryProvider<ListPreference> { preference ->
            themeProvider.getThemeDescriptionForPreference(preference.value)
        }

    private fun openRepository() {
        repositorySetting!!.onPreferenceClickListener =
            Preference.OnPreferenceClickListener { _ ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.repository_link)))
                startActivity(intent)
                true
            }
    }
}

