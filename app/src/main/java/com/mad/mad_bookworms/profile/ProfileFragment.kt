package com.mad.mad_bookworms.profile

import android.content.ContentValues
import android.content.Intent
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
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.SettingListAdapter
import com.mad.mad_bookworms.admin.AdminActivity
import com.mad.mad_bookworms.customer.bookDetail.BookDetailActivity
import com.mad.mad_bookworms.data.LocalDB
import com.mad.mad_bookworms.data.LocalDao
import com.mad.mad_bookworms.data.User_Table
import com.mad.mad_bookworms.databinding.FragmentProfileBinding
import com.mad.mad_bookworms.security.ChangePasswordFragment
import com.mad.mad_bookworms.security.LoginActivity
import com.mad.mad_bookworms.security.RegisterActivity
import com.mad.mad_bookworms.toBitmap
import com.mad.mad_bookworms.viewModels.UserViewModel
import kotlinx.coroutines.*
import java.util.*


class ProfileFragment : Fragment() {

    lateinit var dao: LocalDao
    private val vm: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileBinding
    lateinit var listItem: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        //Animation
        val animFadeIn: Animation =
            AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in)
        val animFadeOut: Animation =
            AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out)
        val listView = binding.listview

//        Handler(Looper.getMainLooper()).postDelayed({
//            if
//        },2000)

        binding.shimmerView.startShimmerAnimation()

        CoroutineScope(Dispatchers.IO).launch {
            loadUser()
        }

        binding.listview.setOnItemClickListener { parent, view, position, id ->

            when (listItem?.get(position)) {
                "Daily Check In" -> Toast.makeText(context, "Check In", Toast.LENGTH_SHORT).show()
                "Change Password" -> setCurrentFragment(ChangePasswordFragment())
                "Referral History" -> checkIn()
                "Purchase History" -> Toast.makeText(context, "Item One", Toast.LENGTH_SHORT).show()
                "My Favourites" -> Toast.makeText(context, "Item Two", Toast.LENGTH_SHORT).show()
                "Language" -> Toast.makeText(context, "Item Three", Toast.LENGTH_SHORT).show()
                "Refer a Friend" ->  CoroutineScope(Dispatchers.IO).launch { referFriend() }
                "Admin Panel" -> activity()
                "Log Out" -> signOut()
            }
        }

        binding.btnEditInfo.setOnClickListener{
            setCurrentFragment(EditPersonalInfoFragment())
        }
        return binding.root

    }

    private fun checkIn() {
        val uid = Firebase.auth.currentUser?.uid

        if (uid != null) {
            lifecycleScope.launch {
                val currentDate = Date()
                val u = vm.get(uid)

                if (u != null) {

                }
            }
        }

    }

    private fun activity(){
        Toast.makeText(context, "admin apge", Toast.LENGTH_SHORT).show()
        val intent = Intent(getActivity(), AdminActivity::class.java)
        startActivity(intent)
    }

    private fun setCurrentFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    fun signOut() {
        Firebase.auth.signOut()
        Toast.makeText(context, "Signed Out", Toast.LENGTH_SHORT).show()

//        CoroutineScope(Dispatchers.IO).launch {
//
//            val userList = Firebase.auth.currentUser?.let { dao.getUser(it.uid) }
////            val userList = dao.getUser()
//            Log.w(ContentValues.TAG, "Delete Data : " + userList.toString())
//            if(userList!=null)
//            {
//                Log.w(ContentValues.TAG, "Delete Data")
////                dao.deleteUser(u)
//            }else{
//                Log.w(ContentValues.TAG, "Not Delete Data")
//            }
//        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(getActivity(), LoginActivity::class.java)
            startActivity(intent)
        }, 1000)
    }

    suspend fun referFriend() = coroutineScope {

        val uid = Firebase.auth.currentUser?.uid

        val u = vm.get(uid!!)

        val id = u?.referral_code.toString()

        val link = "http://www.mad_bookworm.com/referral_register/" + id

        val text = "Join the Bookworms now with the links Below to earn points ! \n\n" +
                "*** Use TARUC student email will earn extra 50% points ! *** \n\n" +
                link

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)

    }

    suspend fun loadUser() = coroutineScope {

        //Animation
        val animFadeIn: Animation =
            AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in)
        val animFadeOut: Animation =
            AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out)

        val user = Firebase.auth.currentUser
        var role = ""

        val u = user?.let { vm.get(it.uid) }

        role = u?.role.toString()

        Handler(Looper.getMainLooper()).postDelayed({

            binding.shimmerView.stopShimmerAnimation()
            binding.shimmerView.visibility = View.GONE

            binding.profile.visibility = View.VISIBLE
            binding.layoutList.visibility = View.VISIBLE

            binding.tvName.startAnimation(animFadeIn)
            binding.tvName.setText(u?.username)

           if(u?.photo!!.toBitmap() != null){
               binding.imageView.setImageBitmap(u!!.photo.toBitmap())
           }else{
               binding.imageView.setImageResource(R.drawable.user_profile_1)
           }

            if (role == "normal") {
                listItem =
                    context?.getResources()?.getStringArray(R.array.array_profile) as Array<String>
            } else {
//                Toast.makeText(context, role, Toast.LENGTH_SHORT).show()
                listItem = context?.getResources()
                    ?.getStringArray(R.array.admin_array_profile) as Array<String>
            }
            binding.listview.adapter = listItem?.let { SettingListAdapter(requireActivity(), it) }
            binding.listview.startAnimation(animFadeIn)

        }, 500)

    }

    suspend fun createUser(u: String) = coroutineScope {

        Log.w(ContentValues.TAG, "Create User : " + u)
//        val c = vm.get(u)
        val u = async { vm.get(u) }
        val p = u.await()
        Log.w(ContentValues.TAG, "Create User : " + p.toString())
        dao = LocalDB.getInstance(requireActivity()).LocalDao

        val m = p?.let {
            User_Table(
                it.id,
                it.username,
                it.email,
                it.level,
                it.role,
                it.earn_points,
                it.usable_points,
                it.referral_code,
                it.referred_by
            )
        }
        CoroutineScope(Dispatchers.IO).launch {
            if (m != null) {
                dao.insertUser(m)
                Log.w(ContentValues.TAG, "Create User Not null")
            } else {
                Log.w(ContentValues.TAG, "Create User null")
            }
        }
    }


//    private fun setCurrentFragment(fragment: Fragment)=
//        parentFragmentManager.beginTransaction().apply {
//            replace(R.id.flFragment,fragment)
//            commit()
//        }

}