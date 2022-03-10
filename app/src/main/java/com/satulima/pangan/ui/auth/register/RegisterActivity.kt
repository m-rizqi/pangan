package com.satulima.pangan.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.satulima.pangan.R
import com.satulima.pangan.databinding.ActivityRegisterBinding
import com.satulima.pangan.ui.auth.setup.AccountSetupActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor  = resources.getColor(R.color.yellow_light)
    }

    override fun onStart() {
        super.onStart()
        binding.buttonRegisterWithEmail.setOnClickListener {
            startActivity(Intent(this, AccountSetupActivity::class.java))
        }
    }
}