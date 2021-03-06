package com.mad.mad_bookworms.redeem

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
import com.mad.mad_bookworms.data.MyVoucher
import java.text.SimpleDateFormat
import java.util.*

class UserVoucherAdapter(val fn: (ViewHolder, MyVoucher) -> Unit = { _, _ -> }): ListAdapter<MyVoucher, UserVoucherAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<MyVoucher>() {
        override fun areItemsTheSame(a: MyVoucher, b: MyVoucher)    = a.id == b.id
        override fun areContentsTheSame(a: MyVoucher, b: MyVoucher) = a == b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_active_voucher_list,parent,false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucher = getItem(position)
        var title = ""
        var formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        if(voucher.type == "1")
        {
            title = "Free Shipping"
            holder.itemImage.setImageResource(R.drawable.ic_baseline_local_shipping_24);

        }else if(voucher.type == "2"){

            title = "Discount RM " + voucher.discount.toString()
            holder.itemImage.setImageResource(R.drawable.ic_baseline_receipt_24);
        }

        holder.itemTitle.text = title
        holder.itemExpiry.text = "Expired on " +formatter.format(voucher.expiry_date)

        fn(holder, voucher)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var root = itemView
        var itemImage: ImageView = itemView.findViewById(R.id.imgView)
        var itemTitle: TextView = itemView.findViewById(R.id.tvVoucher)
        var itemExpiry: TextView = itemView.findViewById((R.id.tvExpiry))
        var itemButton: Button = itemView.findViewById((R.id.btnUse))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

//    fun clear() {
//        MyVoucher.clear()
//        notifyDataSetChanged()
//    }
//
//    fun addAll(tweetList: List<Tweet>) {
//        MyVoucher.addAll(tweetList)
//        notifyDataSetChanged()
//    }

}