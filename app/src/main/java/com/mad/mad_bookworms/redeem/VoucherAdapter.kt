package com.mad.mad_bookworms.redeem

import android.content.ContentValues
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.type.DateTime
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.Voucher
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDate.parse
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Level.parse

class VoucherAdapter(val fn: (ViewHolder, Voucher) -> Unit = { _, _ -> }): ListAdapter<Voucher, VoucherAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Voucher>() {
        override fun areItemsTheSame(a: Voucher, b: Voucher)    = a.id == b.id
        override fun areContentsTheSame(a: Voucher, b: Voucher) = a == b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.browse_voucher_list,parent,false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucher = getItem(position)
        var title = ""

        val level = voucher.level

        if(voucher.type == "1")
        {
            if(level == 1){

                title = "Free Shipping"
                holder.itemImage.setImageResource(R.drawable.ic_baseline_local_shipping_24);
            }
            else if(level == 2){

                title = "[Silver & Platinum Exclusive]" +"\nFree Shipping"
                holder.itemImage.setImageResource(R.drawable.ic_baseline_local_shipping_24);

            }else if(level == 3){

                title = "[Platinum Exclusive]" +"\nFree Shipping"
                holder.itemImage.setImageResource(R.drawable.ic_baseline_local_shipping_24);
            }

        }else if(voucher.type == "2"){

            if(level == 1){

                title = "Discount RM " + voucher.discount.toString()
                holder.itemImage.setImageResource(R.drawable.ic_baseline_receipt_24);
            }
            else if(level == 2){

                title = "[Gold & Platinum Exclusive]" +"\nDiscount RM " + voucher.discount.toString()
                holder.itemImage.setImageResource(R.drawable.ic_baseline_receipt_24);

            }else if(level == 3){

                title = "[Platinum Exclusive]" +"\nDiscount RM " + voucher.discount.toString()
                holder.itemImage.setImageResource(R.drawable.ic_baseline_receipt_24);
            }

        }

        holder.itemTitle.text = title
        holder.itemPoint.text = voucher.requiredPoint.toString() + " points"

        fn(holder, voucher)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var root = itemView
        var itemImage: ImageView = itemView.findViewById(R.id.imgView)
        var itemTitle: TextView = itemView.findViewById(R.id.tvVoucher)
        var itemPoint: TextView = itemView.findViewById((R.id.tvPoint))
        var itemButton: Button = itemView.findViewById((R.id.btnClaim))
    }

}