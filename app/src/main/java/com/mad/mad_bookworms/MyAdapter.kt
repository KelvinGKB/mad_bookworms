package com.mad.mad_bookworms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mad.mad_bookworms.profile.HistoryFragment
import com.mad.mad_bookworms.profile.InvitedUserFragment
import com.mad.mad_bookworms.profile.ReferralFragment
import com.mad.mad_bookworms.redeem.ActiveVoucherFragment
import com.mad.mad_bookworms.redeem.BrowseVoucherFragment
import com.mad.mad_bookworms.redeem.MyVoucherFragment

@Suppress("DEPRECATION")
internal class MyAdapter(
    var context: MyVoucherFragment,
    fm: FragmentManager,
    var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ActiveVoucherFragment()
            }
            1 -> {
                BrowseVoucherFragment()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }

}

@Suppress("DEPRECATION")
internal class ReferralAdapter(
    var context: ReferralFragment,
    fm: FragmentManager,
    var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                InvitedUserFragment()
            }
            1 -> {
                HistoryFragment()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }

}

class CustomAdapter(
    applicationContext: Context?,
    countryList: Array<String>,
    flags: IntArray
) :
    BaseAdapter() {
    lateinit var context: Context
    var countryList: Array<String>
    var flags: IntArray
    var inflter: LayoutInflater
    override fun getCount(): Int {
        return countryList.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(
        i: Int,
        view: View,
        viewGroup: ViewGroup
    ): View {
        var view = view
        view = inflter.inflate(R.layout.setting_list, null)
        val country = view.findViewById<View>(R.id.tv) as TextView
        val icon =
            view.findViewById<View>(R.id.icon) as ImageView
        country.text = countryList[i]
        icon.setImageResource(flags[i])
        return view
    }

    init {
        this.countryList = countryList
        this.flags = flags
        inflter = LayoutInflater.from(applicationContext)
    }
}

class SettingListAdapter(context: Context,listItem:Array<String>):BaseAdapter(){

    private val mContext :Context

    val listItem = listItem

    init {
        mContext =context
    }

    override fun getCount(): Int {
        return listItem.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return "TEST STRING"
    }

    override fun getView(position: Int,convertView: View?,viewGroup: ViewGroup?): View{
        val layoutInflater = LayoutInflater.from(mContext)
        val row_main = layoutInflater.inflate(R.layout.setting_list,viewGroup,false)

        val positionTextView = row_main.findViewById<TextView>(R.id.tv)
        positionTextView.text = listItem.get(position)
        return row_main
    }
}
