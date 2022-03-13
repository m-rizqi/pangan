package com.satulima.pangan.ui.auth.setup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.satulima.pangan.R
import com.satulima.pangan.databinding.ActivityAccountSetupBinding
import com.satulima.pangan.entity.User
import com.satulima.pangan.utility.WindowUtility

class AccountSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSetupBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        window.statusBarColor  = resources.getColor(R.color.grey_18)

        navController = findNavController(R.id.navHostFragmentAccountSetup)

        WindowUtility.setTransparentStatusBar(window)

        val isByGoogle = intent.getBooleanExtra("isByGoogle", false)
        val newUser = intent.getParcelableExtra<User>("newUser")

        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragmentAccountSetup) as NavHostFragment
        val graph = navHost.navController.navInflater.inflate(R.navigation.accountsetup_nav_graph)

        if(isByGoogle){
            // Register by google account
            binding.dotIndicatorAccountSetup.initDots(2)
            graph.setStartDestination(R.id.accountSetup2Fragment)
        }else{
            // Register by email
            binding.dotIndicatorAccountSetup.initDots(3)
            graph.setStartDestination(R.id.accountSetup1Fragment)
        }
        val args = Bundle()
        args.putBoolean("isByGoogle", isByGoogle)
        args.putParcelable("newUser", newUser)

        navHost.navController.setGraph(graph,args)

    }
}