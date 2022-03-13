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

    override fun onStart() {
        super.onStart()

        val newUser = args.newUser
        activity?.findViewById<DotsIndicator>(R.id.dotIndicatorAccountSetup)?.setDotSelection(0)

        binding.buttonNext.setOnClickListener {
            val action = AccountSetup1FragmentDirections.setup1ToSetup2(args.isByGoogle, newUser)
            Navigation.findNavController(it).navigate(action)
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })
    }
}