package com.mad.mad_bookworms.customer.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var title = arrayOf("Book 1", "Book 2", "Book 3", "Book 4", "Book 5", "Book 6")
    private  var author = arrayOf("Author 1","Author 2","Author 3","Author 4","Author 5","Author 6")
    private val images = intArrayOf(R.drawable.book, R.drawable.book, R.drawable.book, R.drawable.book, R.drawable.book, R.drawable.book)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_booklist,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return title.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = title[position]
        holder.itemAuthor.text = author[position]
        holder.itemImage.setImageResource(images[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView = itemView.findViewById(R.id.image_book)
        var itemTitle: TextView = itemView.findViewById(R.id.tvBookTitle)
        var itemAuthor: TextView = itemView.findViewById((R.id.tvBookAuthor))

    }

}