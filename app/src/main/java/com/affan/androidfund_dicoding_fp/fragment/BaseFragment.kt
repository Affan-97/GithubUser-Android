package com.affan.androidfund_dicoding_fp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.affan.androidfund_dicoding_fp.ui.DetailActivity
import com.affan.androidfund_dicoding_fp.adapter.UserAdapter
import com.affan.androidfund_dicoding_fp.api.ItemsItem
import com.affan.androidfund_dicoding_fp.databinding.FragmentBaseBinding
import com.affan.androidfund_dicoding_fp.viewmodel.MainViewModel


class BaseFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentBaseBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseBinding.inflate(inflater, container, false)

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(requireActivity())
            rvUser.setHasFixedSize(true)
        }
        mainViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]
        return binding.root
    }


    private fun setData(userList: List<ItemsItem>) {
        val adapter = UserAdapter(userList) {
            showSelectedHero(it)
        }
        binding.rvUser.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val name = arguments?.getString(NAME)
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
        mainViewModel.loading.observe(viewLifecycleOwner) {
            if (it != null) {
                showLoading(it)
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvUser.alpha = if (isLoading) 0.5f else 1.0f
    }
    private fun showSelectedHero(user: ItemsItem) {

        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.USERNAME, user.login)
        startActivity(intent)

    }
    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val NAME = "app_name"
    }

}