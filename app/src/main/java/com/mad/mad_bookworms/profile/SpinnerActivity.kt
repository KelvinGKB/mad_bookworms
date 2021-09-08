package com.mad.mad_bookworms.profile

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.MainActivity
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.ActivitySpinnerBinding
import com.mad.mad_bookworms.profile.spinWheel.LuckyWheelView
import com.mad.mad_bookworms.showEmailDialog
import com.mad.mad_bookworms.showMultiuseDialog
import com.mad.mad_bookworms.viewModels.UserViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SpinnerActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpinnerBinding
    private val vm: UserViewModel by viewModels()

    /// Hide Action Bar
    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar!!.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data: MutableList<LuckyItem> = ArrayList()

        val luckyItem1 = LuckyItem()
        luckyItem1.topText = "5"
        luckyItem1.secondaryText = "POINTS"
        luckyItem1.textColor = Color.parseColor("#212121")
        luckyItem1.color = Color.parseColor("#eceff1")
        data.add(luckyItem1)

        val luckyItem2 = LuckyItem()
        luckyItem2.topText = "10"
        luckyItem2.secondaryText = "POINTS"
        luckyItem2.color = Color.parseColor("#00cf00")
        luckyItem2.textColor = Color.parseColor("#ffffff")
        data.add(luckyItem2)

        val luckyItem3 = LuckyItem()
        luckyItem3.topText = "15"
        luckyItem3.secondaryText = "POINTS"
        luckyItem3.textColor = Color.parseColor("#212121")
        luckyItem3.color = Color.parseColor("#eceff1")
        data.add(luckyItem3)

        val luckyItem4 = LuckyItem()
        luckyItem4.topText = "20"
        luckyItem4.secondaryText = "POINTS"
        luckyItem4.color = Color.parseColor("#7f00d9")
        luckyItem4.textColor = Color.parseColor("#ffffff")
        data.add(luckyItem4)

        val luckyItem5 = LuckyItem()
        luckyItem5.topText = "25"
        luckyItem5.secondaryText = "POINTS"
        luckyItem5.textColor = Color.parseColor("#212121")
        luckyItem5.color = Color.parseColor("#eceff1")
        data.add(luckyItem5)

        val luckyItem6 = LuckyItem()
        luckyItem6.topText = "30"
        luckyItem6.secondaryText = "POINTS"
        luckyItem6.color = Color.parseColor("#dc0000")
        luckyItem6.textColor = Color.parseColor("#ffffff")
        data.add(luckyItem6)

        val luckyItem7 = LuckyItem()
        luckyItem7.topText = "35"
        luckyItem7.secondaryText = "POINTS"
        luckyItem7.textColor = Color.parseColor("#212121")
        luckyItem7.color = Color.parseColor("#eceff1")
        data.add(luckyItem7)

        val luckyItem8 = LuckyItem()
        luckyItem8.topText = "0"
        luckyItem8.secondaryText = "POINTS"
        luckyItem8.color = Color.parseColor("#008bff")
        luckyItem8.textColor = Color.parseColor("#ffffff")
        data.add(luckyItem8)

        binding.wheelview.setData(data)
        binding.wheelview.setRound(5)

        binding.spinBtn.setOnClickListener {

            val uid = Firebase.auth.currentUser?.uid

            if (uid != null) {
                lifecycleScope.launch {
                    var formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val currentDate = formatter.format(Date())
                    var today = Date()

                    today = formatter.parse(currentDate); // Date with 0:00:00

                    val u = vm.get(uid)

                    if (u != null) {
                        if (u.lastSpinDate.before(today)) {
                            val r = Random()
                            val randomNumber = r.nextInt(8)
                            binding.wheelview.startLuckyWheelWithTargetIndex(randomNumber)
                        } else {
                            Toast.makeText(
                                this@SpinnerActivity,
                                "Sorry. You have already spin the wheel today. Please comeback tomorrow.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }


        }

        binding.wheelview.setLuckyRoundItemSelectedListener(object :
            LuckyWheelView.LuckyRoundItemSelectedListener {
            override fun LuckyRoundItemSelected(index: Int) {
                updateCash(index)
            }
        })

    }

    fun updateCash(index: Int) {
        var cash: Double = 0.0
        when (index) {
            0 -> cash = 5.0
            1 -> cash = 10.0
            2 -> cash = 15.0
            3 -> cash = 20.0
            4 -> cash = 25.0
            5 -> cash = 30.0
            6 -> cash = 35.0
            7 -> cash = 0.0
        }

        val uid = Firebase.auth.currentUser?.uid

        if (uid != null) {
            lifecycleScope.launch {
                var formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val currentDate = formatter.format(Date())
                var today = Date()

                today = formatter.parse(currentDate); // Date with 0:00:00

                val u = vm.get(uid)

                if (u != null) {

                    var currentEarnPoint = u.earn_points.toInt()
                    var currentUsablePoint = u.usable_points.toInt()

                    vm.addCashBackPoints(
                        uid,
                        currentEarnPoint,
                        currentUsablePoint,
                        cash,
                        "spinWheel_earn"
                    )
                    vm.updateLastSpinDate(uid, Calendar.getInstance().time)

                    if (cash > 0.0) {
                        val title = "Congratulation!"
                        val text = "Oh WOW, AMAZING LUCK! You have earned $cash points!"
                        showMultiuseDialog(this@SpinnerActivity, 2, title, text)
                    } else if (cash == 0.0) {
                        val title = "No Luck Today.."
                        val text = "OH NO... Not your luck today... You have earned $cash points today."
                        showMultiuseDialog(this@SpinnerActivity, 3, title, text)
                    }

                }
//
//                Handler(Looper.getMainLooper()).postDelayed({
//
//
//                }, 500)
            }
        }

    }
}