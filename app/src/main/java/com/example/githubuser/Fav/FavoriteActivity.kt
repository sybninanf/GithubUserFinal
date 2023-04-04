package com.example.githubuser.Fav

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.local.DbModul
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.detail.DetailActivity
import com.example.githubuser.ui.UserAdapter


class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val adapter by lazy{
        UserAdapter {user ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", user)
                startActivity(this)
            }
        }
    }

    private val viewModel by viewModels<FavoritrViewModel>{
        FavoritrViewModel.Factory(DbModul((this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvfav.layoutManager = LinearLayoutManager(this)
        binding.rvfav.adapter = adapter

        viewModel.getUserFavorite().observe(this){
            adapter.setData(it)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}