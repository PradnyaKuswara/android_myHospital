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
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ContentInfoCompat
import com.example.ugd3_kelompok15.databinding.ActivityRegisterBinding
import com.example.ugd3_kelompok15.room.User
import com.example.ugd3_kelompok15.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val CHANNEL_ID_1 = "channel_01"
    private val notificationId1 = 101

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

        binding.btnRegister.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
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

            val user = User(0, nama, username, email, noTelp, password)
            userDao.addUser(user)

            sendNotification()
            intent.putExtra("Register", mBundle)
            startActivity(intent)

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
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

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
}