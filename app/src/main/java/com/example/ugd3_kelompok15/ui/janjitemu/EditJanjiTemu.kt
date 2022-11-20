package com.example.ugd3_kelompok15.ui.janjitemu

import android.app.*
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
import com.example.ugd3_kelompok15.NotificationReceiver
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.databinding.ActivityEditJanjiTemuBinding

//import com.example.ugd3_kelompok15.room.Constant
//import com.example.ugd3_kelompok15.room.JanjiTemu
//import com.example.ugd3_kelompok15.room.JanjiTemuDB
//import com.example.ugd3_kelompok15.room.JanjiTemuDao
import kotlinx.android.synthetic.main.activity_edit_janji_temu.*
import java.text.SimpleDateFormat
import java.util.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok15.api.JanjiTemuApi
import com.example.ugd3_kelompok15.models.JanjiTemuModels
import com.example.ugd3_kelompok15.room.JanjiTemuDB
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class EditJanjiTemu : AppCompatActivity()  {
    companion object{
        private val RUMAHSAKIT_LIST = arrayOf(
            "Rumah Sakit Bethesda",
            "RSUD Kota Yogyakarta",
            "Rumah Sakit Ludira Husada Tama",
            "Rumah Sakit Mata Dr.Yap",
            "Rumah Sakit Panti Rapih",
            "Siloam Hospital",
            "Rumah Sakit JIH",
            "Rumah Sakit Bhakti Ibu")
        private val DOKTER_LIST = arrayOf(
            "Dokter Edward",
            "Dokter Michael",
            "Dokter Joel",
            "Dokter Johan",
            "Dokter Mera",
            "Dokter Pamela",
            "Dokter Herlina",
            "Dokter Joana",
            "Dokter Lenny")
    }
    private lateinit var binding: ActivityEditJanjiTemuBinding
//    private lateinit var selectrs: String
//    private lateinit var selectdr: String
//    private lateinit var idRs: String
//    private lateinit var idDr: String
    private lateinit var date: String
    private var textKeluhan: EditText? = null
    private var viewPilihTanggal: TextView? = null
    private var edRumahSakit: AutoCompleteTextView? = null
    private var edDokter: AutoCompleteTextView? = null
    private var queue: RequestQueue? = null
    private var layoutLoading: LinearLayout? = null
    private val CHANNEL_ID_2 = "channel_02"
    private val notificationId2 = 102
    private val CHANNEL_ID_3 = "channel_03"
    private val notificationId3 = 103


    val db by lazy { JanjiTemuDB(this) }
    private var janjiId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_janji_temu)

        queue = Volley.newRequestQueue(this)
        edRumahSakit = findViewById(R.id.ed_rs)
        edDokter = findViewById(R.id.ed_dr)
        textKeluhan = findViewById(R.id.text_keluhan)
        viewPilihTanggal = findViewById(R.id.viewPilihTanggal)
        layoutLoading = findViewById(R.id.layout_loading)

        binding = ActivityEditJanjiTemuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setExposedDropDownMenu()

        supportActionBar?.hide()
        createChannel()

        binding.btnSave.setOnClickListener{
            createJanjiTemu()
        }

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
//        setupView()
//        setupListener()
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

//
    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }

    fun setExposedDropDownMenu(){
        val adapterrs: ArrayAdapter<String> = ArrayAdapter<String>(this,
            R.layout.dropdown_item, RUMAHSAKIT_LIST)
        edRumahSakit!!.setAdapter(adapterrs)

        val adapterdr: ArrayAdapter<String> = ArrayAdapter<String>(this,
            R.layout.dropdown_item, DOKTER_LIST)
        edDokter!!.setAdapter(adapterdr)
    }

