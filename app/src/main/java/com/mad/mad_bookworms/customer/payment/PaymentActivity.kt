package com.mad.mad_bookworms.customer.payment

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.databinding.ActivityPaymentBinding
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
import android.os.strictmode.IntentReceiverLeakedViolation
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.*
import com.mad.mad_bookworms.data.*
import com.mad.mad_bookworms.viewModels.*
import com.paypal.android.sdk.payments.PaymentActivity.*

import com.paypal.android.sdk.payments.PaymentConfirmation
import com.razorpay.Checkout
import com.razorpay.CheckoutActivity
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList


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
    private var appliedVCode: MyVoucher? = null
    private val allOrder: MutableList<PendingOrder> = ArrayList()
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private val vm: BookViewModel by viewModels()
    private val vmVoucher: UserVoucherViewModel by viewModels()
    private val vmBookOrder: BookOrderViewModel by viewModels()
    private val vmMyCart: CartOrderViewModel by viewModels()
    private val vmOrder: OrderViewModel by viewModels()
    private val vmUser: UserViewModel by viewModels()
    private val vmBook: BookViewModel by viewModels()
    private var totalItemQty : Int = 0
    private var placeOrderAmt : Double = 0.0
    private var subTotalAmt : Double = 0.0
    private var totalRequiredPoint : Int = 0
    private var selectedPayMethod : String = ""
    private var selected_position: Int = -1
    private var applyVoucher: Boolean = false
    private var cashBackPoint: Double = 0.0
    private var userLevel: String = ""

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
        paymentMethod.add(PaymentMethod("RazorPay", R.drawable.razorpay_icon))
        paymentMethod.add(PaymentMethod("Paypal", R.drawable.paypal_icon))
        paymentMethod.add(PaymentMethod("Member Point", R.drawable.ic_baseline_redeem_24))
        paymentMethodAdapter.submitList(paymentMethod)

        val pendingOrder = intent.getParcelableArrayListExtra<PendingOrder>("pendingOrder") as ArrayList<PendingOrder>
        orderAdapter.submitList(pendingOrder)
        allOrder.addAll(pendingOrder)
    }

    override fun onDestroy() {
        //Stop paypal service
        stopService(Intent(this, PayPalService::class.java))
        paymentMethod.clear()
        super.onDestroy()
    }

    override fun onPause() {

        super.onPause()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        //add payment method
//        paymentMethod.add(PaymentMethod("RazorPay", R.drawable.razorpay_icon))
//        paymentMethod.add(PaymentMethod("Paypal", R.drawable.paypal_icon))
//        paymentMethod.add( PaymentMethod("BookWorm Wallet", R.drawable.ic_baseline_account_balance_wallet_24))
//        paymentMethod.add(PaymentMethod("Member Point", R.drawable.ic_baseline_redeem_24))
//        paymentMethodAdapter.submitList(paymentMethod)

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
                    //totalOrderAmt += subTotalAmt
                    placeOrderAmt = subTotalAmt+9


                    //Binding Payment Details
                    binding.tvTotalItem.text = "[${totalItemQty} item(s)]"
                    binding.tvSubtotalAmt.text = "RM" + "%.2f".format(subTotalAmt)
                    binding.tvTotalOrderAmt.text = "RM" + "%.2f".format(subTotalAmt+9)
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

            if (payMethod.PaymentName == "Paypal" ){
                holder.tvCashbackMessage.text = getString(R.string.earn_more_10_cashback)
            }
            else if (payMethod.PaymentName == "RazorPay"){
                holder.tvCashbackMessage.text = getString(R.string.earn_more_15_cashback)
            }


            //Handle Button Clicking Events
            binding.btnPlaceOrder.setOnClickListener {
                if(selectedPayMethod != ""){
                    processPayment(placeOrderAmt,selectedPayMethod)
                }else{
                    Toast.makeText(this,getString(R.string.please_select_a_payment_method) , Toast.LENGTH_SHORT).show()
                }


            }

        }

        binding.rvPaymentMethod.adapter = paymentMethodAdapter
        binding.rvPaymentMethod.setHasFixedSize(true)

        binding.tvApplyVoucher.setOnClickListener {
            if(binding.edtVoucherCode.text.toString().isNotBlank()){
                lifecycleScope.launch {
                    var beforeDis = placeOrderAmt
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
                            binding.tvTotalOrderAmt.text = "RM" + "%.2f".format(placeOrderAmt)
                            applyVoucher = true
                        }
                        else if (v.type == "1"){
                            val disPrice = 9.00
                            binding.tvDiscountText.text = "Shipping Fees Discount"
                            binding.tvVoucherDisAmt.text = "(-) RM" + "%.2f".format(disPrice)
                            beforeDis -= disPrice
                            placeOrderAmt = beforeDis
                            binding.tvTotalOrderAmt.text = "RM" + "%.2f".format(placeOrderAmt)
                            applyVoucher = true
                        }
                        //prepare to be deleted after payment is made
                        appliedVCode = v
                        binding.edtVoucherCode.clearFocus()
                        Toast.makeText(applicationContext,"Voucher Applied" , Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        //binding user address
        val uid = Firebase.auth.currentUser?.uid

        if (uid != null) {
            lifecycleScope.launch {
                val u = vmUser.get(uid)
                if (u != null) {
                    binding.tvDeliveryAddress.text = "${u.address} \n${u.city} \n${u.postal}, ${u.state}"
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
        else if (paymentType == "Member Point") {
            Log.d("TAG","MEMBER POINT PAYMENT METHOD")
            val currentDate = Date()
            //get uid of current user
            val uid = Firebase.auth.currentUser?.uid

            if (uid != null) {
                lifecycleScope.launch {
                    val u = vmUser.get(uid)
                    if (u!=null){
                        //get user usable point
                        val usablePoint = u.usable_points
                        //accumulate total required points to claim
                        for(b in allOrder){
                            val book = vmBook.get(b.bookId)
                            if (book != null){
                                totalRequiredPoint += (book.requiredPoint * b.quantity)
                            }
                        }
                        Log.d("TAG","totalrequiredpoint = $totalRequiredPoint")

                        if (usablePoint >= totalRequiredPoint){
                            val orderId = random() + random()
                            //Add to order
                            val f = uid?.let {
                                Order(
                                    id = orderId,
                                    amount = placeOrderAmt,
                                    dateTime = currentDate,
                                    paymentType = selectedPayMethod,
                                    status = "paid",
                                    uid = it
                                )

                            }

                            if (f != null) {
                                vmOrder.set(f)
                            }

                            //add to book order & delete order from cart
                            for (order in allOrder){
                                val rBookOrderId = random()
                                vmBookOrder.set(BookOrder(id= rBookOrderId,order_id = orderId, book_id = order.bookId, qty = order.quantity))
                                lifecycleScope.launch {
                                    if (uid != null) {
                                        vmMyCart.deleteCart(uid,order.bookId)
                                    }
                                }
                            }
                            vmUser.minusUsablePoints(uid,usablePoint,totalRequiredPoint)
                            try{
                                startActivity(Intent(this@PaymentActivity, RazorPaySuccess::class.java)
                                    .putExtra("PaymentAmount", placeOrderAmt)
                                    .putExtra("CashBackPoint", cashBackPoint)
                                    .putExtra("UserLevel", "redeemPoint")
                                    .putExtra("OrderId",orderId))
                            }catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        else{
                            val title = getString(R.string.not_enough_point)
                            val content = getString(R.string.share_bookworm_to_more_ppl)

                            showMultiuseDialog(this@PaymentActivity,3,title,content)
                        }
                    }

                }

            }
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
                        //get current datetime
                        val currentDate = Date()

                        //get random order id
                        val rOrderId:String = random()

                        //get uid of current user
                        val uid = Firebase.auth.currentUser?.uid

                        val paymentDetails = confirmation.toJSONObject().toString(4)
                        val jsonObject = JSONObject(paymentDetails)
                        val res = jsonObject.getJSONObject("response")
                        val orderId = res.getString("id")
                        //Add to order
                        val f = uid?.let {
                                Order(
                                    id = orderId,
                                    amount = placeOrderAmt,
                                    dateTime = currentDate,
                                    paymentType = selectedPayMethod,
                                    status = "paid",
                                    uid = it
                                )

                        }

                        if (f != null) {
                            vmOrder.set(f)
                        }

                        //add to book order & delete order from cart
                        for (order in allOrder){
                            val rBookOrderId = random()
                            vmBookOrder.set(BookOrder(id= rBookOrderId,order_id = orderId, book_id = order.bookId, qty = order.quantity))
                            lifecycleScope.launch {
                                if (uid != null) {
                                    vmMyCart.deleteCart(uid,order.bookId)
                                }
                            }
                        }

                        if (applyVoucher){
                            val f = appliedVCode?.let { MyVoucher(
                                id = it.id,
                                discount = it.discount,
                                expiry_date = it.expiry_date,
                                status = "used",
                                type = it.type,
                                uid = it.uid) }

                            if (f != null) {
                                vmVoucher.set(f)
                            }
                        }


                        if (uid != null) {
                            lifecycleScope.launch {
                                Log.d("TAG","UID NOT NULL")
                                val u = vmUser.get(uid)

                                if (u!=null){
                                    var currentEarnPoint = u.earn_points.toInt()
                                    var currentUsablePoint = u.usable_points.toInt()
                                    var paypalEarnPoint = 0
                                            Log.d("TAG","User NOT NULL")
                                    if (u.level == "Silver"){
                                        Log.d("TAG","User is silver")
                                        paypalEarnPoint = (placeOrderAmt * 0.1).toInt()
                                        vmUser.addCashBackPoints(uid,currentEarnPoint, currentUsablePoint,placeOrderAmt + paypalEarnPoint, u.level)
                                        cashBackPoint = paypalEarnPoint.toDouble()
                                    }
                                    else if (u.level == "Gold") {
                                        paypalEarnPoint = (placeOrderAmt * 0.1).toInt()
                                        vmUser.addCashBackPoints(uid,currentEarnPoint, currentUsablePoint,placeOrderAmt+paypalEarnPoint, u.level)
                                        cashBackPoint = placeOrderAmt * 1.2
                                    }
                                    else if (u.level == "Platinum"){
                                        paypalEarnPoint = (placeOrderAmt * 0.1).toInt()
                                        vmUser.addCashBackPoints(uid,currentEarnPoint, currentUsablePoint,placeOrderAmt+paypalEarnPoint, u.level)
                                        cashBackPoint = placeOrderAmt * 1.5
                                    }
                                    userLevel = u.level
                                    Log.d("TAG","cashBackPoint = $cashBackPoint")
                                    Log.d("TAG","userLevel = $userLevel")

                                    startActivity(Intent(this@PaymentActivity, PaymentDetailsActivity::class.java)
                                        .putExtra("PaymentDetails",paymentDetails)
                                        .putExtra("PaymentAmount", placeOrderAmt)
                                        .putExtra("CashBackPoint", cashBackPoint)
                                        .putExtra("UserLevel", userLevel))
                                }

                            }

                        }





                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }
            else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, getString(R.string.cancel), Toast.LENGTH_SHORT).show()
            }
            else if (resultCode == RESULT_EXTRAS_INVALID){
                Toast.makeText(this, getString(R.string.invalid), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        //get current datetime
        if (p0!=null){
            val currentDate = Date()
            //get uid of current user
            val uid = Firebase.auth.currentUser?.uid

            //Add to order
            val f = uid?.let {
                Order(
                    id = p0,
                    amount = placeOrderAmt,
                    dateTime = currentDate,
                    paymentType = selectedPayMethod,
                    status = "paid",
                    uid = it
                )

            }

            if (f != null) {
                vmOrder.set(f)
            }

            //add to book order & delete order from cart
            for (order in allOrder){
                val rBookOrderId = random()
                vmBookOrder.set(BookOrder(id= rBookOrderId,order_id = p0, book_id = order.bookId, qty = order.quantity))
                lifecycleScope.launch {
                    if (uid != null) {
                        vmMyCart.deleteCart(uid,order.bookId)
                    }
                }
            }

            if (applyVoucher){
                val f = appliedVCode?.let { MyVoucher(
                    id = it.id,
                    discount = it.discount,
                    expiry_date = it.expiry_date,
                    status = "used",
                    type = it.type,
                    uid = it.uid) }

                if (f != null) {
                    vmVoucher.set(f)
                }
            }
            if (uid != null) {
                lifecycleScope.launch {
                    Log.d("TAG","UID NOT NULL")
                    val u = vmUser.get(uid)

                    if (u!=null){
                        var currentEarnPoint = u.earn_points.toInt()
                        var currentUsablePoint = u.usable_points.toInt()
                        var razorPayEarnPoint = 0

                        if (u.level == "Silver"){
                            Log.d("TAG","User is silver")
                            razorPayEarnPoint = (placeOrderAmt * 0.1).toInt()
                            vmUser.addCashBackPoints(uid,currentEarnPoint, currentUsablePoint,placeOrderAmt + razorPayEarnPoint, u.level)
                            cashBackPoint = razorPayEarnPoint.toDouble()
                        }
                        else if (u.level == "Gold") {
                            razorPayEarnPoint = (placeOrderAmt * 0.1).toInt()
                            vmUser.addCashBackPoints(uid,currentEarnPoint, currentUsablePoint,placeOrderAmt+razorPayEarnPoint, u.level)
                            cashBackPoint = placeOrderAmt * 1.2
                        }
                        else if (u.level == "Platinum"){
                            razorPayEarnPoint = (placeOrderAmt * 0.1).toInt()
                            vmUser.addCashBackPoints(uid,currentEarnPoint, currentUsablePoint,placeOrderAmt+razorPayEarnPoint, u.level)
                            cashBackPoint = placeOrderAmt * 1.5
                        }
                        userLevel = u.level
                        Log.d("TAG","cashBackPoint = $cashBackPoint")
                        Log.d("TAG","userLevel = $userLevel")
                        try{
                            startActivity(Intent(this@PaymentActivity, RazorPaySuccess::class.java)
                                .putExtra("PaymentAmount", placeOrderAmt)
                                .putExtra("CashBackPoint", cashBackPoint)
                                .putExtra("UserLevel", userLevel)
                                .putExtra("OrderId",p0))
                        }catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                }

            }
        }

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed: $p1", Toast.LENGTH_SHORT).show()
    }

    fun random(): String {
        val generator = Random()
        val randomStringBuilder = StringBuilder()
        val randomLength = generator.nextInt(12)
        var tempChar: Char
        for (i in 0 until randomLength) {
            tempChar = (generator.nextInt(96) + 32).toChar()
            randomStringBuilder.append(tempChar)
        }
        return randomStringBuilder.toString()
    }


}