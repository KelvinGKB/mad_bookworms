package com.mad.mad_bookworms.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.Feedback

class FeedbackAdapter (
    val fn: (ViewHolder, Feedback) -> Unit = { _, _ -> }
) : ListAdapter<Feedback, FeedbackAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Feedback>() {
        override fun areItemsTheSame(a: Feedback, b: Feedback)    = a.id == b.id
        override fun areContentsTheSame(a: Feedback, b: Feedback) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val tvDesc    : TextView = view.findViewById(R.id.tvDesc)
        val tvType  : TextView = view.findViewById(R.id.tvType)
        val rating_bar   : RatingBar = view.findViewById(R.id.rating_bar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_feedback, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedback = getItem(position)

        holder.rating_bar.setRating(feedback.rating.toFloat())
        holder.tvDesc.text = feedback.desc
        holder.tvType.text  = feedback.type

        fn(holder, feedback)
    }

}