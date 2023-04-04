package com.example.githubuser.ui


import android.util.Log
import androidx.lifecycle.*
import com.example.githubuser.Fav.FavoritrViewModel
import com.example.githubuser.data.local.DbModul
import com.example.githubuser.data.local.SettingPreference
import com.example.githubuser.util.Result
import com.example.githubuser.data.networks.ApiConfig
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel (private val preference: SettingPreference)  : ViewModel(){

    val resultUser = MutableLiveData<Result>()

    fun getTheme() = preference.getThemesetting().asLiveData()

    fun getUser() {
        viewModelScope.launch{
                flow {
                    val response = ApiConfig
                        .apiService
                        .getUser()

                    emit(response)

                }.onStart {
                    resultUser.value = Result.Loading(true)
                }
                    .onCompletion {
                        resultUser.value = Result.Loading(false)
                    }.catch {
                        Log.e("Error",it.message.toString())
                        it.printStackTrace()
                        resultUser.value = Result.Error(it)
                    }.collect {
                        resultUser.value = Result.Success(it)
                    }
            }
        }

    fun getUser(username: String) {
        viewModelScope.launch{
            flow {
                val response = ApiConfig
                    .apiService
                    .searchUser(
                        mapOf(
                            "q" to username, "per_page" to 10
                        )
                    )

                emit(response)

            }.onStart {
                resultUser.value = Result.Loading(true)
            }
                .onCompletion {
                    resultUser.value = Result.Loading(false)
                }.catch {
                    Log.e("Error",it.message.toString())
                    it.printStackTrace()
                    resultUser.value = Result.Error(it)
                }.collect {
                    resultUser.value = Result.Success(it.items)
                }
        }
    }

    class Factory(private val preference: SettingPreference): ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(preference) as T
    }
    }
