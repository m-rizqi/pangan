package com.satulima.pangan.ui.auth.setup

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.satulima.pangan.R
import com.satulima.pangan.databinding.ActivityAccountSetupBinding
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

        WindowUtility.setFullScreen(window)
        WindowUtility.lightStatusBar(window, false)
    }
}