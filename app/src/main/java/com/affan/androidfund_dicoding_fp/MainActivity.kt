package com.affan.androidfund_dicoding_fp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle



import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.affan.androidfund_dicoding_fp.adapter.UserAdapter
import com.affan.androidfund_dicoding_fp.api.ItemsItem
import com.affan.androidfund_dicoding_fp.databinding.ActivityMainBinding
import com.affan.androidfund_dicoding_fp.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {


    private lateinit var mainViewModel: MainViewModel
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUser.layoutManager = LinearLayoutManager(this)

        binding.rvUser.setHasFixedSize(true)
        mainViewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        mainViewModel.listUser.observe(this) {
            if (it != null) {
                setData(it)
            }
        }
        mainViewModel.loading.observe(this){
            showLoading(it)
        }

    }


    private fun setData(userList: List<ItemsItem>){
        val adapter = UserAdapter(userList)
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback{
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
/*        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        startActivity(intent)*/
        Toast.makeText(this, "Kamu memilih " + user.login, Toast.LENGTH_SHORT).show()
    }

}