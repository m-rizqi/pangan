package com.satulima.pangan.ui.auth.setup

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.satulima.pangan.R
import com.satulima.pangan.databinding.FragmentAccountSetup2Binding
import java.text.SimpleDateFormat
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AccountSetup2Fragment : Fragment() {

    private var _binding : FragmentAccountSetup2Binding? = null
    private val binding get() = _binding!!
    val myCalendar: Calendar = Calendar.getInstance()

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        binding.editTextBirthday.setOnClickListener {
            DatePickerDialog(
                this.requireContext(),
                provideDateListener(),
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
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

}