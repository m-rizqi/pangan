package com.satulima.pangan.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.satulima.pangan.databinding.ActivityRegisterBinding
import com.satulima.pangan.ui.auth.setup.AccountSetupActivity
import com.satulima.pangan.utility.WindowUtility

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowUtility.setTransparentStatusBar(window)
    }

    override fun onStart() {
        super.onStart()
        binding.buttonRegisterWithEmail.setOnClickListener {
            startActivity(Intent(this, AccountSetupActivity::class.java))
        }
    }

    override fun onBackPressed() {

    }
}