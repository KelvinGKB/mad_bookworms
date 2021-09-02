package com.mad.mad_bookworms.admin

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

class BookAdapter (
    val fn: (ViewHolder, Book) -> Unit = { _, _ -> }
) : ListAdapter<Book, BookAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(a: Book, b: Book)    = a.id == b.id
        override fun areContentsTheSame(a: Book, b: Book) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto : ImageView = view.findViewById(R.id.imgPhoto)
        val txtId    : TextView = view.findViewById(R.id.txtId)
        val txtName  : TextView = view.findViewById(R.id.txtName)
        val txtAge   : TextView = view.findViewById(R.id.txtAge)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = getItem(position)

        holder.txtId.text   = book.id
        holder.txtName.text = book.title
        holder.txtAge.text  = book.price.toString()

        // TODO: Photo (blob to bitmap)
        holder.imgPhoto.setImageBitmap(book.image.toBitmap())

        fn(holder, book)
    }

}