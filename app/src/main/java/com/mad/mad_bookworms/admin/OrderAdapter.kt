package com.mad.mad_bookworms.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.Order
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class OrderAdapter (
    val fn: (ViewHolder, Order) -> Unit = { _, _ -> }
) : ListAdapter<Order, OrderAdapter.ViewHolder>(DiffCallback) {

    private val formatter = DecimalFormat("0.00")
    private val format = SimpleDateFormat("yyyy.MM.dd")

    companion object DiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(a: Order, b: Order)    = a.id == b.id
        override fun areContentsTheSame(a: Order, b: Order) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val txtId      : TextView = view.findViewById(R.id.txtId)
        val txtType    : TextView = view.findViewById(R.id.txtType)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtAmount   : TextView = view.findViewById(R.id.txtAmount)
        var btnDelete: Button = itemView.findViewById((R.id.btnDelete))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)

        holder.txtId.text    = order.id
        holder.txtType.text  = order.paymentType
        holder.txtDate.text  = format.format(order.dateTime).toString()
        holder.txtAmount.text  = formatter.format(order.amount)

        fn(holder, order)
    }

}