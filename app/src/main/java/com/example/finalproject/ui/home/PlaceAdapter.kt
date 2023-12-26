package com.example.finalproject.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.data.Item
import com.example.finalproject.databinding.ListItemBinding

class PlaceAdapter : RecyclerView.Adapter<PlaceAdapter.HomeHolder>() {
    var places: List<Item>? = null
    fun setData(newData: List<Item>?) {
        places = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return places?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        holder.itemBinding.tvPlaceTitle.text = places?.get(position)?.title
        holder.itemBinding.tvPlaceInfo.text = places?.get(position)?.information

        holder.itemBinding.clItem.setOnClickListener {
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