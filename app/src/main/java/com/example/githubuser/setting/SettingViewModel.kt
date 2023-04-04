package com.example.githubuser.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.local.SettingPreference
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SettingPreference) :ViewModel() {
    fun getTheme() = pref.getThemesetting().asLiveData()

    fun saveTheme(isDark: Boolean){
        viewModelScope.launch {
            pref.saveThemeSetting(isDark)
        }
    }

    class Factory(private val pref: SettingPreference) :ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T = SettingViewModel(pref) as T
    }

}