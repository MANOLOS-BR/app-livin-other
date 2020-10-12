package com.manoloscorp.livinother.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.manoloscorp.livinother.R
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnboardingFragment : Fragment() {

    interface OnBoardingListener {
        fun onNextClick()
    }

    companion object {
        val NAME = "name"

        /**
         * @param name a string to be displayed on the fragment
         * @param listener click listener to pass click events to the activity
         */
//        fun newInstance(name: String, listener: OnBoardingListener): Fragment {
//            val fragment = OnboardingFragment()
//            val bundle = Bundle()
//            bundle.putString(NAME, name)
//            fragment.arguments = bundle
//            fragment.onBoardingListener = listener
//            return fragment
//        }
        fun newInstance(name: String, listener: OnBoardingListener): Fragment {
            val fragment = OnboardingFragment()
            val bundle = Bundle()
            bundle.putString(NAME, name)
            fragment.arguments = bundle
            fragment.onBoardingListener = listener
            return fragment
        }
    }

    private lateinit var onBoardingListener: OnBoardingListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_name.text = arguments?.getString(NAME, "")

        // pass the click event to the activity
        tv_next.setOnClickListener {
            onBoardingListener.onNextClick()
        }
    }

}