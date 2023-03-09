package com.elvinliang.aviation.data

import android.content.SharedPreferences
import com.elvinliang.aviation.presentation.component.settings.SettingsConfig
import com.google.gson.Gson

class ConfigRepositoryImpl(
    private val sharedPreference: SharedPreferences
): ConfigRepository {
    override fun saveConfig(settingsConfig: SettingsConfig) {
        sharedPreference.edit().putString("user", Gson().toJson(settingsConfig)).apply()
    }

    override fun getConfig(): SettingsConfig {
        val data = sharedPreference.getString("user", null) ?: return SettingsConfig()
        return Gson().fromJson(data, SettingsConfig::class.java)
    }
}

interface ConfigRepository {
    fun saveConfig(settingsConfig: SettingsConfig)
    fun getConfig(): SettingsConfig
}
