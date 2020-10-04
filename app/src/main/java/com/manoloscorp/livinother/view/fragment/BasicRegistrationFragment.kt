package com.manoloscorp.livinother.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.view.activity.LoginActivity
import com.manoloscorp.livinother.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_basic_registration.*


class BasicRegistrationFragment : Fragment(), View.OnClickListener {


    private lateinit var mViewModel: RegisterViewModel

    companion object {
        fun newInstance(): BasicRegistrationFragment {
            return BasicRegistrationFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = inflater.inflate(R.layout.fragment_basic_registration, container, false)

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

            val name = text_edit_user.text.toString()
            val email = text_edit_register_email.text.toString()
            val password = text_edit_register_password.text.toString()

            validationFields(name, email, password)

        } else if (view.id == R.id.back_arrow) {
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
    }

    private fun validationFields(name: String, email: String, password: String) {
        if (name != null && name != "") {
            if (email != null && email != "") {
                if (password != null && password != "") {
                    mViewModel.setBasicRegister(name, email, password)
                    mViewModel.setCurrentFragmentPosition(1)
                } else {
                    Toast.makeText(context, "Preencha sua senha", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Preencha seu e-mail", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Preencha um nome para seu usuário", Toast.LENGTH_SHORT)
                .show()
        }
    }
}