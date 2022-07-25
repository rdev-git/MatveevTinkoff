package com.matveev.tinkoff.fintex.ui.features.random.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.matveev.tinkoff.fintex.ui.features.top.view.TopGifFragment
import com.matveev.tinkoff.fintex.ui.features.last.view.LastGifFragment
import com.matveev.tinkoff.fintex.ui.features.random.view.RandomGifFragment

class ViewPager2Adapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return RandomGifFragment()
            1 -> return LastGifFragment()
            2 -> return TopGifFragment()
        }
        return RandomGifFragment()
    }

}