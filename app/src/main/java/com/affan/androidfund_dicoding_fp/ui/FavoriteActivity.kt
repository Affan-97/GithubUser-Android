package com.affan.androidfund_dicoding_fp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import android.view.View

import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.affan.androidfund_dicoding_fp.R


import com.affan.androidfund_dicoding_fp.adapter.UserAdapter
import com.affan.androidfund_dicoding_fp.api.ItemsItem

import com.affan.androidfund_dicoding_fp.databinding.ActivityFavoriteBinding
import com.affan.androidfund_dicoding_fp.factory.PrefViewModelFactory
import com.affan.androidfund_dicoding_fp.helper.ThemeHelper
import com.affan.androidfund_dicoding_fp.viewmodel.FavViewModel
import com.affan.androidfund_dicoding_fp.factory.ViewModelFactory
import com.affan.androidfund_dicoding_fp.preferences.SettingPreferences
import com.affan.androidfund_dicoding_fp.viewmodel.PrefViewModel
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var prefViewModel: PrefViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUser.layoutManager = LinearLayoutManager(this)

        binding.rvUser.setHasFixedSize(true)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(application)
        val viewModel: FavViewModel by viewModels {
            factory
        }
        viewModel.getAll().observe(this) {  it->
            val items = arrayListOf<ItemsItem>()

            it.map { fav ->
                val item =
                    fav.avatarUrl?.let { it1 -> ItemsItem(login = fav.username, avatarUrl = it1) }
                if (item != null) {
                    items.add(item)
                }
            }
            if (items.size > 0) {

                setData(items)
                binding.dispError.visibility = View.GONE
                binding.rvUser.visibility = View.VISIBLE
            } else {
                binding.dispError.visibility = View.VISIBLE
                binding.rvUser.visibility = View.GONE
            }

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val pref = SettingPreferences.getInstance(dataStore)
        prefViewModel = ViewModelProvider(this, PrefViewModelFactory(pref))[PrefViewModel::class.java]
        val theme = ThemeHelper()

        prefViewModel.getTheme().observe(this){
            theme.switchTheme(it)

        }
    }

    private fun setData(userList: List<ItemsItem>) {
        val adapter = UserAdapter(userList) {
            showSelectedHero(it)
        }
        binding.rvUser.adapter = adapter


    }

    private fun showSelectedHero(user: ItemsItem) {
        val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.USERNAME, user.login)
        startActivity(intent)
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
                val intent = Intent(this@FavoriteActivity, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.dark_btn->{
                val theme = ThemeHelper()
                val nightMode = ThemeHelper.isNight

                prefViewModel.saveTheme(nightMode)
                theme.switchTheme(nightMode)
            }
        }
        return super.onOptionsItemSelected(item)

    }
}