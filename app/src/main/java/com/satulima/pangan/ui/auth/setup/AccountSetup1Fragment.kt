package com.satulima.pangan.ui.auth.setup

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.mazenrashed.dotsindicator.DotsIndicator
import com.satulima.pangan.R
import com.satulima.pangan.databinding.FragmentAccountSetup1Binding
import com.satulima.pangan.service.Status
import com.satulima.pangan.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AccountSetup1Fragment : Fragment() {

    private var _binding : FragmentAccountSetup1Binding? = null
    private val binding get() = _binding!!
    private val args : AccountSetup2FragmentArgs by navArgs()
    private val authViewModel : AuthViewModel by viewModels()

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

        val newUser = args.newUser
        activity?.findViewById<DotsIndicator>(R.id.dotIndicatorAccountSetup)?.setDotSelection(0)
        binding.editTextEmail.setText(newUser.email)
        if (!newUser.email.isNullOrBlank()){
            binding.editTextPassword.setText("password1234")
            binding.editTextConfirmPassword.setText("password1234")
        }

        binding.buttonNext.setOnClickListener {view ->
            if (validateEmailPassword()){
                authViewModel.registerWithEmail(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString())
            }
            lifecycleScope.launch {
                authViewModel.registerWithEmailState.collect {
                    when(it.status){
                        Status.LOADING -> {
                            binding.progressBarRegisterEmail.visibility = View.VISIBLE
                            binding.buttonNext.visibility = View.INVISIBLE
                        }
                        Status.SUCCESS -> {
                            binding.progressBarRegisterEmail.visibility = View.INVISIBLE
                            binding.buttonNext.visibility = View.VISIBLE
                            it.data?.let{data ->
                                newUser.email = data.email.toString()
                                newUser.firstName = data.displayName.toString()
                                newUser.lastName = ""
                            }
                            val action = AccountSetup1FragmentDirections.setup1ToSetup2(args.isByGoogle, newUser)
                            Navigation.findNavController(view).navigate(action)
                        }
                        else -> {
                            binding.buttonNext.visibility = View.VISIBLE
                            it.message?.let {msg ->
                                Toast.makeText(
                                    context,
                                    "Failed: $msg",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
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

        if (binding.editTextEmail.text.isNullOrBlank()){
            isValid = false
            binding.inputLayoutEmail.error = "Email can't be empty"
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
}