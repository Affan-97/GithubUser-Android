package com.affan.androidfund_dicoding_fp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.affan.androidfund_dicoding_fp.R
import com.affan.androidfund_dicoding_fp.api.ItemsItem
import com.bumptech.glide.Glide


class UserAdapter(private val listUser: List<ItemsItem>): RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_user)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row,parent,false))
    }

    override fun getItemCount():Int=listUser.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        holder.tvName.text = user.login
        Glide.with(holder.itemView).load(user.avatarUrl).into(holder.imageView)

        holder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(user)

        }

    }
    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }
}
