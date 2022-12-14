package com.example.ugd3_kelompok15.ui.vaksin

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.api.JanjiTemuApi
import com.example.ugd3_kelompok15.api.VaksinApi
import com.example.ugd3_kelompok15.databinding.ActivityAddUpdateVaksinBinding
import com.example.ugd3_kelompok15.models.JanjiTemuModels
import com.example.ugd3_kelompok15.models.VaksinModels
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class AddUpdateVaksin : AppCompatActivity() {
    companion object{
        private val RS_LIST = arrayOf(
            "Panti Rapih",
            "Bethesda",
            "RSUD",
            "JIH",
            "RS.YAP",
            "Panti Waluyo")
        private val JENIS_VAKSIN = arrayOf(
            "Vaksin Gen 1 Booster",
            "Vaksin Gen 2 Booster",
            "Vaksin Gen 3 Booster",
        )
    }
    private lateinit var binding: ActivityAddUpdateVaksinBinding
    private lateinit var date: String
    private var edRS: AutoCompleteTextView? = null
    private var edJenis: AutoCompleteTextView? = null
    private var viewTanggal: TextView? = null
    private var nama: EditText? = null
    private var umur: EditText? = null

    private var queue: RequestQueue? = null
    private var layoutLoading: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_update_vaksin)
        binding = ActivityAddUpdateVaksinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()


        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        edRS = findViewById(R.id.ed_rsVaksin)
        edJenis = findViewById(R.id.ed_jenisVaksin)
        viewTanggal = findViewById(R.id.viewJadwalVaksin)
        nama = findViewById(R.id.tietNamaPendaftar)
        umur = findViewById(R.id.tietUmur)

        val btnSave = findViewById<Button>(R.id.btnSaveVaksin)
        val btnDate = findViewById<Button>(R.id.layoutJadwalVaksin)

        val id = intent.getIntExtra("id",-1)

        setExposedDropDownMenu()


        val myCalender = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener{view, year, month, dayofMonth ->
            myCalender.set(Calendar.YEAR, year)
            myCalender.set(Calendar.MONTH, month)
            myCalender.set(Calendar.DAY_OF_MONTH, dayofMonth)
            updateLable(myCalender)
        }
        btnDate.setOnClickListener {
            DatePickerDialog(this,datePicker,myCalender.get(Calendar.YEAR), myCalender.get(Calendar.MONTH),myCalender.get(Calendar.DAY_OF_MONTH)).show()
        }

        if(id==-1) {
            btnSave.setOnClickListener {
                createVaksin()
            }
        }else {
            getVaksinById(id)
            btnSave.setOnClickListener {
                updateVaksin(id)
            }
        }


    }

    private fun updateLable(myCalender: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        date = sdf.format(myCalender.time).toString()
        viewTanggal!!.setText(date)
    }

    private fun setExposedDropDownMenu() {
        val adapterrs: ArrayAdapter<String> = ArrayAdapter<String>(this,R.layout.item_list, RS_LIST)
        edRS!!.setAdapter(adapterrs)

        val adapterJenis: ArrayAdapter<String> = ArrayAdapter<String>(this,R.layout.item_list, JENIS_VAKSIN)
        edJenis!!.setAdapter(adapterJenis)
    }

    private fun createVaksin() {
        setLoading(true)

        if(nama!!.text.toString().isEmpty()){
            MotionToast.darkToast(
                this,"Notification Pendaftaran Vaksin!",
                "Input Nama tidak boleh kosong",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        }else if(umur!!.text.toString().isEmpty()) {
            MotionToast.darkToast(
                this,"Notification Pendaftaran Vaksin!",
                "Input Umur tidak boleh kosong",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        }else if(edRS!!.text.toString().isEmpty()) {
            MotionToast.darkToast(
                this,"Notification Pendaftaran Vaksin!",
                "Input Lokasi Vaksin tidak boleh kosong",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        }else if(edJenis!!.text.toString().isEmpty()) {
            MotionToast.darkToast(
                this,"Notification Pendaftaran Vaksin!",
                "Input Jenis Vaksin tidak boleh kosong",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        }else if(viewTanggal!!.text.toString() == "") {
            MotionToast.darkToast(
                this,"Notification Pendaftaran Vaksin!",
                "Input tanggal tidak boleh kosong",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        }else {
            val vaksin = VaksinModels(
                0,
                nama!!.text.toString(),
                umur!!.text.toString().toInt(),
                edRS!!.text.toString(),
                edJenis!!.text.toString(),
                viewTanggal!!.text.toString()
            )

            val stringRequest: StringRequest =
                object :
                    StringRequest(Method.POST, VaksinApi.ADD_URL, Response.Listener { response ->
                        val gson = Gson()
                        var vaksin = gson.fromJson(response, VaksinModels::class.java)

                        if (vaksin != null)
//                    Toast.makeText(this@UpdateJanjiTemu, "Data berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                            MotionToast.darkToast(
                                this, "Notification Pendaftaran Vaksin!",
                                "Data Vaksin Berhasil Ditambahkan",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(
                                    this,
                                    www.sanju.motiontoast.R.font.helvetica_regular
                                )
                            )
                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()

                        setLoading(false)
                    }, Response.ErrorListener { error ->
                        setLoading(false)
                        try {
                            val responseBody =
                                String(error.networkResponse.data, StandardCharsets.UTF_8)
                            val errors = JSONObject(responseBody)
//
                            MotionToast.darkToast(
                                this, "Notification Pendaftaran Vaksin!",
                                errors.getString("message"),
                                MotionToastStyle.INFO,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(
                                    this,
                                    www.sanju.motiontoast.R.font.helvetica_regular
                                )
                            )
                        } catch (e: Exception) {
//                    Toast.makeText(this@UpdateJanjiTemu, e.message, Toast.LENGTH_SHORT).show()
                            MotionToast.darkToast(
                                this, "Notification Pendaftaran Vaksin!",
                                error.message.toString(),
                                MotionToastStyle.INFO,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(
                                    this,
                                    www.sanju.motiontoast.R.font.helvetica_regular
                                )
                            )
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
                        val requestBody = gson.toJson(vaksin)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }
                }
            queue!!.add(stringRequest)
        }
        setLoading(false)
    }

    private fun updateVaksin(id: Int){
        setLoading(true)

        if(nama!!.text.toString().isEmpty()){
            MotionToast.darkToast(
                this,"Notification Pendaftaran Vaksin!",
                "Input Nama tidak boleh kosong",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        }else if(umur!!.text.toString().isEmpty()) {
            MotionToast.darkToast(
                this,"Notification Pendaftaran Vaksin!",
                "Input Umur tidak boleh kosong",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        }else if(edRS!!.text.toString().isEmpty()) {
            MotionToast.darkToast(
                this,"Notification Pendaftaran Vaksin!",
                "Input Lokasi Vaksin tidak boleh kosong",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        }else if(edJenis!!.text.toString().isEmpty()) {
            MotionToast.darkToast(
                this,"Notification Pendaftaran Vaksin!",
                "Input Jenis Vaksin tidak boleh kosong",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        }else if(viewTanggal!!.text.toString() == "") {
            MotionToast.darkToast(
                this,"Notification Pendaftaran Vaksin!",
                "Input tanggal tidak boleh kosong",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        }else {
            val vaksin = VaksinModels(
                id,
                nama!!.text.toString(),
                umur!!.text.toString().toInt(),
                edRS!!.text.toString(),
                edJenis!!.text.toString(),
                viewTanggal!!.text.toString()
            )

            val stringRequest: StringRequest =
                object : StringRequest(Method.PUT, VaksinApi.UPDATE_URL + id, Response.Listener { response ->
                    val gson = Gson()
                    var vaksinModels = gson.fromJson(response, VaksinModels::class.java)

                    if(vaksinModels != null)
//                    Toast.makeText(this@UpdateJanjiTemu, "Data berhasil Diupdate", Toast.LENGTH_SHORT).show()
                        MotionToast.darkToast(
                            this,"Notification Pendaftaran Vaksin!",
                            "Data Vaksin Berhasil Diubah",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()

                    setLoading(false)
                }, Response.ErrorListener { error ->
                    setLoading(false)
                    try {
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        this@UpdateJanjiTemu,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
                        MotionToast.darkToast(
                            this,"Notification Pendaftaran Vaksin!",
                            errors.getString("message"),
                            MotionToastStyle.INFO,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    }catch (e: Exception){
//                    Toast.makeText(this@UpdateJanjiTemu, e.message, Toast.LENGTH_SHORT).show()
                        MotionToast.darkToast(
                            this,"Notification Pendaftaran Vaksin!",
                            error.message.toString(),
                            MotionToastStyle.INFO,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
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
                        val requestBody = gson.toJson(vaksin)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }
                }
            queue!!.add(stringRequest)
        }
    }
    private fun getVaksinById(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, VaksinApi.GET_BY_ID_URL + id, Response.Listener { response ->
//                val gson = Gson()
//                val janjiTemuModels = gson.fromJson(response, JanjiTemuModels::class.java)

                var joVaksin = JSONObject(response.toString())
                val vaksin = joVaksin.getJSONObject("data")

                nama!!.setText(vaksin.getString("nama"))
                umur!!.setText(vaksin.getInt("umur").toString())
                edRS!!.setText(vaksin.getString("lokasi"))
                edJenis!!.setText(vaksin.getString("jenis"))
                viewTanggal!!.setText(vaksin.getString("tanggal"))
                setExposedDropDownMenu()

//                Toast.makeText(this@UpdateJanjiTemu, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                MotionToast.darkToast(
                    this,"Notification Pendaftaran Vaksin!",
                    "Data Vaksin Berhasil Diambil",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        this@UpdateJanjiTemu,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    MotionToast.darkToast(
                        this,"Notification Pendaftaran Vaksin!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: Exception){
//                    Toast.makeText(this@UpdateJanjiTemu, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Pendaftaran Vaksin!",
                        error.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
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


    private fun setLoading(isLoading: Boolean) {
        if(isLoading) {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.VISIBLE
        }else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}