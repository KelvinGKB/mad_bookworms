package com.mad.mad_bookworms.customer.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.customer.explore.classes.Book

class RecyclerAdapter(private val data: List<Book>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener ){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_booklist,parent,false)
        return ViewHolder(v,mListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = data[position].bookTitle
        holder.itemAuthor.text = data[position].bookAuthor
        holder.itemImage.setImageResource(data[position].bookImage)
    }

    inner class ViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView = itemView.findViewById(R.id.image_book)
        var itemTitle: TextView = itemView.findViewById(R.id.tvBookTitle)
        var itemAuthor: TextView = itemView.findViewById((R.id.tvBookAuthor))

        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }
        }

    }

}