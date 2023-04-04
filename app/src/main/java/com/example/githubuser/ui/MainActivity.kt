package com.example.githubuser.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.model.ResponseUser
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.detail.DetailActivity
import com.example.githubuser.util.Result


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy{
        UserAdapter {
            Intent(this, DetailActivity::class.java).apply {
                putExtra("username", it.login)
                startActivity(this)
            }
        }
    }

    private val viewModel by viewModels<MainViewModel> ()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvHome.layoutManager = LinearLayoutManager(this)
        binding.rvHome.setHasFixedSize(true)
        binding.rvHome.adapter = adapter

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                viewModel.getUser(p0.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
        viewModel.resultUser.observe(this){
            when (it) {
                is Result.Success<*> -> {
                    adapter.setData(it.data as MutableList<ResponseUser.Item>)
                }
                is Result.Error -> {
                    Toast.makeText( this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }


        viewModel.getUser()


            }
        }

