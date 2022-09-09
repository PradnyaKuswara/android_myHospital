package com.example.ugd3_kelompok15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private lateinit var inputNama: TextInputLayout
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputNoTelp: TextInputLayout
    private lateinit var inputPassword: TextInputLayout

    private lateinit var tietNama : TextInputEditText
    private lateinit var tietUsername : TextInputEditText
    private lateinit var tietEmail : TextInputEditText
    private lateinit var tietNomor : TextInputEditText
    private lateinit var tietPassword : TextInputEditText

    private lateinit var btnRegister : Button
    private lateinit var registerLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        inputNama = findViewById(R.id.inputLayoutNama)
        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputEmail = findViewById(R.id.inputLayoutEmail)
        inputNoTelp = findViewById(R.id.inputLayoutNoTelp)
        inputPassword = findViewById(R.id.inputLayoutPassword)

        tietNama = findViewById(R.id.tietNama)
        tietUsername = findViewById(R.id.tietUsername)
        tietEmail = findViewById(R.id.tietEmail)
        tietNomor = findViewById(R.id.tietNomor)
        tietPassword = findViewById(R.id.tietPassword)

        registerLayout = findViewById(R.id.registerLayout)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            val mBundle = Bundle()

            val nama: String = inputNama.getEditText()?.getText().toString()
            val username: String = inputUsername.getEditText()?.getText().toString()
            val email: String = inputEmail.getEditText()?.getText().toString()
            val noTelp: String = inputNoTelp.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()
            var checkRegister = false

            mBundle.putString("tietNama" , tietNama.text.toString())
            mBundle.putString("tietUsername" , tietUsername.toString())
            mBundle.putString("tietEmail" , tietEmail.toString())
            mBundle.putString("tietNomor" , tietNomor.toString())
            mBundle.putString("tietPassword" , tietPassword.toString())

            intent.putExtra("Register", mBundle)
            startActivity(intent)

           /* if(tietNama.toString().isEmpty()){
                inputNama.setError("Nama must be filled with text")
                checkRegister = false
            }
            if(tietUsername.toString().isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkRegister = false
            }
            if(tietEmail.toString().isEmpty()){
                inputEmail.setError("Email must be filled with text")
                checkRegister = false
            }
            if(tietNomor.toString().isEmpty()){
                inputNoTelp.setError("No Telp must be filled with text")
                checkRegister = false
            }
            if(tietPassword.toString().isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkRegister = false
            }

            if(!tietNama.toString().isEmpty() && !tietUsername.toString().isEmpty() && !tietEmail.toString().isEmpty() && !tietNomor.toString().isEmpty() && !tietPassword.toString().isEmpty()) {
                checkRegister = true
            }

            if(!checkRegister){
                return@OnClickListener
            } */

        })
    }
}