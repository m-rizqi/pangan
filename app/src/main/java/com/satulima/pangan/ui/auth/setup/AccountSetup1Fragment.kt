package com.satulima.pangan.ui.auth.setup

import android.os.Build
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.textfield.TextInputEditText
import com.mazenrashed.dotsindicator.DotsIndicator
import com.satulima.pangan.R
import com.satulima.pangan.databinding.FragmentAccountSetup1Binding
import com.satulima.pangan.entity.User
import com.satulima.pangan.service.Status
import com.satulima.pangan.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AccountSetup1Fragment : Fragment() {

    private var _binding : FragmentAccountSetup1Binding? = null
    private val binding get() = _binding!!
    private val args : AccountSetup1FragmentArgs by navArgs()
    private lateinit var newUser: User
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountSetup1Binding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStart() {
        super.onStart()

        newUser = args.newUser
        activity?.findViewById<DotsIndicator>(R.id.dotIndicatorAccountSetup)?.setDotSelection(0)
        if (args.isLogin){
            setForLogin()
        }else{
            setForRegister()
        }

        binding.buttonNext.setOnClickListener {view ->
            if (args.isLogin){
                lifecycleScope.launch {
                    authViewModel.loginWithEmail(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString())
                    authViewModel.loginWithEmailState.collect { state->
                        when(state.status){
                            Status.LOADING -> {
                                binding.progressBarLogin.visibility = View.VISIBLE
                                binding.buttonNext.visibility = View.INVISIBLE
                            }
                            Status.SUCCESS -> {
                                hideProgressBar()
                                state.data?.let {user ->
                                    Toast.makeText(requireContext(), "Login Success : ${user.uid}",Toast.LENGTH_SHORT).show()
                                }
                            }
                            Status.ERROR ->{
                                hideProgressBar()
                                Toast.makeText(requireContext(), "Login Failed : ${state.message}",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }else{
                if (validateEmailPassword()){
                    newUser.email = binding.editTextEmail.text.toString()
                    newUser.password = binding.editTextPassword.text.toString()
                    val action = AccountSetup1FragmentDirections.setup1ToSetup2(args.isByGoogle, newUser, GoogleSignInAccount.createDefault())
                    Navigation.findNavController(view).navigate(action)
                }
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })
    }

    private fun validateEmailPassword() : Boolean{
        var isValid = true

        val emailText = binding.editTextEmail.text.toString()
        val isBadEmail = !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()
        if (isBadEmail){
            isValid = false
            binding.inputLayoutEmail.error = "The input is not an email format"
            binding.inputLayoutEmail.isErrorEnabled = true
            binding.inputLayoutEmail.requestFocus()
        }else{
            binding.inputLayoutEmail.isErrorEnabled = false
            binding.inputLayoutEmail.clearFocus()
        }
        if (binding.editTextPassword.text.isNullOrBlank() || binding.editTextPassword.text!!.length < 8 ){
            isValid = false
            binding.inputLayoutPassword.error = "Minimum password length is 8 characters"
            binding.inputLayoutPassword.isErrorEnabled = true
            binding.inputLayoutEmail.requestFocus()
        }else{
            binding.inputLayoutPassword.isErrorEnabled = false
            binding.inputLayoutEmail.clearFocus()
        }
        if (binding.editTextConfirmPassword.text.toString() != binding.editTextPassword.text.toString()){
            isValid = false
            binding.inputLayoutConfirmPassword.error = "Confirm password does not match password"
            binding.inputLayoutConfirmPassword.isErrorEnabled = true
            binding.inputLayoutEmail.requestFocus()
        }else{
            binding.inputLayoutConfirmPassword.isErrorEnabled = false
            binding.inputLayoutEmail.clearFocus()
        }

        return isValid
    }

    private fun setForLogin(){
        binding.textView1Setup1.setText("Welcome Back!")
        binding.textView2Setup1.setText("Fill the credential below")
        binding.inputLayoutConfirmPassword.visibility = View.INVISIBLE
        binding.textView5Setup1.visibility = View.INVISIBLE
        binding.editTextPassword.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.buttonNext.setText("Login")
    }

    private fun setForRegister(){
        binding.editTextEmail.setText(newUser.email)
        binding.editTextPassword.setText(newUser.password)
        binding.editTextConfirmPassword.setText(newUser.password)
    }

    private fun hideProgressBar(){
        binding.progressBarLogin.visibility = View.INVISIBLE
        binding.buttonNext.visibility = View.VISIBLE
    }
}