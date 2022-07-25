package com.matveev.tinkoff.fintex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.matveev.tinkoff.fintex.databinding.ActivityMainBinding
import com.matveev.tinkoff.fintex.ui.features.random.adapter.ViewPager2Adapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            pager2.adapter = ViewPager2Adapter(this@MainActivity)
            TabLayoutMediator(tabs, pager2) { tab, position ->
                tab.text = when(position){
                    0-> getString(R.string.tabs_random)
                    1-> getString(R.string.tabs_last)
                    2-> getString(R.string.tabs_the_best)
                    else -> error("Unexpected position $position")
                }
            }.attach()
        }
    }
}