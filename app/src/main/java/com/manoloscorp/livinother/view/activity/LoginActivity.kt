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
import kotlinx.android.synthetic.main.fragment_personal_register.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mViewModel: LoginViewModel
    private val progressDialog = CustomProgressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // launch the onboarding screen if it is the first launch of the app
        if (isFirstLaunch()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        }

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Inicializa eventos
        setListeners()
        observe()

        // Verifica se usuário está logado
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

    /**
     * Autentica usuário
     */
    private fun handleLogin() {
        val email = text_edit_email.text.toString()
        val password = text_edit_password.text.toString()

        validationFields(email, password)
    }

    private fun validationFields(email: String, password: String) {
        if (email != null && email != "") {
            if (password != null && password != "") {
                progressDialog.show(this, "Aguarde...")
                mViewModel.doLogin(email, password)
            } else {
                Toast.makeText(applicationContext, "Preencha o campo senha", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(applicationContext, "Preencha o campo e-mail", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Observa ViewModel
     */
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

    /**
     * Inicializa os eventos de click
     */
    private fun setListeners() {
        button_login.setOnClickListener(this)
        text_new_account.setOnClickListener(this)
        text_register.setOnClickListener(this)
    }

    /**
     * Verifica se usuário está logado
     */
    private fun verifyLoggedUser() {
        mViewModel.verifyLoggedUser()
    }
}