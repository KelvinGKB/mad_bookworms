import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mad.mad_bookworms.MyVoucherFragment
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.FragmentRedeemBinding


class RedeemFragment : Fragment() {

//    /// Hide Action Bar
//    override fun onResume() {
//        super.onResume()
//        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
//    }

    private fun setCurrentFragment(fragment: Fragment)=
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    private lateinit var binding: FragmentRedeemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_redeem, container, false)

        binding.copyIdbtn.setOnClickListener(){

            val id :String = binding.copyIdbtn.text.toString()
//            val inputPassword :String = binding.editPassword.text.toString()
//
//            if(inputName == "abc" && inputPassword == "123") {
//
////                Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_questionFragment)
//
//            }else{
//                Toast.makeText(context, "Invalid user name or password!", Toast.LENGTH_LONG).show()
//            }

            Toast.makeText(context, "Referal Code copied : " + id, Toast.LENGTH_LONG).show()
        }

        binding.copyLinkbtn.setOnClickListener(){

            Toast.makeText(context, "Copied", Toast.LENGTH_LONG).show()
        }

        binding.voucherBtn.setOnClickListener(){

            val myVoucherFragment = MyVoucherFragment()
            setCurrentFragment(myVoucherFragment)

        }

        return binding.root
    }



}
