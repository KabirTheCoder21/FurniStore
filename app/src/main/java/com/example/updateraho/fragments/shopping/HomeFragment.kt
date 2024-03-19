package com.example.updateraho.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.updateraho.R
import com.example.updateraho.activities.ShoppingActivity
import com.example.updateraho.adapters.HomeViewPagerAdapter
import com.example.updateraho.databinding.ActivityShoppingBinding
import com.example.updateraho.databinding.FragmentHomeBinding
import com.example.updateraho.fragments.categories.AccessoryFragment
import com.example.updateraho.fragments.categories.ChairFragment
import com.example.updateraho.fragments.categories.CupboardFragment
import com.example.updateraho.fragments.categories.FurnitureFragment
import com.example.updateraho.fragments.categories.MainCategory
import com.example.updateraho.fragments.categories.TableFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home){
    private lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoriesFragment = arrayListOf(
            MainCategory(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )
        binding.viewPaggerHome.isUserInputEnabled = false
        val viewPager2Adapter = HomeViewPagerAdapter(categoriesFragment,childFragmentManager,lifecycle)
        binding.viewPaggerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewPaggerHome){
            tab,pos->
            when(pos)
            {
                0-> tab.text = "Home"
                1-> tab.text = "Chair"
                2-> tab.text = "Cupboard"
                3-> tab.text = "Accessory"
                4-> tab.text = "Furniture"
                5-> tab.text = "Table"
            }

        }.attach()
    }
}