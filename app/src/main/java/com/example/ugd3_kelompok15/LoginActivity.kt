package com.example.ugd3_kelompok15

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    lateinit var mBundle: Bundle

    lateinit var bNama : String
    lateinit var bUsername: String
    lateinit var bPassword: String
    lateinit var bEmail: String
    lateinit var bNoTelp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        getBundle()

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)
        val btnLogin: Button = findViewById(R.id.btn_masuk)
        val btnRegister: Button = findViewById(R.id.btn_daftar)

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if (username.isEmpty()) {
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if (password.isEmpty()) {
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if (username == "admin" && password == "admin") checkLogin = true
            if (username.isEmpty() || password.isEmpty()) {
                return@OnClickListener
            }

            if(!checkLogin) {
                loginAlert()
                return@OnClickListener
            }
            val moveHome = Intent (this@LoginActivity, HomeActivity::class.java)
            startActivity(moveHome)

        })

        btnRegister.setOnClickListener(View.OnClickListener {
            val moveRegister = Intent (this@LoginActivity, RegisterActivity::class.java)
            startActivity(moveRegister)
        })

    }

    fun loginAlert() {
        val builder = AlertDialog.Builder(this)
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        builder.setTitle("Error!")
        builder.setMessage("Maaf, Username dan Password Salah. Tolong Cek Kembali")
        builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
        builder.show()
    }
    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Tolong Konfirmasi")
            setMessage("Apakah anda yakin ingin keluar?")

            setPositiveButton("Iya") { _, _ ->
                super.onBackPressed()
            }

            setNegativeButton("Tidak"){_, _ ->
                Toast.makeText(this@LoginActivity, "Terima Kasih",
                    Toast.LENGTH_LONG).show()
            }

            setCancelable(true)
        }.create().show()
    }

    fun getBundle() {
        if(intent.getBundleExtra("Register") != null) {
            mBundle = intent.getBundleExtra("Register")!!
            bNama = mBundle?.getString("Nama Lengkap") !!
            bUsername = mBundle?.getString("Username")!!
            bEmail = mBundle?.getString("Email")!!
            bNoTelp = mBundle?.getString("Nomor Telepon")!!
            bPassword = mBundle?.getString("Password")!!
        }
    }

}