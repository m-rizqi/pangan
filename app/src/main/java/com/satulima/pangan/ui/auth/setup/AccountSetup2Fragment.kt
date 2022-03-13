package com.satulima.pangan.ui.auth.setup

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.mazenrashed.dotsindicator.DotsIndicator
import com.satulima.pangan.R
import com.satulima.pangan.databinding.FragmentAccountSetup2Binding
import com.satulima.pangan.entity.User
import java.text.SimpleDateFormat
import java.util.*

class AccountSetup2Fragment : Fragment() {

    private var _binding : FragmentAccountSetup2Binding? = null
    private val binding get() = _binding!!
    private val myCalendar: Calendar = Calendar.getInstance()
    private val args: AccountSetup2FragmentArgs by navArgs()
    private lateinit var newUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountSetup2Binding.inflate(inflater, container, false)
        val root: View = binding.root
        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, gender)
        binding.autoCompleteGender.setAdapter(arrayAdapter)
        binding.autoCompleteGender.setDropDownBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.white))
        )

        return root
    }

    override fun onStart() {
        super.onStart()
        newUser = args.newUser

        if (args.isByGoogle){
            // Register by Google
            activity?.findViewById<DotsIndicator>(R.id.dotIndicatorAccountSetup)?.setDotSelection(0)
            binding.buttonNext.visibility = View.VISIBLE
            binding.relativeLayoutPrevNext.visibility = View.GONE

        }else{
            // Register by Email
            activity?.findViewById<DotsIndicator>(R.id.dotIndicatorAccountSetup)?.setDotSelection(1)
            binding.buttonNext.visibility = View.GONE
            binding.relativeLayoutPrevNext.visibility = View.VISIBLE
        }

        binding.editTextBirthday.setOnClickListener {
            DatePickerDialog(
                this.requireContext(),
                provideDateListener(),
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
        binding.buttonNext.setOnClickListener { navigateToNextSetup(it) }
        binding.buttonRlNext.setOnClickListener { navigateToNextSetup(it) }
        binding.buttonRlPrev.setOnClickListener {
            val action = AccountSetup2FragmentDirections.setup2ToSetup1(args.isByGoogle, newUser)
            Navigation.findNavController(it).navigate(action)
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    if (args.isByGoogle){
                        activity?.finish()
                    }else{
                        val action = AccountSetup2FragmentDirections.setup2ToSetup1(args.isByGoogle, newUser)
                        Navigation.findNavController(binding.root).navigate(action)
                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun provideDateListener(): OnDateSetListener {
        val date =
            OnDateSetListener { view, year, month, day ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = month
                myCalendar[Calendar.DAY_OF_MONTH] = day
                updateBirthdayText()
            }
        return date
    }

    private fun updateBirthdayText() {
        val myFormat = "dd/MM/yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.editTextBirthday.setText(dateFormat.format(myCalendar.time))
    }

    private fun navigateToNextSetup(view: View){
        val action = AccountSetup2FragmentDirections.setup2ToSetup3(args.isByGoogle, newUser)
        Navigation.findNavController(view).navigate(action)
    }

}