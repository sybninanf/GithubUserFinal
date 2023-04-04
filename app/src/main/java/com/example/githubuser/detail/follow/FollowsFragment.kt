package com.example.githubuser.detail.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.githubuser.util.Result
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.model.ResponseUser
import com.example.githubuser.databinding.FragmentFollowsBinding
import com.example.githubuser.detail.DetailviewModel
import com.example.githubuser.ui.UserAdapter

class FollowsFragment : Fragment() {

    private  var binding: FragmentFollowsBinding? = null
    private val adapter by lazy {
        UserAdapter{

        }
    }

    private  val viewModel by activityViewModels<DetailviewModel>()
    var type = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowsBinding.inflate((layoutInflater))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.follow?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowsFragment.adapter
        }

        when (type){
            FOLLOWERS -> {
                viewModel.resulFollowersUser.observe(viewLifecycleOwner, this::manageResultFollows)
            }
            FOLLOWING -> {
                viewModel.resulFollowingUser.observe(viewLifecycleOwner, this::manageResultFollows)
            }
        }

    }
private fun manageResultFollows(state: Result) {
    when (state) {
        is Result.Success<*> -> {
            adapter.setData(state.data as MutableList<ResponseUser.Item>)
        }
        is Result.Error -> {
            Toast.makeText( requireActivity(), state.exception.message.toString(), Toast.LENGTH_SHORT).show()
        }
        is Result.Loading -> {
            binding?.progressBar?.isVisible = state.isLoading
        }
    }
}

    companion object {
        const val FOLLOWERS = 101
        const val FOLLOWING = 100


        fun newInstance(type: Int) = FollowsFragment()
            .apply {
                this.type = type
            }
    }
}