package com.example.shopdrop.presentation.razorpay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopdrop.R
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity(), PaymentResultWithDataListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        Checkout.preload(this)
        val intent = intent
        val amount = intent.getStringExtra("amount")
        val email = intent.getStringExtra("email")
        val phone = intent.getLongExtra("phone", 0)

        startPayment(amount!!, email!!, phone)


    }

    private fun startPayment(amount: String, email: String, phone: Long) {
        val activity: Activity = this
        val co = Checkout()
        co.setKeyID("rzp_test_mrAItCJ56ubi6y")

        try {
            val options = JSONObject()
            options.put("name", "Shop Drop")
            options.put("description", "Total Amount")
            options.put("currency", "INR")
            options.put("amount", "$amount")


            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 0)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", "$email")
            prefill.put("contact", "$phone")

            options.put("prefill", prefill)

            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        val response = Intent()
        response.putExtra("response", true)
        setResult(RESULT_OK, response)
        finish()
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        val response = Intent()
        response.putExtra("response", false)
        setResult(RESULT_OK, response)
        finish()
    }


}