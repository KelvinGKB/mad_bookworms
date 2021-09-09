package com.mad.mad_bookworms.customer.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.Category

class CategoryAdapter(val fn: (ViewHolder, Category) -> Unit = { _, _ -> }):
    ListAdapter<Category, CategoryAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(a: Category, b: Category)    = a.id == b.id
        override fun areContentsTheSame(a: Category, b: Category) = a == b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_book_category,parent,false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        holder.tvCategory.text = category.name

        fn(holder, category)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var root = itemView
        var tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
    }
}