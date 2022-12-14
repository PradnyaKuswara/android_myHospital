package com.example.ugd3_kelompok15

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.ugd3_kelompok15.HomeActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.ugd3_kelompok15.api.UserProfilApi
import com.example.ugd3_kelompok15.models.UserProfil
import com.example.ugd3_kelompok15.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Exception
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var editUsername : TextInputEditText
    private lateinit var editPassword : TextInputEditText
    private lateinit var textEmail: TextView
    private var queue: RequestQueue? = null
    lateinit var mBundle: Bundle
    private lateinit var auth: FirebaseAuth

    lateinit var bNama : String
    lateinit var bUsername: String
    lateinit var bPassword: String
    lateinit var bEmail: String
    lateinit var bNoTelp: String

    var checkLogin = true


    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val db by lazy { UserDB(this) }
        val userDao = db.userDao()
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        queue = Volley.newRequestQueue(this)
        auth = FirebaseAuth.getInstance()

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)
        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        textEmail = findViewById(R.id.textviewemail)

        val btnLogin: Button = findViewById(R.id.btn_masuk)
        val btnRegister: Button = findViewById(R.id.btn_daftar)

        getBundle()

        btnLogin.setOnClickListener(View.OnClickListener {
            val username: String = inputUsername.getEditText()?.getText().toString().trim()
            val password: String = inputPassword.getEditText()?.getText().toString().trim()
            var checkLogin = true

            if (username.isEmpty()) {
                inputUsername.setError("Username must be filled with email and valid format")
                checkLogin = false
            }

            if (password.isEmpty()) {
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if(!checkLogin) {
//                loginAlert()
                return@OnClickListener
            }else {
//                loginFirebase(username,password)
                LoginUser()
            }


        })

        btnRegister.setOnClickListener(View.OnClickListener {
            val moveRegister = Intent (this@LoginActivity, RegisterActivity::class.java)
            startActivity(moveRegister)
        })

    }

//    fun loginAlert() {
////        val builder = AlertDialog.Builder(this)
////        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
////            Toast.makeText(applicationContext,
////                android.R.string.no, Toast.LENGTH_SHORT).show()
////        }
////        builder.setTitle("Error!")
////        builder.setMessage("Maaf, Username dan Password Salah. Silahkan Cek Kembali")
////        builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
////        builder.show()
////    }
    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Tolong Konfirmasi")
            setMessage("Apakah anda yakin ingin keluar?")

            setPositiveButton("Iya") { _, _ ->
                super.onBackPressed()
            }

            setNegativeButton("Tidak"){_, _ ->
//                Toast.makeText(this@LoginActivity, "Terima Kasih", Toast.LENGTH_LONG).show()
                MotionToast.darkColorToast(this@LoginActivity,"Notification Login!",
                    "Terima Kasih!!",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@LoginActivity, www.sanju.motiontoast.R.font.helvetica_regular))
            }

            setCancelable(true)
        }.create().show()
    }

    private fun loginback4app(email: String, password: String) {
        ParseUser.logInInBackground(email,password) { parseUser: ParseUser?, e: ParseException? ->
            if (parseUser != null) {
                Toast.makeText(this,"Berhasil",Toast.LENGTH_LONG).show()

            } else {
                ParseUser.logOut()
                if (e != null) {
                    Toast.makeText(this,e?.message,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
//                    val verification = auth.currentUser?.isEmailVerified
//                    if(verification == true) {
//                        Log.d(TAG, "signInWithEmail:success")
//                    }else {
//                        Log.d(TAG, "signInWithEmail:success")
//                    }
                }else {
                    Toast.makeText(this, "Error"+ task.getException()?.message, Toast.LENGTH_LONG).show();
                }
            }
    }

    fun getBundle() {
        if (intent.getBundleExtra("Register") != null) {
            mBundle = intent.getBundleExtra("Register")!!
            bNama = mBundle.getString("tietNama")!!
            bUsername = mBundle.getString("tietUsername")!!
            bEmail = mBundle.getString("tietEmail")!!
            bNoTelp = mBundle.getString("tietNomor")!!
            bPassword = mBundle.getString("tietPassword")!!
            editUsername.setText(bUsername)
            editPassword.setText(bPassword)
        }
    }

    private fun LoginUser() {
        //  setLoading(true)

        val userprofil = UserProfil(
            0,
            "",
            inputUsername.getEditText()?.getText().toString(),
            "",
            "",
            inputPassword.getEditText()?.getText().toString()

        )
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, UserProfilApi.LOGIN, Response.Listener { response ->
                val gson = Gson()
                var user = gson.fromJson(response, UserProfil::class.java)

                if(user!=null) {
                    var resJO = JSONObject(response.toString())
                    val  userobj = resJO.getJSONObject("data")

//                    Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                    MotionToast.darkColorToast(this,"Notification Login!",
                        "Login Berhasil!!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    sharedPreferences.edit()
                        .putInt("id",userobj.getInt("id"))
                        .putString("nama",userobj.getString("namaLengkap"))
                        .putString("pass",userobj.getString("password"))
                        .apply()
                    startActivity(intent)
                }else {
                    MotionToast.darkColorToast(this,"Notification Login!",
                        "Login Gagal!!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    return@Listener
                }

            }, Response.ErrorListener { error ->
                // setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        this@LoginActivity,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    MotionToast.darkColorToast(this,"Notification Login!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: Exception) {
//                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkColorToast(this,"Notification Login!",
                        e.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }

            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(userprofil)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }
    fun CharSequence?.isValidEmail() = !
    isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


}