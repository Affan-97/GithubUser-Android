package com.affan.androidfund_dicoding_fp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.affan.androidfund_dicoding_fp.adapter.UserAdapter
import com.affan.androidfund_dicoding_fp.api.ItemsItem
import com.affan.androidfund_dicoding_fp.databinding.ActivityMainBinding
import com.affan.androidfund_dicoding_fp.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {


    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Github User"
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUser.layoutManager = LinearLayoutManager(this)

        binding.rvUser.setHasFixedSize(true)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )
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
                binding.rvUser.visibility = View.GONE
                binding.dispError.visibility = View.VISIBLE
                mainViewModel.textError.observe(this) {
                    binding.textError.text = it
                }
            } else {
                binding.rvUser.visibility = View.VISIBLE
                binding.dispError.visibility = View.GONE
            }
        }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvUser.alpha = if (isLoading) 0.5f else 1.0f
    }

    private fun showSelectedHero(user: ItemsItem) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra("USERNAME", user.login)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
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

}