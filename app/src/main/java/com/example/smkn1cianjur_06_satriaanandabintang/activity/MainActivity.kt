package com.example.smkn1cianjur_06_satriaanandabintang.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smkn1cianjur_06_satriaanandabintang.R
import com.example.smkn1cianjur_06_satriaanandabintang.activity.fragment.ProductFragment
import com.example.smkn1cianjur_06_satriaanandabintang.activity.fragment.ProfileFragment
import com.example.smkn1cianjur_06_satriaanandabintang.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        switchFragment(ProductFragment())

        binding.mainBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main_product -> switchFragment(ProductFragment())
                R.id.main_profile -> switchFragment(ProfileFragment())
            }
            true
        }
    }

    private fun switchFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.mainFrameLayout.id, fragment)
        fragmentTransaction.commit()
    }
}