import android.content.ContentValues
import android.content.Context
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
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.SettingListAdapter
import com.mad.mad_bookworms.data.LocalDB
import com.mad.mad_bookworms.data.LocalDao
import com.mad.mad_bookworms.data.User_Table
import com.mad.mad_bookworms.databinding.FragmentProfileBinding
import com.mad.mad_bookworms.security.ForgotPasswordActivity
import com.mad.mad_bookworms.security.LoginActivity
import com.mad.mad_bookworms.viewModels.UserViewModel
import kotlinx.coroutines.*


class ProfileFragment:Fragment() {

    lateinit var dao: LocalDao
    private val vm : UserViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileBinding
    lateinit var listItem: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        //Animation
        val animFadeIn: Animation = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in)
        val animFadeOut: Animation = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out)
        val listView = binding.listview

//        Handler(Looper.getMainLooper()).postDelayed({
//            if
//        },2000)

        val user = Firebase.auth.currentUser

        var role =""
        CoroutineScope(Dispatchers.IO).launch {
            val u = user?.let { vm.get(it.uid) }
            binding.tvName.setText(u?.username)

            role = u?.role.toString()

            Handler(Looper.getMainLooper()).postDelayed({

                if(role == "normal"){
                    listItem = context?.getResources()?.getStringArray(R.array.array_profile) as Array<String>
                }else
                {
                    Toast.makeText(context, role,   Toast.LENGTH_SHORT).show()
                    listItem = context?.getResources()?.getStringArray(R.array.admin_array_profile) as Array<String>
                }
                listView.adapter = listItem?.let { SettingListAdapter(requireActivity(), it) }
                binding.listview.startAnimation(animFadeIn)

            },200)
        }


        binding.listview.setOnItemClickListener{parent, view, position, id ->

            when (listItem?.get(position)) {
                "Change Password" -> changePassword()
                "Purchase History" -> Toast.makeText(context, "Item One",   Toast.LENGTH_SHORT).show()
                "My Favourites" -> Toast.makeText(context, "Item Two",   Toast.LENGTH_SHORT).show()
                "Language" -> Toast.makeText(context, "Item Three",   Toast.LENGTH_SHORT).show()
                "Refer a Friend" -> Toast.makeText(context, "Item Five",   Toast.LENGTH_SHORT).show()
                "Admin Panel" -> Toast.makeText(context, "Item Six",   Toast.LENGTH_SHORT).show()
                "Log Out" -> signOut()
            }

//            if (position==0){
//                Toast.makeText(context, "Item One",   Toast.LENGTH_SHORT).show()
//            }
        }

//                if (user != null) {
//            CoroutineScope(Dispatchers.IO).launch {
//                createUser(user.uid)
//            }
//        }

        return binding.root
    }

    fun changePassword()
    {

    }

    fun signOut()
    {
//        Firebase.auth.signOut()
        Toast.makeText(context, "Signed Out",  Toast.LENGTH_SHORT).show()

        CoroutineScope(Dispatchers.IO).launch {

//            val u = Firebase.auth.currentUser?.let { dao.getUser(it.uid) }
        }
        val userList = dao.getAll()
        Log.w(ContentValues.TAG, "Delete Data : " + userList.toString())
        if(userList!=null)
        {
            Log.w(ContentValues.TAG, "Delete Data")
//                dao.deleteUser(u)
        }else{
            Log.w(ContentValues.TAG, "Not Delete Data")
        }

//        Handler(Looper.getMainLooper()).postDelayed({
//            val intent = Intent(getActivity(), LoginActivity::class.java)
//            startActivity(intent) },2000)
    }

    suspend fun createUser(u: String) = coroutineScope{

        Log.w(ContentValues.TAG, "Create User : "+u)
//        val c = vm.get(u)
        val u = async { vm.get(u) }
        val p = u.await()
        Log.w(ContentValues.TAG, "Create User : " + p.toString())
        dao = LocalDB.getInstance(requireActivity()).LocalDao

        val m = p?.let { User_Table(it.id,it.username,it.email,it.level,it.role,it.earn_points,it.usable_points,it.referral_code,it.referred_by) }
        CoroutineScope(Dispatchers.IO).launch {
            if (m != null) {
                dao.insertUser(m)
                Log.w(ContentValues.TAG, "Create User Not null")
            }else{
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