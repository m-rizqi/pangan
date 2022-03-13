package com.satulima.pangan.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.satulima.pangan.databinding.ActivityRegisterBinding
import com.satulima.pangan.entity.User
import com.satulima.pangan.ui.auth.setup.AccountSetupActivity
import com.satulima.pangan.utility.WindowUtility
import java.util.*

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
        val newUser = User()
        val intent = Intent(this, AccountSetupActivity::class.java)
        intent.putExtra("newUser", newUser)
        binding.buttonRegisterWithEmail.setOnClickListener {
            intent.putExtra("isByGoogle", false)
            startActivity(intent)
        }

        binding.buttonRegisterWithGoogle.setOnClickListener {
            intent.putExtra("isByGoogle", true)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {

    }
}