package com.affan.androidfund_dicoding_fp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.affan.androidfund_dicoding_fp.api.ItemsItem
import com.affan.androidfund_dicoding_fp.databinding.ItemRowBinding
import com.bumptech.glide.Glide


class UserAdapter(
    private val listUser: List<ItemsItem>,
    private val clickListener: (ItemsItem) -> Unit
) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    class ListViewHolder(
        private val binding: ItemRowBinding,
        private val clickListener: (ItemsItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemsItem) {
            binding.tvName.text = item.login
            Glide.with(itemView).load(item.avatarUrl).into(binding.imgUser)
            binding.cardView.setOnClickListener {
                clickListener(item)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)

    }
}
