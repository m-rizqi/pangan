package com.satulima.pangan.ui.auth.setup

import android.os.Build
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.mazenrashed.dotsindicator.DotsIndicator
import com.satulima.pangan.R
import com.satulima.pangan.databinding.FragmentAccountSetup1Binding

class AccountSetup1Fragment : Fragment() {

    private var _binding : FragmentAccountSetup1Binding? = null
    private val binding get() = _binding!!
    private val args : AccountSetup2FragmentArgs by navArgs()

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
        binding.editTextPassword.setText(newUser.password)
        binding.editTextConfirmPassword.setText(newUser.password)

        binding.buttonNext.setOnClickListener {view ->
            if (validateEmailPassword()){
                newUser.email = binding.editTextEmail.text.toString()
                newUser.password = binding.editTextPassword.text.toString()
                val action = AccountSetup1FragmentDirections.setup1ToSetup2(args.isByGoogle, newUser)
                Navigation.findNavController(view).navigate(action)
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
        if (emailText.isNullOrBlank()){
            isValid = false
            binding.inputLayoutEmail.error = if (Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) "The input is not an email format" else "Email can't be empty"
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