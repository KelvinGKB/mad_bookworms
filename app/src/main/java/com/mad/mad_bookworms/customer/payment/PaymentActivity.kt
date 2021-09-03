package com.mad.mad_bookworms.customer.payment

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.MyCartTable
import com.mad.mad_bookworms.data.PaymentMethod
import com.mad.mad_bookworms.data.PendingOrder
import com.mad.mad_bookworms.data.SelectedPayMethod
import com.mad.mad_bookworms.databinding.ActivityPaymentBinding
import com.mad.mad_bookworms.toBitmap
import com.mad.mad_bookworms.viewModels.BookViewModel
import kotlinx.coroutines.launch

class PaymentActivity : AppCompatActivity() {

    /// Hide Action Bar
    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar!!.show()
    }



    private lateinit var binding: ActivityPaymentBinding
    private lateinit var orderAdapter: PlaceOrderAdapter
    private val paymentMethod: MutableList<PaymentMethod> = ArrayList()
    private val payMethod: MutableList<SelectedPayMethod> = ArrayList()
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private val vm: BookViewModel by viewModels()
    private var totalItemQty : Int = 0
    private var subTotalAmt : Double = 0.0
    private var totalOrderAmt : Double = 0.0
    private var selectedPayMethod : String = ""
    private var selected_position: Int = -1

    override fun onStart() {
        super.onStart()
        //Payment Method
        paymentMethod.add(PaymentMethod("Paypal", R.drawable.paypal_icon))
        paymentMethod.add( PaymentMethod("BookWorm Wallet", R.drawable.ic_baseline_account_balance_wallet_24))
        paymentMethod.add(PaymentMethod("Member Point", R.drawable.ic_baseline_redeem_24))
        paymentMethodAdapter.submitList(paymentMethod)

        val pendingOrder = intent.getParcelableArrayListExtra<PendingOrder>("pendingOrder") as ArrayList<PendingOrder>
        orderAdapter.submitList(pendingOrder)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pendingOrder = intent.getParcelableArrayListExtra<PendingOrder>("pendingOrder") as ArrayList<PendingOrder>

        //Recycler View for order
        orderAdapter = PlaceOrderAdapter() { holder, order ->

            totalItemQty += order.quantity

            lifecycleScope.launch {
                val o = vm.get(order.bookId)
                var pricePerOrder : Double
                if (o!=null) {
                    holder.tvBookTitle.text = o.title
                    holder.tvBookAuthor.text = o.author
                    holder.tvBookPrice.text = "RM " + "%.2f".format(o.price)
                    holder.itemImage.setImageBitmap(o.image.toBitmap())

                    pricePerOrder = o.price * order.quantity
                    totalOrderAmt += pricePerOrder

                    //Binding Payment Details
                    binding.tvTotalItem.text = "[${totalItemQty} item(s)]"
                    binding.tvSubtotalAmt.text = "RM" + "%.2f".format(totalOrderAmt)
                    binding.tvTotalOrderAmt.text = "RM" + "%.2f".format(totalOrderAmt)
                }



            }

        }

        binding.rvPlaceOrder.adapter = orderAdapter
        binding.rvPlaceOrder.setHasFixedSize(true)


        paymentMethodAdapter = PaymentMethodAdapter() { holder, payMethod ->

            holder.itemView.setOnClickListener {

                selected_position = holder.position
                selectedPayMethod = payMethod.PaymentName
                binding.rvPaymentMethod.adapter!!.notifyDataSetChanged()

            }

            if (selected_position == holder.adapterPosition) {
                holder.cvPayMethod.setCardBackgroundColor(Color.GRAY)
            }

            if (selected_position != holder.adapterPosition){
                holder.cvPayMethod.setCardBackgroundColor(Color.parseColor("#F5F1F1"))
            }


            //Handle Button Clicking Events
            binding.btnPlaceOrder.setOnClickListener {
                Toast.makeText(this,selectedPayMethod , Toast.LENGTH_SHORT).show()
                Log.d("TAG", "$pendingOrder")
            }

        }

        binding.rvPaymentMethod.adapter = paymentMethodAdapter
        binding.rvPaymentMethod.setHasFixedSize(true)





    }
}