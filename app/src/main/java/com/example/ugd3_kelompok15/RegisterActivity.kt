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
        registerLayout = findViewById(R.id.registerLayout)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            val mBundle = Bundle()

            mBundle.putString("Nama Lengkap" , inputNama.getEditText()?.getText().toString())
            mBundle.putString("Username" , inputNama.getEditText()?.getText().toString())
            mBundle.putString("Email" , inputNama.getEditText()?.getText().toString())
            mBundle.putString("Nomor Telepon" , inputNama.getEditText()?.getText().toString())
            mBundle.putString("Password" , inputNama.getEditText()?.getText().toString())

            intent.putExtra("Register", mBundle)
            startActivity(intent)
        }
    }
}