package com.affan.androidfund_dicoding_fp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.affan.androidfund_dicoding_fp.adapter.PagerAdapter
import com.affan.androidfund_dicoding_fp.databinding.ActivityDetailBinding
import com.affan.androidfund_dicoding_fp.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
        const val USERNAME = "username"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = intent.getStringExtra(USERNAME).toString()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_title,name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        mainViewModel.getUser(name)
        mainViewModel.detailUser.observe(this) {
            if (it != null) {
                binding.tvName.text = it.name
                binding.tvUsername.text = it.login
                binding.tvFollower.text = getString(R.string.follower_tag,it.followers.toString())
                binding.tvFollowing.text = getString(R.string.following_tag,it.following.toString())
                Glide.with(this).load(it.avatarUrl).into(binding.profileImg)
            }
        }
        mainViewModel.loading.observe(this) {
            showLoading(it)
        }
        val pagerAdapter = PagerAdapter(this)
        pagerAdapter.username = name
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_main -> {
                finishAffinity()
                val intent = Intent(this@DetailActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
}