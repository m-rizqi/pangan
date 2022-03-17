package com.satulima.pangan.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.satulima.pangan.R
import com.satulima.pangan.databinding.ActivityRegisterBinding
import com.satulima.pangan.entity.User
import com.satulima.pangan.service.Status
import com.satulima.pangan.ui.auth.AuthViewModel
import com.satulima.pangan.utility.setTransparentStatusBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val GOOGLE_REQUEST_CODE = 107
    private lateinit var firebaseAuth: FirebaseAuth
    val newUser = User()
    val intentRegister = Intent(".ui.auth.setup.AccountSetupActivity")
    private val authViewModel : AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTransparentStatusBar(window)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        mGoogleSignInClient.signOut()
        binding.buttonRegisterWithEmail.setOnClickListener {
            intentRegister.putExtra("isLogin", false)
            intentRegister.putExtra("newUser", newUser)
            intentRegister.putExtra("isByGoogle", false)
            intentRegister.putExtra("googleAccount", GoogleSignInAccount.createDefault())
            startActivity(intentRegister)
        }

        binding.buttonRegisterWithGoogle.setOnClickListener {
            binding.progressBarRegisterGoogle.visibility = View.VISIBLE
            binding.buttonRegisterWithGoogle.visibility = View.INVISIBLE
            signInGoogle()
        }
        binding.textviewLogin.setOnClickListener {
            intentRegister.putExtra("isLogin", true)
            intentRegister.putExtra("newUser", newUser)
            intentRegister.putExtra("isByGoogle", false)
            intentRegister.putExtra("googleAccount", GoogleSignInAccount.createDefault())
            startActivity(intentRegister)
        }
    }

    override fun onBackPressed() {

    }

    private fun signInGoogle(){
        val signIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signIntent, GOOGLE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_REQUEST_CODE){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null){
                lifecycleScope.launch {
                    account.email?.let {email ->
                        authViewModel.isUserExist(email)
                        authViewModel.isUserExistState.collect { state->
                            when (state.status) {
                                Status.SUCCESS -> Toast.makeText(applicationContext,"User already exist! Automatically login",Toast.LENGTH_SHORT).show()
                                Status.ERROR -> {
                                    newUser.email = account.email.toString()
                                    account.displayName?.let {
                                        if (it.split(" ").size > 1) {
                                            newUser.lastname = it.substring(it.lastIndexOf(" ") + 1)
                                            newUser.firstname = it.substring(0, it.lastIndexOf(" "))
                                        } else {
                                            newUser.firstname = it
                                        }
                                    }
                                    newUser.profilePicture = account.photoUrl.toString()
                                    hideProgressBar()
                                    intentRegister.putExtra("isLogin", false)
                                    intentRegister.putExtra("newUser", newUser)
                                    intentRegister.putExtra("isByGoogle", true)
                                    intentRegister.putExtra("googleAccount", account)
                                    startActivity(intentRegister)
                                }
                            }
                        }
                    }
                }
                }
        }catch (e:ApiException){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
        hideProgressBar()
    }

    private fun hideProgressBar(){
        binding.progressBarRegisterGoogle.visibility = View.INVISIBLE
        binding.buttonRegisterWithGoogle.visibility = View.VISIBLE
    }

}