package com.mad.mad_bookworms.customer.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mad.mad_bookworms.MainActivity
import com.mad.mad_bookworms.databinding.ActivityPaymentDetailsBinding
import com.mad.mad_bookworms.showMultiuseDialog
import org.json.JSONException

import org.json.JSONObject


class PaymentDetailsActivity : AppCompatActivity() {


    /// Hide Action Bar
    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar!!.show()
    }

    private lateinit var binding: ActivityPaymentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        
        //get intent

        val intent = intent
        val cashBackPoint = intent.getDoubleExtra("CashBackPoint", 0.0)
        val userLevel = intent.getStringExtra("UserLevel")?:""

        if (userLevel == "Silver"){
            val point = "%.2f".format(cashBackPoint)
            val title = "You have earn $point cashback points."
            val content = "Share BookWorm to more people to and upgrade your level and earn more cashback points. "

            showMultiuseDialog(this,1,title,content)
        }
        else if (userLevel == "Gold"){
            val title = "Congratulation!"
            val point = "%.2f".format(cashBackPoint)
            val content = "You have successfully earn $point cashback points."

            showMultiuseDialog(this,2,title,content)
        }
        else if (userLevel == "Platinum"){
            val title = "Congratulation!"
            val point = "%.2f".format(cashBackPoint)
            val content = "You have successfully earn $point cashback points."

            showMultiuseDialog(this,2,title,content)
        }


        try {
            val jsonObject = JSONObject(intent.getStringExtra("PaymentDetails"))
            showDetails(
                jsonObject.getJSONObject("response"),
                intent.getDoubleExtra("PaymentAmount", 0.0)
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        binding.btnBackToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showDetails(response: JSONObject, paymentAmount: Double) {
        try {
            binding.txtId.text = response.getString("id")
            binding.txtStatus.text = "Payment Success"
            binding.txtAmount.text = "RM" + "%.2f".format(paymentAmount)
        } catch (e: JSONException){
            e.printStackTrace()
        }
    }
}