package com.mad.mad_bookworms.customer.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.customer.explore.classes.Book

class TrendingAdapter(private val data: List<Book>): RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_trending,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: TrendingAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = data[position].bookTitle
        holder.itemAuthor.text = data[position].bookAuthor
        holder.itemImage.setImageResource(data[position].bookImage)
        holder.itemPrice.text = "RM" + "%.2f".format(data[position].bookPrice)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView = itemView.findViewById(R.id.image_book)
        var itemTitle: TextView = itemView.findViewById(R.id.tvBookTitle)
        var itemAuthor: TextView = itemView.findViewById((R.id.tvBookAuthor))
        var itemPrice: TextView = itemView.findViewById((R.id.tvBookPrice))
    }
}