package com.mad.mad_bookworms.customer.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.customer.explore.RecyclerAdapter
import com.mad.mad_bookworms.customer.explore.TrendingAdapter
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.data.MyCartTable
import com.mad.mad_bookworms.viewModels.BookViewModel
import com.sun.mail.imap.protocol.FetchResponse.getItem
import androidx.fragment.app.FragmentActivity




class CartOrderAdapter(
    val fn: (ViewHolder, MyCartTable) -> Unit = { _, _ -> })
    : ListAdapter<MyCartTable, CartOrderAdapter.ViewHolder>(DiffCallback) {

    private lateinit var vm : BookViewModel

    companion object DiffCallback : DiffUtil.ItemCallback<MyCartTable>() {
        override fun areItemsTheSame(a: MyCartTable, b: MyCartTable)    = a.bookId == b.bookId
        override fun areContentsTheSame(a: MyCartTable, b: MyCartTable) = a == b

    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val root = itemview
        val chkCartOrder : CheckBox = itemview.findViewById(R.id.chkCartOrder)
        val tvBookTitle : TextView = itemview.findViewById(R.id.tvBookTitle)
        val tvBookAuthor : TextView = itemview.findViewById(R.id.tvBookAuthor)
        val tvBookPrice : TextView = itemview.findViewById(R.id.tvBookPrice)
        var itemImage: ImageView = itemView.findViewById(R.id.image_book)
        val btnMinus : ImageButton = itemview.findViewById(R.id.btnMinus)
        val btnIncrease: ImageButton = itemview.findViewById(R.id.btnIncrease)
        val edtQty: EditText = itemview.findViewById(R.id.edtQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.cardview_cartorder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        var qty = holder.edtQty.text.toString().toInt()
//        holder.tvBookTitle.text = item.book.title
//        holder.tvBookAuthor.text = item.book.author
//        holder.tvBookPrice.text = "RM" + "%.2f".format(item.book.price)
        holder.edtQty.setText("${item.quantity}")
        //holder.itemImage.setImageBitmap(data[position].image.toBitmap())

        fn(holder, item)
    }
}