package com.manoloscorp.livinother.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_health_register.*


class HealthRegisterFragment : Fragment(), View.OnClickListener {

    private lateinit var mViewModel: RegisterViewModel

    companion object {
        fun newInstance(): HealthRegisterFragment {
            return HealthRegisterFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_health_register, container, false)

        mViewModel = ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)

        setupSpinnerEatingHabit(view)

        observe()

        return view
    }

    private fun onRadioButtonClicked(view: View) {

        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radioButton_donor ->
                    if (checked) {
                        radioButton_donor.setTextColor(resources.getColor(R.color.white, null))
                        radioButton_receiver.setTextColor(
                            resources.getColor(R.color.dark_grey, null)
                        )
                    }
                R.id.radioButton_receiver ->
                    if (checked) {
                        radioButton_donor.setTextColor(resources.getColor(R.color.dark_grey, null))
                        radioButton_receiver.setTextColor(resources.getColor(R.color.white, null))
                    }
            }
        }
    }

    private fun setupSpinnerEatingHabit(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.dropdown_eatingHabit)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.eating_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    /**
     * Observa ViewModel
     */
    private fun observe() {
        mViewModel.user.observe(requireActivity(), Observer {
            it
        })
    }

    private fun setListeners() {
        radioGroup_user_type.setOnClickListener(this)
        radioButton_donor.setOnClickListener(this)
        radioButton_receiver.setOnClickListener(this)
        back_arrow.setOnClickListener(this)
        button_finish.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.back_arrow -> {
                mViewModel.setCurrentFragmentPosition(1)
            }
            R.id.radioButton_donor -> {
                onRadioButtonClicked(view)
            }
            R.id.radioButton_receiver -> {
                onRadioButtonClicked(view)
            }
            R.id.button_finish -> {

                var userType = getUserType()

                val eatingHabit = dropdown_eatingHabit.selectedItem.toString()

                if (eatingHabit != null && eatingHabit != "" && dropdown_eatingHabit.adapter.getItem(
                        0
                    ) != eatingHabit
                ) {
                    val chemicalAddict = checkBox_chemicalAddict.isChecked
                    val alcoholic = checkBox_alcoholic.isChecked
                    val communicableDiseases = checkBox_communicableDiseases.isChecked
                    val degenerativeDiseases = checkBox_degenerativeDiseases.isChecked
                    val practicePhysicalActivities = checkBox_practicePhysicalActivities.isChecked

                    mViewModel.setHealthRegister(
                        userType,
                        eatingHabit,
                        chemicalAddict,
                        alcoholic,
                        communicableDiseases,
                        degenerativeDiseases,
                        practicePhysicalActivities
                    )
                } else {
                    Toast.makeText(context, "Selecione um hábito alimentar", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun getUserType(): String {
        return if (radioButton_donor.isSelected) {
            radioButton_donor.text.toString()
        } else {
            radioButton_receiver.text.toString()
        }
    }


}