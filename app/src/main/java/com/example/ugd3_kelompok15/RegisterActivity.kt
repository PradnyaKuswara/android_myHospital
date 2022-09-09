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

    private lateinit var inputNama: TextInputEditText
    private lateinit var inputUsername: TextInputEditText
    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputNoTelp: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var btnRegister : Button
    private lateinit var registerLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setTitle("User Register")
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

            mBundle.putString("Nama Lengkap" , inputNama.text.toString())
            mBundle.putString("Username" , inputUsername.text.toString())
            mBundle.putString("Email" , inputEmail.text.toString())
            mBundle.putString("Nomor Telepon" , inputNoTelp.text.toString())
            mBundle.putString("Password" , inputPassword.text.toString())

            intent.putExtra("Register", mBundle)
            startActivity(intent)
        }
    }
}