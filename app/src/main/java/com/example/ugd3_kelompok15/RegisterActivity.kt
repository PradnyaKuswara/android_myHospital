package com.example.ugd3_kelompok15

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok15.api.UserProfilApi
import com.example.ugd3_kelompok15.databinding.ActivityRegisterBinding
import com.example.ugd3_kelompok15.models.UserProfil
import com.example.ugd3_kelompok15.room.UserDB
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Exception
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val CHANNEL_ID_1 = "channel_01"
    private val notificationId1 = 101
    private var queue: RequestQueue? = null
    private var checkRegister = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val db by lazy { UserDB(this) }
        val userDao = db.userDao()


        supportActionBar?.hide()

        createChannel()

        var inputNama = binding.inputLayoutNama
        var inputUsername = binding.inputLayoutUsername
        var inputEmail = binding.inputLayoutEmail
        var inputNoTelp = binding.inputLayoutNoTelp
        var inputPassword = binding.inputLayoutPassword

        queue = Volley.newRequestQueue(this)

        binding.btnRegister.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            val mBundle = Bundle()

            val nama: String = binding.inputLayoutNama.getEditText()?.getText().toString()
            val username: String = binding.inputLayoutUsername.getEditText()?.getText().toString()
            val email: String = binding.inputLayoutEmail.getEditText()?.getText().toString()
            val noTelp: String = binding.inputLayoutNoTelp.getEditText()?.getText().toString()
            val password: String = binding.inputLayoutPassword.getEditText()?.getText().toString()

          //  var checkRegister = false

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
            }else {
                //Create User Profil with Volley
                createUser(mBundle)
            }

            //create user with room
//            val user = User(0, nama, username, email, noTelp, password)
//            userDao.addUser(user)
        })
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE)  as NotificationManager

            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotification() {

        val broadcastIntent: Intent = Intent(this, NotificationReceiver:: class.java)
        broadcastIntent.putExtra("toastMessage", "Hi " + binding.inputLayoutNama.getEditText()?.getText().toString() + " :)")
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_MUTABLE)

        val image = BitmapFactory.decodeResource(resources, R.drawable.gambar_rs)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Notification Register")
            .setContentText("Selamat Datang di My Hospital")
            .setLargeIcon(image)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigLargeIcon(null)
                .bigPicture(image))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .addAction(R.mipmap.ic_launcher, "From Us", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId1, builder.build())
        }
    }
    private fun createUser(mBundle: Bundle) {
      //  setLoading(true)

        val userprofil = UserProfil(
            0,
            binding.inputLayoutNama.getEditText()?.getText().toString(),
            binding.inputLayoutUsername.getEditText()?.getText().toString(),
            binding.inputLayoutEmail.getEditText()?.getText().toString(),
            binding.inputLayoutNoTelp.getEditText()?.getText().toString(),
            binding.inputLayoutPassword.getEditText()?.getText().toString()
        )
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, UserProfilApi.REGISTER, Response.Listener { response ->
                val gson = Gson()
                var user = gson.fromJson(response, UserProfil::class.java)

                if(user != null) {
//                    Toast.makeText(this@RegisterActivity, "User Berhasil Register", Toast.LENGTH_SHORT).show()
                    MotionToast.darkColorToast(this,"Notification Register!",
                        "Register Berhasil!!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    sendNotification()
                    intent.putExtra("Register", mBundle)
                    startActivity(intent)
                } else {
                    checkRegister = false
                }
            }, Response.ErrorListener { error ->
               // setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        this@RegisterActivity,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    MotionToast.darkColorToast(this,"Notification Register!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    checkRegister = false
                }catch (e: Exception) {
//                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkColorToast(this,"Notification Register!",
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
}