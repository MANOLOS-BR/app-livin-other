package com.manoloscorp.livinother.view.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.utils.DateInputMask
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

        val view = inflater.inflate(R.layout.fragment_personal_register, container, false)

        mViewModel = ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)

        observe()

        setupSpinnerGenre(view)

        return view
    }

    private fun setupSpinnerGenre(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.dropdownGenre)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genre_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        DateInputMask(text_edit_birthday).listen()
    }

    private fun setListeners() {
        back_arrow.setOnClickListener(this)
        button_next.setOnClickListener(this)
    }

    private fun observe() {
        mViewModel.user.observe(requireActivity(), Observer {
            it
        })
    }

    override fun onClick(view: View) {
        if (view.id == R.id.button_next) {

            val birthday = text_edit_birthday.text.toString()
            val height = text_edit_height.text.toString()
            val weight = text_edit_weight.text.toString()
            val genre = dropdownGenre.selectedItem.toString()

            validationFields(birthday, height, weight, genre)

        } else if (view.id == R.id.back_arrow) {
            mViewModel.setCurrentFragmentPosition(0)
        }
    }

    private fun validationFields(birthday: String, height: String, weight: String, genre: String) {
        if (birthday != null && birthday != "") {
            if (height != null && height != "") {
                if (weight != null && weight != "") {
                    if (genre != null && genre != "" && dropdownGenre.adapter.getItem(0) != genre) {
                        val mHeigth = height.toDouble()
                        val mWeight = weight.toDouble()
                        mViewModel.setPersonalRegister(birthday, mHeigth, mWeight, genre)
                        mViewModel.setCurrentFragmentPosition(2)
                    } else {
                        Toast.makeText(context, getString(R.string.validade_genre), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, getString(R.string.validate_weight), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, getString(R.string.validate_height), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, getString(R.string.validate_birthday), Toast.LENGTH_SHORT)
                .show()
        }
    }
}