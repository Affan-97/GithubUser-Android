package com.affan.androidfund_dicoding_fp.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.affan.androidfund_dicoding_fp.fragment.BaseFragment

class PagerAdapter(activity: AppCompatActivity):FragmentStateAdapter(activity) {
    var username: String = ""
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
      val fragment:Fragment = BaseFragment()
        fragment.arguments = Bundle().apply {
            putInt(BaseFragment.ARG_SECTION_NUMBER, position)
            putString(BaseFragment.NAME, username)
        }
        return fragment
    }
}