package com.affan.androidfund_dicoding_fp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.affan.androidfund_dicoding_fp.DetailActivity
import com.affan.androidfund_dicoding_fp.adapter.UserAdapter
import com.affan.androidfund_dicoding_fp.api.ItemsItem
import com.affan.androidfund_dicoding_fp.databinding.FragmentBaseBinding
import com.affan.androidfund_dicoding_fp.viewmodel.MainViewModel


class BaseFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    lateinit var binding: FragmentBaseBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseBinding.inflate(inflater, container, false)
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.setHasFixedSize(true)
        mainViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        return binding.root
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val USERNAME = "app_name"
    }
    private fun setData(userList: List<ItemsItem>) {
        val adapter = UserAdapter(userList)
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedHero(data)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val name = arguments?.getString(USERNAME)
        if (index == 0) {
            if (name != null) {
                mainViewModel.getFollowers(name)
            }
            mainViewModel.listFollowers.observe(viewLifecycleOwner) {
                if (it != null) {
                    setData(it)
                }
            }
        } else {
            if (name != null) {
                mainViewModel.getFollowing(name)
            }
            mainViewModel.listFollowing.observe(viewLifecycleOwner) {
                if (it != null) {
                    setData(it)
                }
            }
        }
    }

    private fun showSelectedHero(user: ItemsItem) {

        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra("USERNAME", user.login)
        startActivity(intent)

    }


}