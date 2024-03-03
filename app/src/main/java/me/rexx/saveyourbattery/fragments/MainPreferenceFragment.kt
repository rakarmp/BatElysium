package me.rexx.saveyourbattery.fragments

import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.preference.*
import me.rexx.saveyourbattery.R
import me.rexx.saveyourbattery.models.BatterySaverConstantsConfig
import me.rexx.saveyourbattery.repositories.profiles.Profile
import me.rexx.saveyourbattery.repositories.profiles.ProfileManager
import me.rexx.saveyourbattery.utils.BatterySaverManager
import com.google.android.material.snackbar.Snackbar

class MainPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var batterySaverManager: BatterySaverManager
    private lateinit var profileManager: ProfileManager

    private lateinit var profileDefault: SwitchPreference
    private lateinit var profileLight: SwitchPreference
    private lateinit var profileModerate: SwitchPreference
    private lateinit var profileHigh: SwitchPreference
    private lateinit var profileExtreme: SwitchPreference
    private lateinit var export: Preference
    private lateinit var import: Preference
    private lateinit var advertiseIsEnabled: SwitchPreference
    private lateinit var dataSaverEnabled: SwitchPreference
    private lateinit var enableNightMode: SwitchPreference
    private lateinit var launchBoostEnabled: SwitchPreference
    private lateinit var vibrationEnabled: SwitchPreference
    private lateinit var animationEnabled: SwitchPreference
    private lateinit var soundTriggerEnabled: SwitchPreference
    private lateinit var fullBackupDeferred: SwitchPreference
    private lateinit var keyValueBackupDeferred: SwitchPreference
    private lateinit var fireWallEnabled: SwitchPreference
    private lateinit var gpsMode: ListPreference
    private lateinit var adjustBrightnessEnabled: SwitchPreference
    private lateinit var adjustBrightnessFactor: SeekBarPreference
    private lateinit var forceAllAppsStandby: SwitchPreference
    private lateinit var forceBackgroundCheck: SwitchPreference
    private lateinit var optionalSensorsEnabled: SwitchPreference
    private lateinit var aodEnabled: SwitchPreference
    private lateinit var quickDozeEnabled: SwitchPreference
    private lateinit var restore: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main, rootKey)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        batterySaverManager = BatterySaverManager(context)
        profileManager = ProfileManager(context)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        profileDefault = findPreference(getString(R.string.pref_profile_key_default))!!
        profileLight = findPreference(getString(R.string.pref_profile_key_light))!!
        profileModerate = findPreference(getString(R.string.pref_profile_key_moderate))!!
        profileHigh = findPreference(getString(R.string.pref_profile_key_high))!!
        profileExtreme = findPreference(getString(R.string.pref_profile_key_extreme))!!
        export = findPreference(getString(R.string.pref_key_export))!!
        import = findPreference(getString(R.string.pref_key_import))!!
        advertiseIsEnabled = findPreference(getString(R.string.pref_config_key_advertise_is_enabled))!!
        dataSaverEnabled = findPreference(getString(R.string.pref_config_key_datasaver_enabled))!!
        enableNightMode = findPreference(getString(R.string.pref_config_key_enable_night_mode))!!
        launchBoostEnabled = findPreference(getString(R.string.pref_config_key_launch_boost_enabled))!!
        vibrationEnabled = findPreference(getString(R.string.pref_config_key_vibration_enabled))!!
        animationEnabled = findPreference(getString(R.string.pref_config_key_animation_enabled))!!
        soundTriggerEnabled = findPreference(getString(R.string.pref_config_key_soundtrigger_enabled))!!
        fullBackupDeferred = findPreference(getString(R.string.pref_config_key_fullbackup_deferred))!!
        keyValueBackupDeferred = findPreference(getString(R.string.pref_config_key_keyvaluebackup_deferred))!!
        fireWallEnabled = findPreference(getString(R.string.pref_config_key_firewall_enabled))!!
        gpsMode = findPreference(getString(R.string.pref_config_key_gps_mode))!!
        adjustBrightnessEnabled = findPreference(getString(R.string.pref_config_key_adjust_brightness_enabled))!!
        adjustBrightnessFactor = findPreference(getString(R.string.pref_config_key_adjust_brightness_factor))!!
        forceAllAppsStandby = findPreference(getString(R.string.pref_config_key_force_all_apps_standby))!!
        forceBackgroundCheck = findPreference(getString(R.string.pref_config_key_force_background_check))!!
        optionalSensorsEnabled = findPreference(getString(R.string.pref_config_key_optional_sensors_enabled))!!
        aodEnabled = findPreference(getString(R.string.pref_config_key_aod_enabled))!!
        quickDozeEnabled = findPreference(getString(R.string.pref_config_key_quick_doze_enabled))!!
        restore = findPreference(getString(R.string.pref_config_key_restore))!!

        refreshSettings()
        lockSettings()

        /* Alert the user that LPM is not enabled */
        if (!batterySaverManager.getLowPower())
            Snackbar.make(requireView(), getString(R.string.snackbar_low_power), Snackbar.LENGTH_SHORT).show()

        /* On import text changed, apply the new configuration */
        findPreference<EditTextPreference>(getString(R.string.pref_key_import))?.let {
            it.setOnPreferenceChangeListener { _, newValue ->
                batterySaverManager.apply(BatterySaverConstantsConfig().apply { import(newValue as String) })
                refreshSettings()
                return@setOnPreferenceChangeListener true
            }
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.pref_profile_key_default) -> {
                profileManager.current = Profile.DEFAULT
                Snackbar.make(requireView(), getString(R.string.snackbar_reset), Snackbar.LENGTH_SHORT).show()
                refreshSettings()
            }
            getString(R.string.pref_profile_key_light) -> {
                profileManager.current = Profile.LIGHT
                Snackbar.make(requireView(), getString(R.string.snackbar_applied), Snackbar.LENGTH_SHORT).show()
                refreshSettings()
            }
            getString(R.string.pref_profile_key_moderate) -> {
                profileManager.current = Profile.MODERATE
                Snackbar.make(requireView(), getString(R.string.snackbar_applied), Snackbar.LENGTH_SHORT).show()
                refreshSettings()
            }
            getString(R.string.pref_profile_key_high) -> {
                profileManager.current = Profile.HIGH
                Snackbar.make(requireView(), getString(R.string.snackbar_applied), Snackbar.LENGTH_SHORT).show()
                refreshSettings()
            }
            getString(R.string.pref_profile_key_extreme) -> {
                profileManager.current = Profile.EXTREME
                Snackbar.make(requireView(), getString(R.string.snackbar_applied), Snackbar.LENGTH_SHORT).show()
                refreshSettings()
            }

            getString(R.string.pref_key_export) -> exportSettings()
            getString(R.string.pref_key_import) -> (preference as EditTextPreference).text = batterySaverManager.getConstantsString()
            getString(R.string.pref_config_key_restore) -> {
                profileManager.restore()
                Snackbar.make(requireView(), getString(R.string.snackbar_applied), Snackbar.LENGTH_SHORT).show()
                refreshSettings()
            }

            getString(R.string.pref_source_key) -> openURL(getString(R.string.source_url))
            else -> return super.onPreferenceTreeClick(preference)
        }
        return true
    }

    /**
     * Lock specific settings if the Android version does not support it
     */
    private fun lockSettings() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            advertiseIsEnabled.isVisible = false
            dataSaverEnabled.isVisible = false
            enableNightMode.isVisible = false
            launchBoostEnabled.isVisible = false
            forceAllAppsStandby.isVisible = false
            forceBackgroundCheck.isVisible = false
            optionalSensorsEnabled.isVisible = false
            aodEnabled.isVisible = false
            quickDozeEnabled.isVisible = false
        }
    }

    /**
     * Show the user a dialog where they can export the constants
     */
    private fun exportSettings() {
        val intent = Intent()
            .setAction(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_TEXT, batterySaverManager.getConstantsString())
            .setType("text/plain")
        val chooser = Intent.createChooser(intent, getString(R.string.export_share_title))
        startActivity(chooser)
    }

    /**
     * Update the UI to show the new constants
     */
    private fun refreshSettings() {
        /* Take existing constants and apply to the default config as overrides */
        val currentProfileString = batterySaverManager.getConstantsString()
        val currentProfile = BatterySaverConstantsConfig().also {
            if (currentProfileString != null)
                it.import(currentProfileString)
        }

        val profile = context?.let { ProfileManager(it).current } ?: Profile.DEFAULT
        profileDefault.isChecked = profile == Profile.DEFAULT
        profileLight.isChecked = profile == Profile.LIGHT
        profileModerate.isChecked = profile == Profile.MODERATE
        profileHigh.isChecked = profile == Profile.HIGH
        profileExtreme.isChecked = profile == Profile.EXTREME

        listOf(
            export,
            import,
            advertiseIsEnabled,
            dataSaverEnabled,
            enableNightMode,
            launchBoostEnabled,
            vibrationEnabled,
            animationEnabled,
            soundTriggerEnabled,
            fullBackupDeferred,
            keyValueBackupDeferred,
            fireWallEnabled,
            gpsMode,
            adjustBrightnessEnabled,
            adjustBrightnessFactor,
            forceAllAppsStandby,
            forceBackgroundCheck,
            optionalSensorsEnabled,
            aodEnabled,
            quickDozeEnabled,
            restore
        ).forEach {
            it.isEnabled = profile != Profile.DEFAULT
        }

        advertiseIsEnabled.isChecked = currentProfile.advertiseIsEnabled
        dataSaverEnabled.isChecked = !currentProfile.dataSaverDisabled
        enableNightMode.isChecked = currentProfile.enableNightMode
        launchBoostEnabled.isChecked = !currentProfile.launchBoostDisabled
        vibrationEnabled.isChecked = !currentProfile.vibrationDisabled
        animationEnabled.isChecked = !currentProfile.animationDisabled
        soundTriggerEnabled.isChecked = !currentProfile.soundTriggerDisabled
        fullBackupDeferred.isChecked = currentProfile.fullBackupDeferred
        keyValueBackupDeferred.isChecked = currentProfile.keyValueBackupDeferred
        fireWallEnabled.isChecked = !currentProfile.fireWallDisabled
        gpsMode.value = currentProfile.gpsMode.toString()
        adjustBrightnessEnabled.isChecked = !currentProfile.adjustBrightnessDisabled
        adjustBrightnessFactor.value = (currentProfile.adjustBrightnessFactor * 100).toInt()
        forceAllAppsStandby.isChecked = currentProfile.forceAllAppsStandby
        forceBackgroundCheck.isChecked = currentProfile.forceBackgroundCheck
        optionalSensorsEnabled.isChecked = !currentProfile.optionalSensorsDisabled
        aodEnabled.isChecked = !currentProfile.aodDisabled
        quickDozeEnabled.isChecked = currentProfile.quickDozeEnabled
    }

    /**
     * Take the UI settings and apply them as constants
     */
    private fun applySettings() {
        (profileManager.current == Profile.DEFAULT) && return
        val config = BatterySaverConstantsConfig(
            advertiseIsEnabled.isChecked,
            !dataSaverEnabled.isChecked,
            enableNightMode.isChecked,
            !launchBoostEnabled.isChecked,
            !vibrationEnabled.isChecked,
            !animationEnabled.isChecked,
            !soundTriggerEnabled.isChecked,
            fullBackupDeferred.isChecked,
            keyValueBackupDeferred.isChecked,
            !fireWallEnabled.isChecked,
            gpsMode.value.toInt(),
            !adjustBrightnessEnabled.isChecked,
            adjustBrightnessFactor.value / 100f,
            forceAllAppsStandby.isChecked,
            forceBackgroundCheck.isChecked,
            !optionalSensorsEnabled.isChecked,
            !aodEnabled.isChecked,
            quickDozeEnabled.isChecked
        )
        try {
            batterySaverManager.apply(config)
        } catch (e: Exception) {
            e.printStackTrace()
            Snackbar.make(requireView(), getString(R.string.snackbar_failed_to_apply), Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Open a URL for the user
     */
    private fun openURL(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Snackbar.make(requireView(), getString(R.string.snackbar_intent_failed), Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * When settings are changed, apply the new config
     */
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key?.startsWith(ProfileManager.PREF_PREFIX) == true && return
        applySettings()
    }
}