package com.mad.mad_bookworms.customer.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.PendingOrder


class PlaceOrderAdapter(
    val fn: (ViewHolder, PendingOrder) -> Unit = { _, _ -> })
    : ListAdapter<PendingOrder, PlaceOrderAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<PendingOrder>() {
        override fun areItemsTheSame(a: PendingOrder, b: PendingOrder)    = a.bookId == b.bookId
        override fun areContentsTheSame(a: PendingOrder, b: PendingOrder) = a == b

    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val root = itemview
        val tvBookTitle : TextView = itemview.findViewById(R.id.tvBookTitle)
        val tvBookAuthor : TextView = itemview.findViewById(R.id.tvBookAuthor)
        val tvBookPrice : TextView = itemview.findViewById(R.id.tvBookPrice)
        var itemImage: ImageView = itemview.findViewById(R.id.image_book)
        val tvOrderQty: TextView = itemview.findViewById(R.id.tvOrderQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.cardview_placeorder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.tvOrderQty.text = "QTY: ${item.quantity}"

        fn(holder, item)
    }

}