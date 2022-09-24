package com.example.ugd3_kelompok15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ugd3_kelompok15.databinding.ActivityRegisterBinding
import com.example.ugd3_kelompok15.room.User
import com.example.ugd3_kelompok15.room.UserDB
import com.example.ugd3_kelompok15.ui.profile.FragmentProfile
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var inputNama: TextInputLayout
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputNoTelp: TextInputLayout
    private lateinit var inputPassword: TextInputLayout

    private lateinit var btnRegister : Button
    private lateinit var registerLayout: ConstraintLayout

    val db by lazy { UserDB(this) }
    private var userId: Int = 0

    private lateinit var  binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        supportActionBar?.hide()


        registerLayout = findViewById(R.id.registerLayout)

        binding.btnRegister.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            val intent2 = Intent(this, FragmentProfile::class.java)
            val mBundle = Bundle()

            val nama: String = binding.inputLayoutNama.getEditText()?.getText().toString()
            val username: String = binding.inputLayoutUsername.getEditText()?.getText().toString()
            val email: String = binding.inputLayoutEmail.getEditText()?.getText().toString()
            val noTelp: String = binding.inputLayoutNoTelp.getEditText()?.getText().toString()
            val password: String = binding.inputLayoutPassword.getEditText()?.getText().toString()

            var checkRegister = false

            mBundle.putString("tietNama" , nama)
            mBundle.putString("tietUsername" , username)
            mBundle.putString("tietEmail" , email)
            mBundle.putString("tietNomor" , noTelp)
            mBundle.putString("tietPassword" , password)

            if(nama.isEmpty()){
                binding.inputLayoutNama.setError("Nama must be filled with text")
                checkRegister = false
            }
            if(username.isEmpty()){
                binding.inputLayoutUsername.setError("Username must be filled with text")
                checkRegister = false
            }
            if(email.isEmpty()){
                binding.inputLayoutEmail.setError("Email must be filled with text")
                checkRegister = false
            }
            if(noTelp.isEmpty()){
                binding.inputLayoutNoTelp.setError("No Telp must be filled with text")
                checkRegister = false
            }
            if(password.isEmpty()){
                binding.inputLayoutPassword.setError("Password must be filled with text")
                checkRegister = false
            }

            if(!nama.isEmpty() && !username.isEmpty() && !email.isEmpty() && !noTelp.isEmpty()&& !password.isEmpty()) {
                checkRegister = true
            }

            if(!checkRegister){
                return@OnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                run {
                    db.userDao().addUser(
                        User(0,nama,username,email,noTelp,password)
                    )
                    finish()
                }
            }
            intent.putExtra("Register", mBundle)
            intent.putExtra("intent_id", 0)
            startActivity(intent)
        })
    }
}