package com.mad.mad_bookworms.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.BookOrder

class BookOrderAdapter(
    val fn: (ViewHolder, BookOrder) -> Unit = { _, _ -> })
    : ListAdapter<BookOrder, BookOrderAdapter.ViewHolder>(DiffCallback) {


    companion object DiffCallback : DiffUtil.ItemCallback<BookOrder>() {
        override fun areItemsTheSame(a: BookOrder, b: BookOrder)    = a.book_id == b.book_id
        override fun areContentsTheSame(a: BookOrder, b: BookOrder) = a == b

    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val root = itemview
        val tvBookTitle : TextView = itemview.findViewById(R.id.tvBookTitle)
        val tvBookAuthor : TextView = itemview.findViewById(R.id.tvBookAuthor)
        val tvBookPrice : TextView = itemview.findViewById(R.id.tvBookPrice)
        var itemImage: ImageView = itemview.findViewById(R.id.image_book)
        val tvQty: TextView = itemview.findViewById(R.id.tvQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_book_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
//        holder.tvBookTitle.text = item.book_id
//        holder.tvBookAuthor.text = item.book.author
//        holder.tvBookPrice.text = "RM" + "%.2f".format(item.book.price)
        holder.tvQty.setText("${item.qty}")
        //holder.itemImage.setImageBitmap(data[position].image.toBitmap())

        fn(holder, item)
    }

}