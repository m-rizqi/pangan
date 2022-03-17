package com.satulima.pangan.ui.auth.setup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.satulima.pangan.R
import com.satulima.pangan.databinding.ActivityAccountSetupBinding
import com.satulima.pangan.entity.User
import com.satulima.pangan.utility.setTransparentStatusBar


class AccountSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSetupBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.navHostFragmentAccountSetup)

        setTransparentStatusBar(window)

        val isByGoogle = intent.getBooleanExtra("isByGoogle", false)
        val newUser = intent.getParcelableExtra<User>("newUser")
        val googleAccount = intent.getParcelableExtra<GoogleSignInAccount>("googleAccount")
        val isLogin = intent.getBooleanExtra("isLogin", false)

        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragmentAccountSetup) as NavHostFragment
        val graph = navHost.navController.navInflater.inflate(R.navigation.accountsetup_nav_graph)

        val args = Bundle()
        args.putBoolean("isByGoogle", isByGoogle)
        args.putParcelable("newUser", newUser)
        args.putParcelable("googleAccount", googleAccount)

        if(isByGoogle){
            // Register by google account
            binding.dotIndicatorAccountSetup.initDots(2)
            graph.setStartDestination(R.id.accountSetup2Fragment)
        }else{
            // Register by email
            args.putBoolean("isLogin", isLogin)
            binding.dotIndicatorAccountSetup.initDots(3)
            if (isLogin){
                binding.dotIndicatorAccountSetup.initDots(1)
            }
            graph.setStartDestination(R.id.accountSetup1Fragment)
        }


        navHost.navController.setGraph(graph,args)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}