package my.com.covid.info.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class BaseTabAdapter constructor(private val fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    var tabTitleArr = ArrayList<String>()
    var tabFragmentArr = ArrayList<Fragment>()

    override fun getCount(): Int = tabFragmentArr.size

    override fun getItem(position: Int): Fragment {
        return tabFragmentArr[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitleArr[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        tabFragmentArr.add(fragment)
        tabTitleArr.add(title)
    }
}