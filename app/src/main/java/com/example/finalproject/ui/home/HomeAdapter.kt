package com.example.finalproject.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.data.Item
import com.example.finalproject.data.Place
import com.example.finalproject.databinding.ListItemBinding


class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeHolder>() {
    var places: List<Item>? = null

    override fun getItemCount(): Int {
        return places?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        holder.itemBinding.tvItem.text = places?.get(position).toString()
        holder.itemBinding.clItem.setOnClickListener{
            clickListener?.onItemClick(it, position)
        }
    }

    class HomeHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnItemClickListner {
        fun onItemClick(view: View, position: Int)
    }

    var clickListener: OnItemClickListner? = null

    fun setOnItemClickListener(listener: OnItemClickListner) {
        this.clickListener = listener
    }

}