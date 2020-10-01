package com.manoloscorp.livinother.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.view.adapter.RegisterPagerAdapter
import com.manoloscorp.livinother.view.fragment.BasicRegistrationFragment
import com.manoloscorp.livinother.view.fragment.HealthRegisterFragment
import com.manoloscorp.livinother.view.fragment.PersonalRegisterFragment
import com.manoloscorp.livinother.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var mViewModel: RegisterViewModel

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: RegisterPagerAdapter

    private lateinit var basicRegistrationFragment: BasicRegistrationFragment
    private lateinit var personalRegisterFragment: PersonalRegisterFragment
    private lateinit var healthRegisterFragment: HealthRegisterFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        initViews()

        setListeners()
        observe()

        setupViewPager()

    }

    private fun initViews() {
        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.viewPager)

    }

    /**
     * Inicializa os eventos de click
     */
    private fun setListeners() {
    }

    /**
     * Observa ViewModel
     */
    private fun observe() {
        mViewModel.fragmentPosition.observe(this, Observer {
             viewPager.currentItem = it
        })
    }

    private fun setupViewPager() {
        viewPager.isUserInputEnabled = false

        adapter = RegisterPagerAdapter(this)

        basicRegistrationFragment = BasicRegistrationFragment.newInstance()
        personalRegisterFragment = PersonalRegisterFragment.newInstance()
        healthRegisterFragment = HealthRegisterFragment.newInstance()

        adapter.addFragment(basicRegistrationFragment)
        adapter.addFragment(personalRegisterFragment)
        adapter.addFragment(healthRegisterFragment)

        viewPager.adapter = adapter
    }

    override fun onClick(view: View) {

    }


}