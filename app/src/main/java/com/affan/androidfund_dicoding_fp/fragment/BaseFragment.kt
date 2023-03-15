package com.affan.androidfund_dicoding_fp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.affan.androidfund_dicoding_fp.R
import com.affan.androidfund_dicoding_fp.adapter.UserAdapter
import com.affan.androidfund_dicoding_fp.api.ItemsItem
import com.affan.androidfund_dicoding_fp.databinding.ActivityMainBinding
import com.affan.androidfund_dicoding_fp.databinding.FragmentBaseBinding
import com.affan.androidfund_dicoding_fp.viewmodel.MainViewModel


class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentBaseBinding.inflate(inflater, container, false)

        binding.rvUser.layoutManager = LinearLayoutManager(context)

        binding.rvUser.setHasFixedSize(true)
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        viewModel.listUser.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                binding.rvUser.adapter = UserAdapter(list)
                Log.d("Value", "onViewCreated: $list")
            }
        }

        return binding.root
    }
    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvLabel: TextView = view.findViewById(R.id.section_label)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        tvLabel.text = getString(R.string.content_tab_text, index)
    }

}