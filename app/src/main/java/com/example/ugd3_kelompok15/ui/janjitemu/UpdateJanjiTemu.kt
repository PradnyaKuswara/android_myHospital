package com.example.ugd3_kelompok15.ui.janjitemu

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok15.NotificationReceiver
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.api.JanjiTemuApi
import com.example.ugd3_kelompok15.databinding.ActivityUpdateJanjiTemuBinding
import com.example.ugd3_kelompok15.models.JanjiTemuModels
import com.example.ugd3_kelompok15.room.JanjiTemuDB
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class UpdateJanjiTemu : AppCompatActivity() {
    companion object{
        private val RS_LIST = arrayOf(
            "FTI",
            "FT",
            "FTB",
            "FBE",
            "FISIP",
            "FH")
        private val DR_LIST = arrayOf(
            "Informatika",
            "Arsitektur",
            "Biologi",
            "Manajemen",
            "Ilmu Komunikasi",
            "Ilmu Hukum"
        )
    }
    private lateinit var binding: ActivityUpdateJanjiTemuBinding
    private lateinit var date: String
    private var edRS: AutoCompleteTextView? = null
    private var edDR: AutoCompleteTextView? = null
    private var viewTanggal: TextView? = null
    private var keluhan: EditText? = null

    private var queue: RequestQueue? = null
    private var layoutLoading: LinearLayout? = null

    private val CHANNEL_ID_2 = "channel_02"
    private val notificationId2 = 102
    private val CHANNEL_ID_3 = "channel_03"
    private val notificationId3 = 103

//    val db by lazy { JanjiTemuDB(this) }
//    private var janjiId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_janji_temu)
        supportActionBar?.hide()
        createChannel()

        queue = Volley.newRequestQueue(this)
        edRS = findViewById(R.id.ed_rs)
        edDR = findViewById(R.id.ed_dr)
        viewTanggal = findViewById(R.id.viewPilihTanggal)
        keluhan = findViewById(R.id.tietkeluhan)
        layoutLoading = findViewById(R.id.layout_loading)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnDate = findViewById<Button>(R.id.layoutdate)
        val id = intent.getIntExtra("id",-1)

        setExposedDropDownMenu()

        val myCalender = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
            myCalender.set(Calendar.YEAR, year)
            myCalender.set(Calendar.MONTH, month)
            myCalender.set(Calendar.DAY_OF_MONTH, dayofMonth)
            updateLable(myCalender)
        }

        btnDate.setOnClickListener {
            DatePickerDialog(this,datePicker,myCalender.get(Calendar.YEAR), myCalender.get(Calendar.MONTH),myCalender.get(Calendar.DAY_OF_MONTH)).show()
        }

        if(id==-1) {
            btnSave.setOnClickListener { createJanjiTemu() }
        }else {
            getJanjiTemuById(id)
            btnSave.setOnClickListener {  }
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
        }
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

    private fun updateLable(myCalender: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        date = sdf.format(myCalender.time).toString()
        viewTanggal!!.setText(date)
    }

    fun setExposedDropDownMenu(){
        val adapterrs: ArrayAdapter<String> = ArrayAdapter<String>(this,
            R.layout.item_list, RS_LIST)
        edRS!!.setAdapter(adapterrs)

        val adapterdr: ArrayAdapter<String> = ArrayAdapter<String>(this,
            R.layout.item_list, DR_LIST
        )
        edDR!!.setAdapter(adapterdr)
    }

    private fun getJanjiTemuById(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, JanjiTemuApi.GET_BY_ID_URL + id, Response.Listener { response ->
//                val gson = Gson()
//                val janjiTemuModels = gson.fromJson(response, JanjiTemuModels::class.java)

                var joJanji = JSONObject(response.toString())
                val janji = joJanji.getJSONObject("data")

                edRS!!.setText(janji.getString("rumahSakit"))
                edDR!!.setText(janji.getString("dokter"))
                viewTanggal!!.setText(janji.getString("tanggal"))
                keluhan!!.setText(janji.getString("keluhan"))
                setExposedDropDownMenu()

                Toast.makeText(this@UpdateJanjiTemu, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@UpdateJanjiTemu,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@UpdateJanjiTemu, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }
    private fun createJanjiTemu(){
        setLoading(true)

        val janji = JanjiTemuModels(
            0,
            edRS!!.text.toString(),
            viewTanggal!!.text.toString(),
            edDR!!.text.toString(),
            keluhan!!.text.toString()
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, JanjiTemuApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var janji = gson.fromJson(response, JanjiTemuModels::class.java)

                if(janji != null)
                    Toast.makeText(this@UpdateJanjiTemu, "Data berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                sendNotification2()
                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@UpdateJanjiTemu,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@UpdateJanjiTemu, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(janji)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }


    private fun setLoading(isLoading: Boolean) {
        if(isLoading) {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.VISIBLE
        }else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }

}