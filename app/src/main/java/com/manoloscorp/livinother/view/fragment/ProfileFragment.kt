package com.manoloscorp.livinother.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.model.Profile
import com.manoloscorp.livinother.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var mViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        // Cria os observadores
        observe()

        mViewModel.getProfile(1)

        return root
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
        profile.userType
        text_weight.text = profile.medicalHistory.weight.toString()
        text_weight.text = profile.medicalHistory.height.toString()
        checkbox_chemical_addict.isChecked = profile.medicalHistory.drugAddict
        checkbox_alcoholic.isChecked = profile.medicalHistory.alcoholConsumption
        checkbox_communicable_diseases.isChecked = profile.medicalHistory.communicableDisease
        checkbox_degenerative_diseases.isChecked = profile.medicalHistory.degenerativeDisease
        checkbox_practice_physical_activities.isChecked =
            profile.medicalHistory.practicePhysicalActivity
    }
}