package com.affan.androidfund_dicoding_fp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.affan.androidfund_dicoding_fp.R
import com.affan.androidfund_dicoding_fp.adapter.PagerAdapter
import com.affan.androidfund_dicoding_fp.database.Favorite
import com.affan.androidfund_dicoding_fp.databinding.ActivityDetailBinding
import com.affan.androidfund_dicoding_fp.factory.PrefViewModelFactory
import com.affan.androidfund_dicoding_fp.helper.ThemeHelper
import com.affan.androidfund_dicoding_fp.viewmodel.FavAddViewModel
import com.affan.androidfund_dicoding_fp.viewmodel.MainViewModel
import com.affan.androidfund_dicoding_fp.factory.ViewModelFactory
import com.affan.androidfund_dicoding_fp.preferences.SettingPreferences
import com.affan.androidfund_dicoding_fp.viewmodel.PrefViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityDetailBinding
    private lateinit var favAddViewModel: FavAddViewModel
    private lateinit var prefViewModel: PrefViewModel

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
        const val USERNAME = "username"
    }

    private var favorite: Favorite? = null
    private var userImge: String = ""
    private var userName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = intent.getStringExtra(USERNAME).toString()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_title, name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]
        mainViewModel.getUser(name)
        mainViewModel.detailUser.observe(this) {
            if (it != null) {
                userImge = it.avatarUrl
                userName = it.login
                binding.apply {

                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollower.text = getString(R.string.follower_tag, it.followers.toString())
                    tvFollowing.text = getString(R.string.following_tag, it.following.toString())
                }
                Glide.with(this).load(it.avatarUrl).into(binding.profileImg)

                favAddViewModel.isSaved(it.login, it.avatarUrl) { save ->
                    if (save) {

                        binding.fabAdd.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.fabAdd.context,
                                R.drawable.ic_favorite
                            )
                        )

                    } else {
                        binding.fabAdd.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.fabAdd.context,
                                R.drawable.ic_favorite_border
                            )
                        )


                    }

                }


            }
        }
        mainViewModel.loading.observe(this) {
            showLoading(it)
        }


        binding.fabAdd.setOnClickListener(this)
        val pagerAdapter = PagerAdapter(this)
        pagerAdapter.username = name
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        favorite = Favorite()
        supportActionBar?.elevation = 0f
        favAddViewModel = obtainViewModel(this@DetailActivity)

        val pref = SettingPreferences.getInstance(dataStore)
        prefViewModel =
            ViewModelProvider(this, PrefViewModelFactory(pref))[PrefViewModel::class.java]
        val theme = ThemeHelper()

        prefViewModel.getTheme().observe(this) {
            theme.switchTheme(it)

        }
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
            R.id.dark_btn -> {
                val theme = ThemeHelper()
                val nightMode = ThemeHelper.isNight

                prefViewModel.saveTheme(nightMode)
                theme.switchTheme(nightMode)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add -> {
                favorite.let {

                    it?.username = userName
                    it?.avatarUrl = userImge

                }
                favAddViewModel.isSaved(userName, userImge) { save ->
                    if (!save) {
                        favAddViewModel.insert(favorite as Favorite)
                        binding.fabAdd.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.fabAdd.context,
                                R.drawable.ic_favorite
                            )
                        )

                    } else {
                        binding.fabAdd.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.fabAdd.context,
                                R.drawable.ic_favorite_border
                            )
                        )
                        favAddViewModel.delete(favorite as Favorite)

                    }

                }

            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavAddViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavAddViewModel::class.java]
    }
}