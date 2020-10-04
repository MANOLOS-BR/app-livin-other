package com.manoloscorp.livinother.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.view.adapter.RegisterPagerAdapter
import com.manoloscorp.livinother.view.dialogs.CustomProgressDialog
import com.manoloscorp.livinother.view.fragment.BasicRegistrationFragment
import com.manoloscorp.livinother.view.fragment.HealthRegisterFragment
import com.manoloscorp.livinother.view.fragment.PersonalRegisterFragment
import com.manoloscorp.livinother.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity(){

    private lateinit var mViewModel: RegisterViewModel

    private val progressDialog = CustomProgressDialog()

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

        observe()

        setupViewPager()

    }

    private fun initViews() {
        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.viewPager)

    }

    /**
     * Observa ViewModel
     */
    private fun observe() {
        mViewModel.fragmentPosition.observe(this, Observer {
            viewPager.currentItem = it
        })

        mViewModel.dialog.observe(this, Observer {
            if (it == true){
                progressDialog.show(this,"Aguarde...")
            }
        })

        mViewModel.register.observe(this, Observer {
            if (it.status()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, it.errorMessage(), Toast.LENGTH_SHORT).show()
            }
            progressDialog.dialog.dismiss()
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

}