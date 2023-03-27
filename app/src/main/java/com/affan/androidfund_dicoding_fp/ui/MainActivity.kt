package com.affan.androidfund_dicoding_fp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.affan.androidfund_dicoding_fp.R
import com.affan.androidfund_dicoding_fp.adapter.UserAdapter
import com.affan.androidfund_dicoding_fp.api.ItemsItem
import com.affan.androidfund_dicoding_fp.databinding.ActivityMainBinding
import com.affan.androidfund_dicoding_fp.factory.PrefViewModelFactory

import com.affan.androidfund_dicoding_fp.helper.ThemeHelper
import com.affan.androidfund_dicoding_fp.preferences.SettingPreferences
import com.affan.androidfund_dicoding_fp.viewmodel.MainViewModel
import com.affan.androidfund_dicoding_fp.viewmodel.PrefViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {


    private lateinit var mainViewModel: MainViewModel
    private lateinit var prefViewModel: PrefViewModel

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0f
        supportActionBar?.title =getString(R.string.main_title)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUser.layoutManager = LinearLayoutManager(this)

        binding.rvUser.setHasFixedSize(true)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        mainViewModel.listUser.observe(this) {
            if (it != null) {
                setData(it)
            }
        }
        mainViewModel.loading.observe(this) {
            showLoading(it)
        }
        mainViewModel.error.observe(this) {
            if (it) {
               showUI(it)
                mainViewModel.textError.observe(this) {
                    binding.textError.text = it
                }
            } else {
                showUI(it)
            }
        }
        val pref = SettingPreferences.getInstance(dataStore)
        prefViewModel = ViewModelProvider(this,PrefViewModelFactory(pref))[PrefViewModel::class.java]
        val theme = ThemeHelper()

        prefViewModel.getTheme().observe(this){
        theme.switchTheme(it)

        }
    }
private fun showUI(isShow:Boolean){
    binding.apply {
        if (isShow){
            rvUser.visibility = View.GONE
            dispError.visibility = View.VISIBLE
        }else{
            rvUser.visibility = View.VISIBLE
            dispError.visibility = View.GONE
        }
    }
}

    private fun setData(userList: List<ItemsItem>) {
        val adapter = UserAdapter(userList) {
            showSelectedHero(it)
        }
        binding.rvUser.adapter = adapter
      /*  adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedHero(data)
            }
        })*/

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvUser.alpha = if (isLoading) 0.5f else 1.0f
    }

    private fun showSelectedHero(user: ItemsItem) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.USERNAME, user.login)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(true)
        searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn).setColorFilter(Color.RED)
        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        searchIcon.setImageResource(R.drawable.ic_arrow)
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            run {
                if (hasFocus) {
                    if (searchView.query.isEmpty()) {
                        mainViewModel.getData()
                    }
                }
            }

        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank()) {

                    mainViewModel.getData()
                    searchView.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(searchView.windowToken, 0)
                } else {

                    mainViewModel.findUser(query)
                    searchView.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(searchView.windowToken, 0)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {


                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.fav -> {

                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
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