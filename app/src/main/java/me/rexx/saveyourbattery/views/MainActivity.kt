package me.rexx.saveyourbattery.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import me.rexx.saveyourbattery.R
import me.rexx.saveyourbattery.fragments.MainPreferenceFragment
import me.rexx.saveyourbattery.utils.PermissionUtils

class MainActivity : AppCompatActivity() {
    private lateinit var preferences: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = findViewById(R.id.preferences)

        /* If we are missing a permission, lock the user in the permission activity */
        if (!PermissionUtils.isPermissionsGranted(this, android.Manifest.permission.WRITE_SECURE_SETTINGS))
            goToPermissionActivity()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.preferences, MainPreferenceFragment())
            .commit()
    }

    private fun goToPermissionActivity() {
        val intent = Intent(this, PermissionActivity::class.java)
        startActivity(intent)
    }
}