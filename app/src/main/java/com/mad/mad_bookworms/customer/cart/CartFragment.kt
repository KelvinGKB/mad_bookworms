package com.mad.mad_bookworms.customer.cart

import android.app.AlertDialog
import android.content.Context
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.BadgeDrawable
import com.mad.mad_bookworms.MainActivity
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.customer.bookDetail.BookDetailActivity
import com.mad.mad_bookworms.customer.explore.RecyclerAdapter
import com.mad.mad_bookworms.customer.payment.PaymentActivity
import com.mad.mad_bookworms.data.*
import com.mad.mad_bookworms.databinding.FragmentCartBinding
import com.mad.mad_bookworms.toBitmap
import com.mad.mad_bookworms.viewModels.BookViewModel
import com.mad.mad_bookworms.viewModels.CartOrderViewModel
import com.mad.mad_bookworms.viewModels.MyFavouriteViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.badge_menu_item.view.*
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
    private val favouriteVm: MyFavouriteViewModel by activityViewModels()
    private var totalPrice: Double = 0.0

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
            val uid = Firebase.auth.currentUser?.uid
            holder.adapterPosition
            var qty = MyCartTable.quantity

            holder.root.setOnClickListener {

                val intent = Intent(requireContext(), BookDetailActivity::class.java)
                intent.putExtra("bookID", MyCartTable.bookId)

                startActivity(intent)
            }

            lifecycleScope.launch {
                val f = vm.get(MyCartTable.bookId)
                var price: Double
                if (f != null) {
                    price = f.price * holder.edtQty.text.toString().toInt()
                    holder.tvBookTitle.text = f.title
                    holder.tvBookAuthor.text = f.author
                    holder.tvBookPrice.text = "RM" + "%.2f".format(price)
                    holder.itemImage.setImageBitmap(f.image.toBitmap())


                    holder.chkCartOrder.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            pendingOrder.add(MyCartTable)
                            totalPrice = binding.tvTotalPrice.text.toString().toDouble() + price
                            binding.tvTotalPrice.text = "%.2f".format(totalPrice)

                        }
                        if (!isChecked) {
                            if (pendingOrder.contains(MyCartTable)) {
                                pendingOrder.remove(MyCartTable)
                                totalPrice = binding.tvTotalPrice.text.toString().toDouble() - price
                                binding.tvTotalPrice.text = "%.2f".format(totalPrice)

                            }
                        }
                    }

                }

            }

            holder.btnIncrease.setOnClickListener {
                qty += 1
                if (uid != null) {
                    cartVm.updateQty(uid, MyCartTable.bookId, qty)
                }
                if (holder.chkCartOrder.isChecked) {
                    lifecycleScope.launch {
                        val f = vm.get(MyCartTable.bookId)
                        var price: Double
                        if (f != null) {
                            price = f.price * holder.edtQty.text.toString().toInt()
                            totalPrice = binding.tvTotalPrice.text.toString().toDouble() + price
                            binding.tvTotalPrice.text = "%.2f".format(totalPrice)
                        }
                    }
                }

            }

            holder.btnMinus.setOnClickListener {
                qty -= 1
                if (qty < 1) {
                    qty = 1
                }
                if (uid != null) {
                    cartVm.updateQty(uid, MyCartTable.bookId, qty)
                }
                if (holder.chkCartOrder.isChecked) {
                    lifecycleScope.launch {
                        val f = vm.get(MyCartTable.bookId)
                        var price: Double
                        if (f != null) {
                            price = f.price * holder.edtQty.text.toString().toInt()
                            totalPrice = binding.tvTotalPrice.text.toString().toDouble() + price
                            binding.tvTotalPrice.text = "%.2f".format(totalPrice)
                        }
                    }
                }

            }
            holder.edtQty.setText("${MyCartTable.quantity}")


        }

        binding.rvCartOrder.adapter = adapter
        binding.rvCartOrder.setHasFixedSize(true)

        binding.btnDelete.setOnClickListener {
            if (pendingOrder.isNotEmpty()) {
                var builder = AlertDialog.Builder(activity)
                builder.setTitle(getString(R.string.confirm_delete))
                builder.setMessage(getString(R.string.delete_confirmation_message))
                builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    for (b in pendingOrder) {
                        cartVm.delete(b)

                    }
                    pendingOrder.clear()
                    dialog.cancel()
                })
                builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
                var alert = builder.create()
                alert.show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_selected_any_item_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnCheckOut.setOnClickListener {
            if (pendingOrder.isNotEmpty()) {
                prepareOrder.clear()
                for (o in pendingOrder) {
                    prepareOrder.add(PendingOrder(o.bookId, o.quantity))
                }

                val intent = Intent(requireContext(), PaymentActivity::class.java)
                intent.putExtra("totalAmount", totalPrice)
                intent.putExtra("pendingOrder", ArrayList(prepareOrder))

                startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_selected_any_item_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        val swipeGesture = object : SwipeGesture(requireContext()) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when (direction) {

                    ItemTouchHelper.LEFT -> {
                        cartVm.delete(data[viewHolder.adapterPosition])
                        data.removeAt(viewHolder.adapterPosition)
                        adapter.submitList(data)


                    }



                }


            }

        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        binding.rvCartOrder.apply {
            touchHelper.attachToRecyclerView(this)
        }

        cartVm.getAll().observe(viewLifecycleOwner) { myCart ->
            val o: MutableList<MyCartTable> = ArrayList()
            o.clear()
            val uid = Firebase.auth.currentUser?.uid
            if (uid != null) {
                for (c in myCart) {
                    if (c.uid == uid) {

                        o.add(c)
                    }
                    var totalQty = 0


                    for (c in o) {
                        if (pendingOrder.contains(c)) {
                            pendingOrder.remove(c)
                        }
                    }
                    for (c in o) {
                        totalQty += c.quantity
                    }

                    binding.tvTotalCart.text = "(${totalQty})"

                }
                adapter.submitList(o)
                data.addAll(o)

            }


        }




        return binding.root
    }


}