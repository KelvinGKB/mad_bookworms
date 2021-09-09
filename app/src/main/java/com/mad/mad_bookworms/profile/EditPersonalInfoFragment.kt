package com.mad.mad_bookworms.profile

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.cropToBlob
import com.mad.mad_bookworms.data.User
import com.mad.mad_bookworms.databinding.FragmentEditPersonalInfoBinding
import com.mad.mad_bookworms.databinding.FragmentReferralBinding
import com.mad.mad_bookworms.showEmailDialog
import com.mad.mad_bookworms.viewModels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.core.view.isVisible
import com.mad.mad_bookworms.toBitmap
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.InputStream


class EditPersonalInfoFragment : Fragment() {


    private val vm : UserViewModel by viewModels()

    private lateinit var binding: FragmentEditPersonalInfoBinding


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {

            binding.imgProfile.isVisible = false
            binding.imgProfile.setImageURI(it.data?.data)
            val img = binding.imgProfile.cropToBlob(400, 400)
            binding.imgProfile.setImageBitmap(img.toBitmap())
            binding.imgProfile.isVisible = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_personal_info, container, false)

        binding.edtImg.setOnClickListener { insertImage() }
        binding.btnUpdateInfo.setOnClickListener { updatePersonaInfo() }


        CoroutineScope(Dispatchers.IO).launch {

            val u = vm.get(Firebase.auth.currentUser!!.uid)

            val img = u?.photo
            val username = u?.username
            val email = u?.email
            val name = u?.fullname
            val contact = u?.contact
            val address = u?.address
            val state = u?.state
            val postal = u?.postal
            val city = u?.city


            Handler(Looper.getMainLooper()).postDelayed({

                binding.edtUsername.editText?.setText(username)
                binding.edtEmail.editText?.setText(email)
                binding.edtEmail.editText?.hint = email
                binding.edtName.editText?.setText(name)
                binding.edtContactNumber.editText?.setText(contact)
                binding.edtAddress.editText?.setText(address)
                binding.edtState.editText?.setText(state)
                binding.edtPostal.editText?.setText(postal)
                binding.edtCity.editText?.setText(city)

                Log.d(ContentValues.TAG, "HI" + img!!.toBitmap())

                if(img!!.toBitmap() != null){
                    binding.imgProfile.setImageBitmap(img!!.toBitmap())
                }else{
                    binding.imgProfile.setImageResource(R.drawable.user_profile_1)
                }

            }, 200)

        }

        return binding.root
    }

    fun insertImage(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    fun updatePersonaInfo(){

        val username = binding.edtUsername.editText?.text.toString().trim()
        val name = binding.edtName.editText?.text.toString().trim()
        val email = binding.edtEmail.editText?.text.toString().trim()
        val contact = binding.edtContactNumber.editText?.text.toString()
        val address = binding.edtAddress.editText?.text.toString().trim()
        val state = binding.edtState.editText?.text.toString().trim()
        val postal = binding.edtPostal.editText?.text.toString().trim()
        val city = binding.edtCity.editText?.text.toString().trim()

        val animFadeIn: Animation = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in)
        val animFadeOut: Animation = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out)

        Log.d(ContentValues.TAG, contact.length.toString())

        val e = validateInfo()
        if (e != "") {
            showText(e)
            Log.d(ContentValues.TAG, e)
            return
        }

        binding.tvButton.startAnimation(animFadeIn)
        binding.tvButton.setText(getString(R.string.updating));

        val taddress = address + "," + state + "," + postal + "," + city
//        showText(taddress)

        val u = User(
            id    = Firebase.auth.currentUser!!.uid,
            email = email,
            username  = username,
            contact = contact,
            address = address,
            state = state,
            postal = postal,
            city = city,
            fullname = name,
            photo = binding.imgProfile.cropToBlob(400, 400),
        )
//        vm.set(u)

        vm.updateUserInfo(Firebase.auth.currentUser!!.uid,u)

        showEmailDialog(activity, 2, getString(R.string.information_updated))
        binding.tvButton.setText("update")

        Handler(Looper.getMainLooper()).postDelayed({

            binding.tvButton.startAnimation(animFadeIn)

            setCurrentFragment(ProfileFragment())

        }, 2000)



    }
    private fun setCurrentFragment(fragment: Fragment)=
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    fun showText(text : String ){
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show()
    }

    fun validateInfo() : String{

        val username = binding.edtUsername.editText?.text.toString().trim()
        val name = binding.edtName.editText?.text.toString().trim()
        val contact = binding.edtContactNumber.editText?.text.toString()
        val address = binding.edtAddress.editText?.text.toString().trim()
        val state = binding.edtState.editText?.text.toString().trim()
        val postal = binding.edtPostal.editText?.text.toString().trim()
        val city = binding.edtCity.editText?.text.toString().trim()

        var e = ""

        e += if (username == "") getString(R.string.username_required)
        else if (username.length < 8) "\n" + getString(R.string.username_short)
        else ""

        e += if (name == "") "\n" + getString(R.string.name_requried)
        else if (name.length < 6) "\n" +getString(R.string.name_short)
        else ""

        e +=if (contact == "") "\n" +getString(R.string.contact_required)
        else if (contact.length < 10 || contact.length > 11) "\n" +getString(R.string.invaliad_contact)
        else ""

        e += if (address == "") "\n" +getString(R.string.address_required)
        else if (address.length < 10) "\n" +getString(R.string.address_short)
        else ""

        e += if (state == "") "\n" +getString(R.string.state_required)
        else ""

        e += if (postal == "") "\n" +getString(R.string.postal_requried)
        else if (postal.length != 5) "\n" +getString(R.string.postal_invalid)
        else ""

        e += if (city == "") "\n" +getString(R.string.city_required)
        else ""


        return e

    }


}