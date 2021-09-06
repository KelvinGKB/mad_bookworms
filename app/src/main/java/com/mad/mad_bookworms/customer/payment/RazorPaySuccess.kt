package com.mad.mad_bookworms.customer.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mad.mad_bookworms.MainActivity
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.ActivityRazorPaySuccessBinding
import com.mad.mad_bookworms.showMultiuseDialog

class RazorPaySuccess : AppCompatActivity() {

    private lateinit var binding: ActivityRazorPaySuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRazorPaySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get intent

        val intent = intent
        val cashBackPoint = intent.getDoubleExtra("CashBackPoint", 0.0)
        val userLevel = intent.getStringExtra("UserLevel")?:""
        val paymentAmount = intent.getDoubleExtra("PaymentAmount", 0.0)
        val orderId = intent.getStringExtra("OrderId")?:""

        if (userLevel == "Silver"){
            val point = "%.2f".format(cashBackPoint)
            val title = "You have earn $point cashback points."
            val content = "Share BookWorm to more people to and upgrade your level and earn more cashback points. "

            showMultiuseDialog(this,1,title,content)
        }
        else if (userLevel == "Gold"){
            val title = "Congratulation"
            val point = "%.2f".format(cashBackPoint)
            val content = "You have successfully earn $point cashback points."

            showMultiuseDialog(this,2,title,content)
        }
        else if (userLevel == "Platinum"){
            val title = "Congratulation"
            val point = "%.2f".format(cashBackPoint)
            val content = "You have successfully earn $point cashback points."

            showMultiuseDialog(this,2,title,content)
        }

        binding.txtAmount.text = "RM" + "%.2f".format(paymentAmount)
        binding.txtId.text = orderId
        binding.btnBackToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}