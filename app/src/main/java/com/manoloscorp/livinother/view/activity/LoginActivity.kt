package com.manoloscorp.livinother.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.repository.local.SecurityPreferences
import com.manoloscorp.livinother.view.dialogs.CustomProgressDialog
import com.manoloscorp.livinother.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mViewModel: LoginViewModel
    private val progressDialog = CustomProgressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (isFirstLaunch()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        }

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setListeners()
        observe()

        verifyLoggedUser()

    }

    private fun isFirstLaunch(): Boolean {
        return SecurityPreferences(applicationContext).isFirstLaunch()
    }

    override fun onClick(view: View) {
        if (view.id == R.id.button_login) {
            handleLogin()
        } else if (view.id == R.id.text_new_account || view.id == R.id.text_register) {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun handleLogin() {
        val email = text_edit_email.text.toString()
        val password = text_edit_password.text.toString()

        validationFields(email, password)
    }

    private fun validationFields(email: String, password: String) {
        if (email != null && email != "") {
            if (password != null && password != "") {
                progressDialog.show(this, getString(R.string.dialog_waiting))
                mViewModel.doLogin(email, password)
            } else {
                Toast.makeText(applicationContext, getString(R.string.validate_password), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(applicationContext, getString(R.string.validate_email), Toast.LENGTH_SHORT).show()
        }
    }

    private fun observe() {
        mViewModel.login.observe(this, Observer {
            if (it.status()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, it.errorMessage(), Toast.LENGTH_SHORT).show()
            }
            progressDialog.dialog.dismiss()
        })

        mViewModel.loggedUser.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }

    private fun setListeners() {
        button_login.setOnClickListener(this)
        text_new_account.setOnClickListener(this)
        text_register.setOnClickListener(this)
    }

    private fun verifyLoggedUser() {
        mViewModel.verifyLoggedUser()
    }
}