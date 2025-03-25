package com.example.framereality.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.framereality.MyUtils
import com.example.framereality.R
import com.example.framereality.assets.Helper
import com.example.framereality.databinding.ActivityMainBinding
import com.example.framereality.fragment.HomeFragment
import com.example.framereality.fragment.ItemsFragment
import com.example.framereality.fragments.FavouriteListFragment
import com.example.framereality.fragments.GiftCartFragment
import com.example.framereality.fragments.OtherServices
import com.example.framereality.fragments.ProfileFragment
import com.example.framereality.fragments.RentalListFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Helper.loadGifts(this)
        firebaseAuth = FirebaseAuth.getInstance()
        //Check if user is logged in
        if(firebaseAuth.currentUser == null){
            startLoginOptionsActivity()
        }

        showHomeFragment()

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val itemId = menuItem.itemId

            when(itemId) {
                R.id.item_home -> {
                    showHomeFragment()
                    return@setOnItemSelectedListener true
                }

                R.id.item_rental -> {
                    if (firebaseAuth.currentUser == null) {
                        MyUtils.toast(this, "Login Required...")
                        return@setOnItemSelectedListener false
                    } else {
                        showChatsListFragment()
                        return@setOnItemSelectedListener true
                    }
                }

                R.id.item_shortlist -> {

                    if (firebaseAuth.currentUser == null) {
                        MyUtils.toast(this, "Login Required...")
                        return@setOnItemSelectedListener false
                    } else {
                        showOtherServicesFragment()
                        return@setOnItemSelectedListener true
                    }
                }

                R.id.gift -> {

                    if (firebaseAuth.currentUser == null) {
                        MyUtils.toast(this, "Login Required...")
                        return@setOnItemSelectedListener false
                    } else {
                        showGiftsFragment()
                        return@setOnItemSelectedListener true
                    }
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
        binding.whatsapp.setOnClickListener{
            openWhatsapp("+919031036321","Hello")
        }
        binding.profile.setOnClickListener{
            showProfileFragment()
        }
        binding.shortList.setOnClickListener{
            showFavouriteListFragment()
        }
        binding.shortList2.setOnClickListener{
            showGiftCartFragment()
        }
    }

    private fun showGiftCartFragment() {
        binding.toolbarTitleTv.text = "Gifts Cart"
        val ItemsFragment = GiftCartFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id,ItemsFragment,"Gift")
        fragmentTransaction.commit()
    }

    private fun openWhatsapp(phone: String, text: String) {
        try {
            val url = "https://api.whatsapp.com/send?phone=$phone&text=${Uri.encode(text)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent) // Works with WhatsApp or WhatsApp Web if not installed
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error opening WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showHomeFragment(){
        binding.toolbarTitleTv.text = "Buy"
        val homeFragment = HomeFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id,homeFragment,"Buy")
        fragmentTransaction.commit()
    }

    private fun showChatsListFragment(){
        binding.toolbarTitleTv.text = "Rentals"
        val chatsListFragment = RentalListFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id,chatsListFragment,"Rentals")
        fragmentTransaction.commit()
    }

    private fun showFavouriteListFragment(){
        binding.toolbarTitleTv.text = "Shortlist"
        val favouriteListFragment = FavouriteListFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id,favouriteListFragment,"Shortlist")
        fragmentTransaction.commit()
    }

    private fun showProfileFragment(){
        binding.toolbarTitleTv.text = "Profile"
        val profileFragment = ProfileFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id,profileFragment,"Profile")
        fragmentTransaction.commit()
    }
    private fun showOtherServicesFragment(){
        binding.toolbarTitleTv.text = "Other Services"
        val otherServicesFragment = OtherServices()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id,otherServicesFragment,"Other Services")
        fragmentTransaction.commit()
    }
    private fun showGiftsFragment(){
        binding.toolbarTitleTv.text = "Gifts"
        val ItemsFragment = ItemsFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id,ItemsFragment,"Gift")
        fragmentTransaction.commit()
    }

    private fun startLoginOptionsActivity(){
        startActivity(Intent(this,LoginOptionsActivity::class.java))
    }
}