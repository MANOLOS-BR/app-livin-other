package com.manoloscorp.livinother.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.view.activity.LoginActivity
import com.manoloscorp.livinother.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_personal_register.*


class PersonalRegisterFragment : Fragment(), View.OnClickListener {

    private lateinit var mViewModel: RegisterViewModel

    companion object {
        fun newInstance(): PersonalRegisterFragment {
            return PersonalRegisterFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view: View? = inflater.inflate(R.layout.fragment_personal_register, container, false)

        mViewModel = ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)

        observe()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }
    private fun setListeners() {
        back_arrow.setOnClickListener(this)
        button_next.setOnClickListener(this)
    }


    /**
     * Observa ViewModel
     */
    private fun observe() {
        mViewModel.user.observe(requireActivity(), Observer {
            it
        })
    }

    override fun onClick(view: View) {
        if (view.id == R.id.button_next) {

            val birthday = text_edit_birthday.text.toString()
            val height = text_edit_height.text.toString().toDouble()
            val weight = text_edit_weight.text.toString().toDouble()
            val genre = dropdownGenre

            mViewModel.setPersonalRegister(birthday, height, weight, genre.toString())

            mViewModel.setCurrentFragmentPosition(2)

        }else if (view.id == R.id.back_arrow){
            mViewModel.setCurrentFragmentPosition(0)
        }
    }

}