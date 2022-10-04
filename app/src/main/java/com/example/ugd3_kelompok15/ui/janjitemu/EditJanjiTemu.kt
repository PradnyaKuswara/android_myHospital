package com.example.ugd3_kelompok15.ui.janjitemu

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
import com.example.ugd3_kelompok15.NotificationReceiver
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.databinding.ActivityEditJanjiTemuBinding
import com.example.ugd3_kelompok15.entity.Dokter
import com.example.ugd3_kelompok15.room.Constant
import com.example.ugd3_kelompok15.room.JanjiTemu
import com.example.ugd3_kelompok15.room.JanjiTemuDB
import com.example.ugd3_kelompok15.room.JanjiTemuDao
import kotlinx.android.synthetic.main.activity_edit_janji_temu.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class EditJanjiTemu : AppCompatActivity()  {

    private lateinit var binding: ActivityEditJanjiTemuBinding
    private lateinit var selectrs: String
    private lateinit var selectdr: String
    private lateinit var idRs: String
    private lateinit var idDr: String
    private lateinit var date: String
    private val CHANNEL_ID_2 = "channel_02"
    private val notificationId2 = 102
    private val CHANNEL_ID_3 = "channel_03"
    private val notificationId3 = 103


    val db by lazy { JanjiTemuDB(this) }
    private var janjiId: Int = 0

    override fun onResume() {
        super.onResume()
        val rs = resources.getStringArray(R.array.rumah_sakit)
        val dr = resources.getStringArray(R.array.dokter)
        val arrayAdapterRs = ArrayAdapter(this, R.layout.dropdown_item_rumahsakit, rs)
        val arrayAdapterDr = ArrayAdapter(this, R.layout.dropdown_item_dokter, dr)

        binding.rsOption.setAdapter(arrayAdapterRs)
        binding.drOption.setAdapter(arrayAdapterDr)

        binding.rsOption.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectrs = rs.get(p2).toString()
                idRs = arrayAdapterRs.getPosition(selectrs).toString()
                if(selectrs != "Choose Hospital") {
                    binding.viewRs.setText(selectrs)
                }else {
                    binding.viewRs.setText("Please choose hospital!")
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.drOption.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectdr = dr.get(p2).toString()
                idDr = arrayAdapterDr.getPosition(selectdr).toString()
                if(selectdr != "Choose Doctor") {
                    binding.viewDr.setText(selectdr)
                }else {
                    binding.viewDr.setText("Please choose dockter!")
                }

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_janji_temu)

        binding = ActivityEditJanjiTemuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        createChannel()

        val myCalender = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
            myCalender.set(Calendar.YEAR, year)
            myCalender.set(Calendar.MONTH, month)
            myCalender.set(Calendar.DAY_OF_MONTH, dayofMonth)
            updateLable(myCalender)
        }
        binding.layoutdate.setOnClickListener {
            DatePickerDialog(this, datePicker, myCalender.get(Calendar.YEAR), myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)).show()
        }

        setupView()
        setupListener()
    }

    private fun sendNotification2() {
        val broadcastIntent: Intent = Intent(this, NotificationReceiver:: class.java)
        broadcastIntent.putExtra("toastMessage", "Hi " + findViewById<TextView>(R.id.informasi_dokter))
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_MUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_2)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Janji Temu")
            .setContentText("Janji Temu Berhasil Dibuat")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Diharapkan untuk mendatangi dokter yang sudah dipilih sesuai dengan tanggal yang dipilih!")
                    .setBigContentTitle("Himbauan")
                    .setSummaryText("Summary Text"))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId2, builder.build())
        }
    }

    private fun sendNotification3() {
        val broadcastIntent: Intent = Intent(this, NotificationReceiver:: class.java)
        broadcastIntent.putExtra("toastMessage", "Hi " + findViewById<TextView>(R.id.informasi_dokter))
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_MUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_2)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Janji Temu")
            .setContentText("Janji Temu Berhasil Diupdate")
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("Rumah Sakit telah diubah")
                    .addLine("Dokter telah diubah")
                    .addLine("Tanggal telah diubah")
                    .addLine("Keluhan telah diubah")
                    .setBigContentTitle("Announce"))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId3, builder.build())
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel2 = NotificationChannel(CHANNEL_ID_2, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }
            val channel3 = NotificationChannel(CHANNEL_ID_3, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE)  as NotificationManager

            notificationManager.createNotificationChannel(channel2)
        }    }

    private fun updateLable(myCalender: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        date = sdf.format(myCalender.time).toString()
        binding.viewPilihTanggal.setText(date)
    }

    fun setupView() {
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            Constant.TYPE_CREATE -> {
                binding.btnUpdate.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                binding.btnSave.visibility = View.GONE
                binding.btnUpdate.visibility = View.GONE
                getData()

            }
            Constant.TYPE_UPDATE -> {
                binding.btnSave.visibility = View.GONE
                getData()
            }
        }
    }

    private fun setupListener() {
        binding.btnSave.setOnClickListener {
            if(binding.viewPilihTanggal.text.toString().isEmpty() || binding.tietkeluhan.text.toString().isEmpty() || binding.viewRs.text.toString() == "Please choose hospital!" || binding.viewDr.text.toString() == "Please choose dockter!") {
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                run {
                    db.janjiTemuDao().addJanjiTemu(
                        JanjiTemu(0, idRs.toInt(), idDr.toInt(), selectrs, date, selectdr, binding.tietkeluhan.text.toString())
                    )
                    finish()

                    sendNotification2()
                }
            }
        }
        binding.btnUpdate.setOnClickListener {
            if(binding.viewPilihTanggal.text.toString().isEmpty() || binding.tietkeluhan.text.toString().isEmpty() || binding.viewRs.text.toString() == "Please choose hospital!" || binding.viewDr.text.toString() == "Please choose dockter!") {
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                run {
                    db.janjiTemuDao().updateJanjiTemu(
                        JanjiTemu(janjiId, idRs.toInt() ,idDr.toInt(), selectrs, binding.viewPilihTanggal.text.toString(), selectdr, binding.tietkeluhan.text.toString())
                    )
                    finish()
                    sendNotification3()
                }
            }
        }
    }

    fun getData() {
        janjiId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val janjis = db.janjiTemuDao().getJanji(janjiId)[0]
            binding.viewPilihTanggal.setText(janjis.tanggal)
            binding.tietkeluhan.setText(janjis.keluhan)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}