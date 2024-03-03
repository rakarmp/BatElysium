package me.rexx.saveyourbattery.services

import android.content.SharedPreferences
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.preference.PreferenceManager
import me.rexx.saveyourbattery.repositories.profiles.Profile
import me.rexx.saveyourbattery.repositories.profiles.ProfileManager
import me.rexx.saveyourbattery.utils.BatterySaverManager

open class ProfileTileService : TileService() {
    open val profile = Profile.DEFAULT

    private lateinit var batterySaverManager: BatterySaverManager

    override fun onCreate() {
        super.onCreate()
        batterySaverManager = BatterySaverManager(this)
    }

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == ProfileManager.CURRENT_PROFILE) {
            updateTile()
        }
    }

    override fun onStartListening() {
        updateTile()
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onStopListening() {
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onClick() {
        super.onClick()
        ProfileManager(this).current = profile
    }

    private fun updateTile() {
        if (ProfileManager(this).current == profile) {
            qsTile.state = Tile.STATE_ACTIVE
        } else {
            qsTile.state = Tile.STATE_INACTIVE
        }
        qsTile.updateTile()
    }
}