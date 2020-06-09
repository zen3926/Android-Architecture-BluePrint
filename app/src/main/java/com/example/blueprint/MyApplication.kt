package com.example.blueprint

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.Settings
import android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED
import timber.log.Timber
import timber.log.Timber.DebugTree

class MyApplication : Application() {
    override fun onCreate() {
        enabledStrictModeIfNeeded()
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }

    private fun enabledStrictModeIfNeeded() {
        if (!BuildConfig.DEBUG) return

        val isDeveloperModeEnabled = Settings.Secure
            .getInt(contentResolver, DEVELOPMENT_SETTINGS_ENABLED, 0) != 0

        if (!isDeveloperModeEnabled) return

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork() // or .detectAll() for all detectable problems
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }
}
