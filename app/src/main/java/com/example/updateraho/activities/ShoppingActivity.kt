package com.example.updateraho.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.updateraho.R
import com.example.updateraho.databinding.ActivityShoppingBinding
import com.example.updateraho.fragments.shopping.CartFragment
import com.example.updateraho.fragments.shopping.HomeFragment
import com.example.updateraho.fragments.shopping.ProfileFragment
import com.example.updateraho.fragments.shopping.SearchFragment
import com.example.updateraho.utils.Resource
import com.example.updateraho.viewmodel.CartViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }
    val viewModel by viewModels<CartViewModel> ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)

        // Set up the initial fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment())
            .commit()

        //val b = true
        // Set up navigation item selected listener
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                    R.id.home -> {

                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, HomeFragment())
                            .commit()
                        true
                    }
                        R.id.search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, SearchFragment())
                        .commit()
                    true
                }
                R.id.cart -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CartFragment())
                        .commit()
                    true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ProfileFragment())
                        .commit()
                    true
                }
                else -> false

            }
        }
        //val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val menuItem: MenuItem = bottomNavigation.menu.findItem(R.id.cart)

// Create a BadgeDrawable and set the count
        val badge = bottomNavigation.getOrCreateBadge(menuItem.itemId)

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when(it)
                {
                    is Resource.success ->{
                        val count =  it.data?.size ?: 0
                       // val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                       badge.apply {
                           number = count
                           backgroundColor = ContextCompat.getColor(this@ShoppingActivity, R.color.g_blue)
                          badgeTextColor = ContextCompat.getColor(this@ShoppingActivity, R.color.white)
                           badgeGravity = BadgeDrawable.TOP_END // Set the badge position
                           isVisible = true // Show the badge
                       }
                        Toast.makeText(this@ShoppingActivity, "success case procceed", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
                        Toast.makeText(baseContext,it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading ->{
                        Toast.makeText(baseContext, "Loading", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

    }

    /*override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }*/
}