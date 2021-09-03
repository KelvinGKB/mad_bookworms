package com.mad.mad_bookworms.customer.cart

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mad.mad_bookworms.BadgeDrawable
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.customer.bookDetail.BookDetailActivity
import com.mad.mad_bookworms.customer.explore.RecyclerAdapter
import com.mad.mad_bookworms.customer.payment.PaymentActivity
import com.mad.mad_bookworms.data.*
import com.mad.mad_bookworms.databinding.FragmentCartBinding
import com.mad.mad_bookworms.viewModels.BookViewModel
import com.mad.mad_bookworms.viewModels.CartOrderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: CartOrderAdapter
    private val pendingOrder: MutableList<MyCartTable> = ArrayList()
    private var prepareOrder: MutableList<PendingOrder> = arrayListOf()
    private val vm: BookViewModel by activityViewModels()
    private val cartVm: CartOrderViewModel by activityViewModels()
    private var totalPrice : Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)


        val data: MutableList<MyCartTable> = ArrayList()
        //myCartDao = LocalDB.getInstance(requireContext()).MyCartDao

        //Recycler View for cart order
        adapter = CartOrderAdapter() { holder, MyCartTable ->
            holder.adapterPosition
            var qty = MyCartTable.quantity

            holder.root.setOnClickListener {

                val intent = Intent(requireContext(), BookDetailActivity::class.java)
                intent.putExtra("bookID",MyCartTable.bookId)

                startActivity(intent)
            }

            holder.btnIncrease.setOnClickListener {
                qty += 1
                cartVm.updateQty(MyCartTable.bookId,qty)

            }

            holder.btnMinus.setOnClickListener {
                qty -= 1
                if (qty < 1) {
                    qty = 1
                }
                cartVm.updateQty(MyCartTable.bookId,qty)

            }
            holder.edtQty.setText("${MyCartTable.quantity}")


            MyCartTable.bookId
            lifecycleScope.launch {
                val f = vm.get(MyCartTable.bookId)
                var price: Double
                if (f!=null) {
                    price = f.price * holder.edtQty.text.toString().toInt()
                    holder.tvBookTitle.text = f.title
                    holder.tvBookAuthor.text = f.author
                    holder.tvBookPrice.text = "RM" + "%.2f".format(price)

                    holder.chkCartOrder.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            pendingOrder.add(MyCartTable)
                            totalPrice = binding.tvTotalPrice.text.toString().toDouble() + price
                            binding.tvTotalPrice.text = "%.2f".format(totalPrice)


                        }
                        if (!isChecked) {
                            if (pendingOrder.contains(MyCartTable)){
                                pendingOrder.remove(MyCartTable)
                                totalPrice = binding.tvTotalPrice.text.toString().toDouble() - price
                                binding.tvTotalPrice.text = "%.2f".format(totalPrice)

                            }
                        }
                    }

                }

            }
        }

        binding.rvCartOrder.adapter = adapter
        binding.rvCartOrder.setHasFixedSize(true)

        binding.btnDelete.setOnClickListener {
            if (pendingOrder.isNotEmpty()){
                var builder = AlertDialog.Builder(activity)
                builder.setTitle(getString(R.string.confirm_delete))
                builder.setMessage(getString(R.string.delete_confirmation_message))
                builder.setPositiveButton("Yes",DialogInterface.OnClickListener{ dialog, id ->
                    for (b in pendingOrder) {
                        cartVm.delete(b)
                    }
                    pendingOrder.clear()
                    dialog.cancel()
                })
                builder.setNegativeButton("No", DialogInterface.OnClickListener{ dialog, id ->
                    dialog.cancel()
                })
                var alert = builder.create()
                alert.show()
            }
            else {
                Toast.makeText(requireContext(), getString(R.string.no_selected_any_item_message), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCheckOut.setOnClickListener {
            if (pendingOrder.isNotEmpty()){
                prepareOrder.clear()
                for(o in pendingOrder) {
                    prepareOrder.add(PendingOrder(o.bookId, o.quantity))
                }

                val intent = Intent(requireContext(), PaymentActivity::class.java)
                intent.putExtra("totalAmount", totalPrice)
                intent.putExtra("pendingOrder",ArrayList(prepareOrder))

                startActivity(intent)
            }
            else{
                Toast.makeText(requireContext(), getString(R.string.no_selected_any_item_message), Toast.LENGTH_SHORT).show()
            }
        }



        val swipeGesture = object :  SwipeGesture(requireContext()) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when (direction) {

                    ItemTouchHelper.LEFT -> {
                        cartVm.delete(data[viewHolder.adapterPosition])
                        data.removeAt(viewHolder.adapterPosition)
                        adapter.submitList(data)
                    }

                    ItemTouchHelper.RIGHT -> {
                        val achieveItem = data[viewHolder.adapterPosition]
                        cartVm.delete(data[viewHolder.adapterPosition])
                        data.removeAt(viewHolder.adapterPosition)
                        cartVm.insert(achieveItem)
                        data.add(achieveItem)
                        adapter.submitList(data)
//                        val achieveItemPos = viewHolder.adapterPosition
//                        cartVm.delete(adapter.getOrderAt(viewHolder.adapterPosition))
//                        cartVm.insert(achieveItem)
//                        Toast.makeText(requireContext(), "swipe right", Toast.LENGTH_SHORT).show()
                    }


                }


            }

        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        binding.rvCartOrder.apply {
            touchHelper.attachToRecyclerView(this)
        }

        cartVm.getAll().observe(viewLifecycleOwner) { myCart ->
            var totalQty = 0
            adapter.submitList(myCart)
            data.addAll(myCart)
            for (c in myCart) {
                if (pendingOrder.contains(c)){
                    pendingOrder.remove(c)
                }
            }
            for (c in myCart) {
                totalQty += c.quantity
            }
            binding.tvTotalCart.text = "(${totalQty})"

        }



        return binding.root
    }

}