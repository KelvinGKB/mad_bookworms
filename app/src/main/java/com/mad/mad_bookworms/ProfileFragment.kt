import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.SettingListAdapter
import com.mad.mad_bookworms.databinding.FragmentProfileBinding


class ProfileFragment:Fragment() {
    private lateinit var binding: FragmentProfileBinding
    lateinit var listItem: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        val listView = binding.listview
        listView.adapter = SettingListAdapter(requireActivity())

        binding.listview.setOnItemClickListener{parent, view, position, id ->

            if (position==0){
                Toast.makeText(context, "Item One",   Toast.LENGTH_SHORT).show()
            }
            if (position==1){
                Toast.makeText(context, "Item Two",   Toast.LENGTH_SHORT).show()
            }
            if (position==2){
                Toast.makeText(context, "Item Three", Toast.LENGTH_SHORT).show()
            }
            if (position==3){
                Toast.makeText(context, "Item Four",  Toast.LENGTH_SHORT).show()
            }
            if (position==4){
                Toast.makeText(context, "Item Five",  Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }


//    private fun setCurrentFragment(fragment: Fragment)=
//        parentFragmentManager.beginTransaction().apply {
//            replace(R.id.flFragment,fragment)
//            commit()
//        }

}