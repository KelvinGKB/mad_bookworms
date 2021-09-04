package com.mad.mad_bookworms.customer.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.ActivityPaymentDetailsBinding
import org.json.JSONException

import org.json.JSONObject

import android.content.Intent




class PaymentDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        
        //get intent

        val intent = intent

        try {
            val jsonObject = JSONObject(intent.getStringExtra("PaymentDetails"))
            showDetails(
                jsonObject.getJSONObject("response"),
                intent.getStringExtra("PaymentAmount")
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }


    }

    private fun showDetails(response: JSONObject, paymentAmount: String?) {
        try {
            binding.txtId.setText(response.getString("id"))
            binding.txtStatus.setText(response.getString("state"))
            binding.txtAmount.setText("RM" + "%.2f".format(paymentAmount))
        } catch (e: JSONException){
            e.printStackTrace()
        }
    }
}