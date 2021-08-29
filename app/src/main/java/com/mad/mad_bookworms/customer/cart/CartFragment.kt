package com.mad.mad_bookworms.customer.cart

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.customer.explore.RecyclerAdapter
import com.mad.mad_bookworms.data.LocalDB
import com.mad.mad_bookworms.data.MyCartDao
import com.mad.mad_bookworms.data.MyCartTable
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
    private lateinit var myCartDao : MyCartDao
    private val vm: BookViewModel by activityViewModels()
    private val cartVm: CartOrderViewModel by activityViewModels()
    private var totalPrice : Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

        //myCartDao = LocalDB.getInstance(requireContext()).MyCartDao

        //Recycler View for cart order
        adapter = CartOrderAdapter() { holder, MyCartTable ->
            var qty = MyCartTable.quantity
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

        binding.btnDelete.setOnClickListener {
            Log.d("TAG", "${pendingOrder.isEmpty()}")
            if (pendingOrder.isNotEmpty()){
                var builder = AlertDialog.Builder(activity)
                builder.setTitle(getString(R.string.confirm_delete))
                builder.setMessage(getString(R.string.delete_confirmation_message))
                builder.setPositiveButton("Yes",DialogInterface.OnClickListener{ dialog, id ->
                    for (b in pendingOrder) {
                        cartVm.delete(b)
                    }
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

        binding.rvCartOrder.adapter = adapter
        binding.rvCartOrder.setHasFixedSize(true)

        cartVm.getAll().observe(viewLifecycleOwner) { myCart ->
            adapter.submitList(myCart)
        }



        return binding.root
    }

}