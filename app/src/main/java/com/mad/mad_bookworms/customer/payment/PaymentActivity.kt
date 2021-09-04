package com.mad.mad_bookworms.customer.payment

import android.R.attr
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
import com.mad.mad_bookworms.viewModels.UserVoucherViewModel
import kotlinx.coroutines.launch
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalService

import android.content.Intent
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PaymentActivity
import java.math.BigDecimal
import org.json.JSONException

import android.R.attr.data
import android.app.Activity
import com.paypal.android.sdk.payments.PaymentActivity.*

import com.paypal.android.sdk.payments.PaymentConfirmation
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject


class PaymentActivity : AppCompatActivity(), PaymentResultListener {

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
    private val vmVoucher: UserVoucherViewModel by viewModels()
    private var totalItemQty : Int = 0
    private var placeOrderAmt : Double = 0.0
    private var subTotalAmt : Double = 0.0
    private var totalOrderAmt : Double = 9.0
    private var selectedPayMethod : String = ""
    private var selected_position: Int = -1

    //Setup Paypal Configurations
    val PAYPAL_CLIENT_ID =
        "AQJ7akZbEZkksaD7mfUMoUdbsoznpa23hzTJNqVLA4JEvRSPrTl8O_zPa-a_QgFCPjJ4Hj3_56LCQr4q"
    private val PAYPAL_REQUEST_CODE: Int = 7171

    private val config = PayPalConfiguration()
        .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
        .clientId(PAYPAL_CLIENT_ID)

    override fun onStart() {
        super.onStart()
        //Payment Method
//        paymentMethod.add(PaymentMethod("RazorPay", R.drawable.razorpay_icon))
//        paymentMethod.add(PaymentMethod("Paypal", R.drawable.paypal_icon))
//        paymentMethod.add( PaymentMethod("BookWorm Wallet", R.drawable.ic_baseline_account_balance_wallet_24))
//        paymentMethod.add(PaymentMethod("Member Point", R.drawable.ic_baseline_redeem_24))
//        paymentMethodAdapter.submitList(paymentMethod)

        val pendingOrder = intent.getParcelableArrayListExtra<PendingOrder>("pendingOrder") as ArrayList<PendingOrder>
        orderAdapter.submitList(pendingOrder)
    }

    override fun onDestroy() {
        //Stop paypal service
        stopService(Intent(this, PayPalService::class.java))
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //add payment method
        paymentMethod.add(PaymentMethod("RazorPay", R.drawable.razorpay_icon))
        paymentMethod.add(PaymentMethod("Paypal", R.drawable.paypal_icon))
        paymentMethod.add( PaymentMethod("BookWorm Wallet", R.drawable.ic_baseline_account_balance_wallet_24))
        paymentMethod.add(PaymentMethod("Member Point", R.drawable.ic_baseline_redeem_24))
        paymentMethodAdapter.submitList(paymentMethod)

        //Start Paypal service
        val intent = Intent(this, PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        startService(intent)

        //RazorPay
        Checkout.preload(applicationContext)

        binding.tvApplyVoucher.isClickable = true
       // val pendingOrder = intent.getParcelableArrayListExtra<PendingOrder>("pendingOrder") as ArrayList<PendingOrder>

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
                    subTotalAmt += pricePerOrder
                    totalOrderAmt += subTotalAmt
                    placeOrderAmt = totalOrderAmt


                    //Binding Payment Details
                    binding.tvTotalItem.text = "[${totalItemQty} item(s)]"
                    binding.tvSubtotalAmt.text = "RM" + "%.2f".format(subTotalAmt)
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
                processPayment(placeOrderAmt,selectedPayMethod)
                //Toast.makeText(this,selectedPayMethod , Toast.LENGTH_SHORT).show()
                Log.d("TAG", "$placeOrderAmt")
            }

        }

        binding.rvPaymentMethod.adapter = paymentMethodAdapter
        binding.rvPaymentMethod.setHasFixedSize(true)

