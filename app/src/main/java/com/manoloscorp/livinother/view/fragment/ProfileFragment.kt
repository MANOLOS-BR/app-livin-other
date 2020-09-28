package com.manoloscorp.livinother.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.model.Profile
import com.manoloscorp.livinother.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var mViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        observe()

        mViewModel.getProfile(2)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
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
                        radioButton_receiver.setTextColor(resources.getColor(R.color.dark_grey,null)
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

    private fun observe() {
        mViewModel.profile.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setProfileValues(it)
            }
        })

        mViewModel.validation.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it.errorMessage(), Toast.LENGTH_SHORT).show()
        })
    }

    private fun setProfileValues(profile: Profile) {
        text_user_email.text = profile.name
        text_name.text = profile.name
        text_email.text = profile.email
        text_date_birthday.text = profile.dataNascimento
        text_genre.text = profile.genre
        setProfileType(profile.userType)
        text_weight.text = profile.medicalHistory.weight.toString()
        text_height.text = profile.medicalHistory.height.toString()
        checkbox_chemical_addict.isChecked = profile.medicalHistory.drugAddict
        checkbox_alcoholic.isChecked = profile.medicalHistory.alcoholConsumption
        checkbox_communicable_diseases.isChecked = profile.medicalHistory.communicableDisease
        checkbox_degenerative_diseases.isChecked = profile.medicalHistory.degenerativeDisease
        checkbox_practice_physical_activities.isChecked = profile.medicalHistory.practicePhysicalActivity
    }

    /**
     * Inicializa os eventos de click
     */
    private fun setListeners() {
        radioGroup_user_type.setOnClickListener(this)
        radioButton_donor.setOnClickListener(this)
        radioButton_receiver.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
    }

    private fun setProfileType(value: String){
        if (value == "DOADOR"){
            radioButton_donor.isChecked = true
            onRadioButtonClicked(radioButton_donor)
        }else{
            radioButton_receiver.isChecked = true
            onRadioButtonClicked(radioButton_receiver)
        }

    }


    override fun onClick(view: View) {
        if (view.id == R.id.radioButton_donor) {
            onRadioButtonClicked(view)
        } else if (view.id == R.id.radioButton_receiver) {
            onRadioButtonClicked(view)
        }
    }
}