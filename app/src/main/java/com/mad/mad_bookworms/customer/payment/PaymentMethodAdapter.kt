package com.mad.mad_bookworms.customer.payment

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.PaymentMethod
import com.mad.mad_bookworms.data.SelectedPayMethod

class PaymentMethodAdapter (
    val fn: (ViewHolder, PaymentMethod) -> Unit = { _, _ ->})
    : ListAdapter<PaymentMethod, PaymentMethodAdapter.ViewHolder>(DiffCallback){

    private var selected_position = -1
    private val paymentMethod: MutableList<SelectedPayMethod> = ArrayList()

    companion object DiffCallback : DiffUtil.ItemCallback<PaymentMethod>() {
        override fun areItemsTheSame(a: PaymentMethod, b: PaymentMethod)    = a.PaymentName == b.PaymentName
        override fun areContentsTheSame(a: PaymentMethod, b: PaymentMethod) = a == b
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val root = itemview
        val cvPayMethod : CardView = itemview.findViewById(R.id.cvPayMethod)
        val imgPayIcon : ImageView = itemview.findViewById(R.id.paymentMethodIcon)
        val tvPaymentName : TextView = itemview.findViewById(R.id.tvPaymentName)
        val tvWalletBalance: TextView = itemview.findViewById(R.id.tvWalletBalance)
        val tvCashbackMessage: TextView = itemview.findViewById(R.id.tvCashbackMessage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.cardview_payment_method, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder,  position: Int) {
        val item = getItem(position)

        holder.tvPaymentName.text = item.PaymentName
        holder.imgPayIcon.setImageResource(item.PaymentIcon)
        if (item.PaymentName == "Paypal"){
            holder.tvCashbackMessage.text = "Enjoy 10% points cashback"
        }



        fn(holder, item)
    }

    fun changeCvBackground (payMethod: String) {
        Log.d("TAG","huwehfuehfuher $payMethod")


    }

}