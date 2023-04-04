package com.example.githubuser.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.example.githubuser.R
import com.example.githubuser.data.model.ResponseDetailUser
import com.example.githubuser.databinding.ActivityDetail2Binding
import com.example.githubuser.detail.follow.FollowsFragment
import com.example.githubuser.util.Result
import com.google.android.material.tabs.TabLayout

class DetailActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityDetail2Binding
    private  val viewModel by viewModels<DetailviewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetail2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra("username") ?:""

        viewModel.resulDetailUser.observe(this){
            when (it) {
                is Result.Success<*> -> {
                    val user = it.data as ResponseDetailUser
                    binding.ivAvatar.load(user.avatar_url){
                        transformations(CircleCropTransformation())
                    }
                    binding.nama.text = user.login
                    binding.username.text = user.name
                    binding.tvFollowers.text = user.followers.toString()
                    binding.tvFollowing.text = user.following.toString()
                }
                is Result.Error -> {
                    Toast.makeText( this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }

        viewModel.getDetailUser(username)

        val fragments = mutableListOf<Fragment>(
            FollowsFragment.newInstance(FollowsFragment.FOLLOWERS),
            FollowsFragment.newInstance(FollowsFragment.FOLLOWING)
        )
        val titleFragment = mutableListOf(
            getString(R.string.followers), getString(R.string.following)
        )
        val adapter = DetailAdapter(this, fragments)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tab, binding.viewpager) { tab, posisi ->
            tab.text = titleFragment[posisi]
        }.attach()

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getFollowers(username)
                } else {
                    viewModel.getFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        viewModel.getFollowers(username)
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