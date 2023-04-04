package com.example.githubuser.Fav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.local.DbModul


class FavoritrViewModel(private val dbModul: DbModul) :ViewModel() {

    fun getUserFavorite() = dbModul.userDao.loadAll()

    class Factory(private val db: DbModul): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T = FavoritrViewModel(db) as T
}
}