        val voucherCode = binding.edtVoucherCode.text.toString()
        binding.tvApplyVoucher.setOnClickListener {
            if(binding.edtVoucherCode.text.toString().isNotBlank()){
                lifecycleScope.launch {
                    var beforeDis = totalOrderAmt
                    val v = vmVoucher.get(binding.edtVoucherCode.text.toString().toUpperCase())
                    if(v == null) {
                        Toast.makeText(applicationContext,"This voucher does not exist." , Toast.LENGTH_SHORT).show()
                        binding.edtVoucherCode.setBackgroundColor(Color.parseColor("#e65972"))
                        binding.tvTotalOrderAmt.text = "RM" + "%.2f".format(beforeDis)
                    }
                    else if (v != null) {
                        binding.edtVoucherCode.setBackgroundColor(Color.parseColor("#DDDADA"))
                        if (v.type == "2"){
                            val disPrice : Double = v.discount.toDouble()
                            binding.tvVoucherDisAmt.text = "(-) RM" + "%.2f".format(disPrice)
                            beforeDis -= disPrice
                            placeOrderAmt = beforeDis
                            binding.tvTotalOrderAmt.text = "RM" + "%.2f".format(beforeDis)
                        }
                        else if (v.type == "1"){
                            val disPrice = 9.00
                            binding.tvDiscountText.text = "Shipping Fees Discount"
                            binding.tvVoucherDisAmt.text = "(-) RM" + "%.2f".format(disPrice)
                            beforeDis -= disPrice
                            placeOrderAmt = beforeDis
                            binding.tvTotalOrderAmt.text = "RM" + "%.2f".format(beforeDis)
                        }
                        binding.edtVoucherCode.clearFocus()
                        Toast.makeText(applicationContext,"Voucher Applied" , Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }



    }

    private fun processPayment(amount: Double, paymentType: String) {
        if (paymentType == "Paypal"){
            val payPalPayment = PayPalPayment(
                BigDecimal(amount.toString()), "MYR",
                "Pay To BookWorm", PayPalPayment.PAYMENT_INTENT_SALE
            )
            val intent = Intent(this, com.paypal.android.sdk.payments.PaymentActivity::class.java)
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
            intent.putExtra(EXTRA_PAYMENT,payPalPayment)
            startActivityForResult(intent, PAYPAL_REQUEST_CODE)
        }
        else if (paymentType == "BookWorm Wallet"){
            Toast.makeText(this,selectedPayMethod , Toast.LENGTH_SHORT).show()
        }
        else if (paymentType == "Member Point") {
            Toast.makeText(this,selectedPayMethod , Toast.LENGTH_SHORT).show()
        }
        else if (paymentType == "RazorPay"){
            val co = Checkout()

            try {
                val options = JSONObject()
                options.put("name","Pay For Book Worm")
                options.put("description","Making Payment to Book Worm")
                //You can omit the image option to fetch the image from dashboard
                options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
                options.put("theme.color", "#3399cc");
                options.put("currency","MYR");
                options.put("amount",amount * 100)//pass amount in currency subunits

//                val retryObj =  JSONObject();
//                retryObj.put("enabled", true);
//                retryObj.put("max_count", 4);
//                options.put("retry", retryObj);

                val prefill = JSONObject()
                prefill.put("email","chiater0311@gmail.com")
                prefill.put("contact","+60167510332")

                options.put("prefill",prefill)
                co.open(this,options)
            }catch (e: Exception){
                Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PAYPAL_REQUEST_CODE) {

            if (resultCode === RESULT_OK) {

                val confirmation: PaymentConfirmation? =
                    data?.getParcelableExtra(EXTRA_RESULT_CONFIRMATION)

                if (confirmation != null) {

                    try {
                        val paymentDetails = confirmation.toJSONObject().toString(4)

                        startActivity(Intent(this, PaymentDetailsActivity::class.java)
                            .putExtra("PaymentDetails",paymentDetails)
                            .putExtra("PaymentAmount", placeOrderAmt ))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }
            else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            }
            else if (resultCode == RESULT_EXTRAS_INVALID){
                Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Success: $p0", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed: $p1", Toast.LENGTH_SHORT).show()
    }

}