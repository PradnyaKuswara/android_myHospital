package com.example.ugd3_kelompok15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ugd3_kelompok15.room.UserDB
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

    val db by lazy { UserDB(this)}
    private var userId: Int = 0


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

        btnRegister.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            val mBundle = Bundle()

            val nama: String = inputNama.getEditText()?.getText().toString()
            val username: String = inputUsername.getEditText()?.getText().toString()
            val email: String = inputEmail.getEditText()?.getText().toString()
            val noTelp: String = inputNoTelp.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            var checkRegister = false

            mBundle.putString("tietNama" , nama)
            mBundle.putString("tietUsername" , username)
            mBundle.putString("tietEmail" , email)
            mBundle.putString("tietNomor" , noTelp)
            mBundle.putString("tietPassword" , password)


            if(nama.isEmpty()){
                inputNama.setError("Nama must be filled with text")
                checkRegister = false
            }
            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkRegister = false
            }
            if(email.isEmpty()){
                inputEmail.setError("Email must be filled with text")
                checkRegister = false
            }
            if(noTelp.isEmpty()){
                inputNoTelp.setError("No Telp must be filled with text")
                checkRegister = false
            }
            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkRegister = false
            }

            if(!nama.isEmpty() && !username.isEmpty() && !email.isEmpty() && !noTelp.isEmpty()&& !password.isEmpty()) {
                checkRegister = true
            }

            if(!checkRegister){
                return@OnClickListener
            }

            intent.putExtra("Register", mBundle)
            startActivity(intent)

        })
    }
}