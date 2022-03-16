package com.satulima.pangan.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.satulima.pangan.R
import com.satulima.pangan.databinding.ActivityRegisterBinding
import com.satulima.pangan.entity.User
import com.satulima.pangan.ui.auth.setup.AccountSetupActivity
import com.satulima.pangan.utility.WindowUtility
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val GOOGLE_REQUEST_CODE = 107
    private lateinit var firebaseAuth: FirebaseAuth
    val newUser = User()
    val intentRegister = Intent(".ui.auth.setup.AccountSetupActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowUtility.setTransparentStatusBar(window)

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
                newUser.email = account.email.toString()
                account.displayName?.let {
                    if (it.split(" ").size > 1){
                        newUser.lastName = it.substring(it.lastIndexOf(" ")+1)
                        newUser.firstName = it.substring(0, it.lastIndexOf(" "))
                    }else{
                        newUser.firstName = it
                    }
                }
                newUser.profilePicture = account.photoUrl.toString()
                binding.progressBarRegisterGoogle.visibility = View.INVISIBLE
                binding.buttonRegisterWithGoogle.visibility = View.VISIBLE
                intentRegister.putExtra("newUser", newUser)
                intentRegister.putExtra("isByGoogle", true)
                intentRegister.putExtra("googleAccount", account)
                startActivity(intentRegister)
                }
        }catch (e:ApiException){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}