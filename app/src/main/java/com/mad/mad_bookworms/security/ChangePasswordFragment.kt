package com.mad.mad_bookworms.security

import com.mad.mad_bookworms.profile.ProfileFragment
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.FragmentChangePasswordBinding
import com.mad.mad_bookworms.showEmailDialog
import com.mad.mad_bookworms.showMultiuseDialog

class ChangePasswordFragment : Fragment() {

    private fun setCurrentFragment(fragment: Fragment)=
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    private lateinit var binding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)

        binding.btnChange.setOnClickListener(){

            val animFadeIn: Animation = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in)
            val animFadeOut: Animation = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out)

            val conPassword = binding.edtConPassword.editText?.text.toString()
            val password = binding.edtPassword.editText?.text.toString()

            if(password == null || password == "")
            {
                showText("Password is required !")
                return@setOnClickListener
            }
            if(password.length < 6 )
            {
                showText("Password must be at least 6 characters !")
                return@setOnClickListener
            }
            if(conPassword == null || conPassword == "")
            {
                showText("Confirm Password is required !")
                return@setOnClickListener
            }
            if(password != conPassword)
            {
                showText("Confirm Password does not match Password !")
                return@setOnClickListener
            }

            binding.tvButton.startAnimation(animFadeIn)
            binding.tvButton.setText("Changing...");

            val user = Firebase.auth.currentUser
            val newPassword = password

            user!!.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User password updated.")

                        showEmailDialog(activity, 2, "Password changed")
                        binding.tvButton.setText("Change")

                        Handler(Looper.getMainLooper()).postDelayed({
//                            binding.tvButton.startAnimation(animFadeIn)
//                            binding.tvButton.setText("Password Changed")

                            binding.tvButton.startAnimation(animFadeIn)

                            setCurrentFragment(ProfileFragment())

                        }, 2000)

                    }else{
                        Log.d(TAG, "User password update unsuccessful.")

                        val title = "Oh No..."
                        val content = "password change unsuccessfully. Please try again later"

                        showMultiuseDialog(activity,3,title,content)
                        binding.tvButton.setText("Change")
                    }
                }


        }

        return binding.root
    }

    fun showText(text : String ){
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show()
    }



}