//    fun getData() {
//        janjiId = intent.getIntExtra("intent_id", 0)
//        CoroutineScope(Dispatchers.IO).launch {
//            val janjis = db.janjiTemuDao().getJanji(janjiId)[0]
//            binding.viewPilihTanggal.setText(janjis.tanggal)
//            binding.tietkeluhan.setText(janjis.keluhan)
//        }
//    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return super.onSupportNavigateUp()
//    }

    private fun getJanjiTemuById(id: Long){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, JanjiTemuApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val janjiTemuModels = gson.fromJson(response, JanjiTemuModels::class.java)

                edRumahSakit!!.setText(janjiTemuModels.rumahSakit)
                edDokter!!.setText(janjiTemuModels.dokter)
                textKeluhan!!.setText(janjiTemuModels.keluhan)
                viewPilihTanggal!!.setText(janjiTemuModels.tanggal)
              //  setExposedDropDownMenu()

                Toast.makeText(this@EditJanjiTemu, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@EditJanjiTemu,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@EditJanjiTemu, e.message, Toast.LENGTH_SHORT).show()
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
            "a",
            "a",
            "a",
            "a"
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, JanjiTemuApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var janji = gson.fromJson(response, JanjiTemuModels::class.java)

                if(janji != null)
                    Toast.makeText(this@EditJanjiTemu, "Data berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

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
                        this@EditJanjiTemu,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@EditJanjiTemu, e.message, Toast.LENGTH_SHORT).show()
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

    private fun updateJanjiTemu(id: Int){
        setLoading(true)

        val janjiTemuModels = JanjiTemuModels(
            id,
            edRumahSakit!!.text.toString(),
            edDokter!!.text.toString(),
            textKeluhan!!.text.toString(),
            viewPilihTanggal!!.text.toString(),
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.PUT, JanjiTemuApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                var janjiTemuModels = gson.fromJson(response, JanjiTemuModels::class.java)

                if(janjiTemuModels != null)
                    Toast.makeText(this@EditJanjiTemu, "Data berhasil Diupdate", Toast.LENGTH_SHORT).show()

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
                        this@EditJanjiTemu,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@EditJanjiTemu, e.message, Toast.LENGTH_SHORT).show()
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
                    val requestBody = gson.toJson(janjiTemuModels)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }


}
//fun setupView() {
//        val intentType = intent.getIntExtra("intent_type", 0)
//        when (intentType) {
//            Constant.TYPE_CREATE -> {
//                binding.btnUpdate.visibility = View.GONE
//            }
//            Constant.TYPE_READ -> {
//                binding.btnSave.visibility = View.GONE
//                binding.btnUpdate.visibility = View.GONE
//                getData()
//
//            }
//            Constant.TYPE_UPDATE -> {
//                binding.btnSave.visibility = View.GONE
//                getData()
//            }
//        }
//    }
//
//    private fun setupListener() {
//        binding.btnSave.setOnClickListener {
//            if(binding.viewPilihTanggal.text.toString().isEmpty() || binding.tietkeluhan.text.toString().isEmpty() || binding.viewRs.text.toString() == "Please choose hospital!" || binding.viewDr.text.toString() == "Please choose dockter!") {
//                return@setOnClickListener
//            }
//            CoroutineScope(Dispatchers.IO).launch {
//                run {
//                    db.janjiTemuDao().addJanjiTemu(
//                        JanjiTemu(0, idRs.toInt(), idDr.toInt(), selectrs, date, selectdr, binding.tietkeluhan.text.toString())
//                    )
//                    finish()
//
//                    sendNotification2()
//                }
//            }
//        }
//        binding.btnUpdate.setOnClickListener {
//            if(binding.viewPilihTanggal.text.toString().isEmpty() || binding.tietkeluhan.text.toString().isEmpty() || binding.viewRs.text.toString() == "Please choose hospital!" || binding.viewDr.text.toString() == "Please choose dockter!") {
//                return@setOnClickListener
//            }
//            CoroutineScope(Dispatchers.IO).launch {
//                run {
//                    db.janjiTemuDao().updateJanjiTemu(
//                        JanjiTemu(janjiId, idRs.toInt() ,idDr.toInt(), selectrs, binding.viewPilihTanggal.text.toString(), selectdr, binding.tietkeluhan.text.toString())
//                    )
//                    finish()
//                    sendNotification3()
//                }
//            }
//        }
//    }