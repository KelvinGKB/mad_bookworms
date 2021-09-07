package com.mad.mad_bookworms.customer.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.toBitmap

class TrendingAdapter(
    val fn: (ViewHolder, Book) -> Unit = { _, _ -> }):
    ListAdapter<Book, TrendingAdapter.ViewHolder>(TrendingAdapter)
    {

        companion object DiffCallback : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(a: Book, b: Book)    = a.id == b.id
            override fun areContentsTheSame(a: Book, b: Book) = a == b
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_trending,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = getItem(position)
        holder.itemTitle.text = book.title
        holder.itemAuthor.text = book.author
        holder.itemImage.setImageBitmap(book.image.toBitmap())
        holder.itemPrice.text = "RM" + "%.2f".format(book.price)

        fn(holder, book)
    }

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var root = itemView
        var itemImage: ImageView = itemView.findViewById(R.id.image_book)
        var itemTitle: TextView = itemView.findViewById(R.id.tvBookTitle)
        var itemAuthor: TextView = itemView.findViewById((R.id.tvBookAuthor))
        var itemPrice: TextView = itemView.findViewById((R.id.tvBookPrice))
        val btnAddToCart: Button = itemView.findViewById(R.id.btnShare)
    }
}