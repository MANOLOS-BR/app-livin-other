package com.manoloscorp.livinother.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        var view: View? = inflater.inflate(R.layout.fragment_health_register, container, false)

        mViewModel = ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)

        observe()
        return view
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
        back_arrow.setOnClickListener(this)
        button_next.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        if (view.id == R.id.button_next) {

//            mViewModel.setPersonalRegister(birthday, height, weight, genre.toString())

        }else if (view.id == R.id.back_arrow){
            mViewModel.setCurrentFragmentPosition(1)
        }
    }


}