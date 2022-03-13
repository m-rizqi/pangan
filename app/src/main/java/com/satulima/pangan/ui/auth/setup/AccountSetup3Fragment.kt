package com.satulima.pangan.ui.auth.setup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.mazenrashed.dotsindicator.DotsIndicator
import com.satulima.pangan.R
import com.satulima.pangan.databinding.FragmentAccountSetup3Binding
import com.satulima.pangan.entity.User

class AccountSetup3Fragment : Fragment() {

    private var _binding : FragmentAccountSetup3Binding? = null
    private val binding get() = _binding!!
    private val args: AccountSetup3FragmentArgs by navArgs()
    private lateinit var newUser : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountSetup3Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        newUser = args.newUser

        activity?.findViewById<DotsIndicator>(R.id.dotIndicatorAccountSetup)?.setDotSelection(if (args.isByGoogle) 1 else 2)

        binding.buttonRlPrev.setOnClickListener {
            val action = AccountSetup3FragmentDirections.setup3ToSetup2(args.isByGoogle, newUser)
            Navigation.findNavController(it).navigate(action)
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    val action = AccountSetup3FragmentDirections.setup3ToSetup2(args.isByGoogle, newUser)
                    Navigation.findNavController(binding.root).navigate(action)
                }
            })

    }
}