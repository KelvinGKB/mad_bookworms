package com.mad.mad_bookworms.profile

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
import com.mad.mad_bookworms.data.User
import java.util.*

class ReferralHistoryAdapter(val fn: (ViewHolder, User) -> Unit = { _, _ -> }) : ListAdapter<User, ReferralHistoryAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(a: User, b: User)    = a.id == b.id
        override fun areContentsTheSame(a: User, b: User) = a == b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_referral_history,parent,false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)

        holder.itemName.text = user.username
        holder.itemEmail.text = user.email
        fn(holder, user)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var root = itemView
        var itemName: TextView = itemView.findViewById(R.id.tvUsername)
        var itemEmail: TextView = itemView.findViewById((R.id.tvEmail))
    }

}