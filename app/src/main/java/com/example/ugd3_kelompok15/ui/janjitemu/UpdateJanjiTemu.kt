package com.example.ugd3_kelompok15.ui.janjitemu

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok15.NotificationReceiver
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.api.JanjiTemuApi
import com.example.ugd3_kelompok15.databinding.ActivityRegisterBinding
import com.example.ugd3_kelompok15.databinding.ActivityUpdateJanjiTemuBinding
import com.example.ugd3_kelompok15.models.JanjiTemuModels
import com.example.ugd3_kelompok15.room.JanjiTemuDB
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_update_janji_temu.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class UpdateJanjiTemu : AppCompatActivity() {
    companion object{
        private val RS_LIST = arrayOf(
            "Panti Rapih",
            "Bethesda",
            "RSUD",
            "JIH",
            "RS.YAP",
            "Panti Waluyo")
        private val DR_LIST = arrayOf(
            "Dr. Kuswara",
            "Dr. Beni",
            "Dr. Joel",
            "Dr. Devin",
            "Dr. Kevin",
            "Dr. Rakai"
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
        binding = ActivityUpdateJanjiTemuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
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
            btnSave.setOnClickListener {
                val edRS = edRS!!.text.toString()
                val edDR = edDR!!.text.toString()
                val viewTanggal = viewTanggal!!.text.toString()
                val keluhan = keluhan!!.text.toString()
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (edRS.isEmpty() && edDR.isEmpty() && viewTanggal.isEmpty() && keluhan.isEmpty()) {
//                            Toast.makeText(
//                                applicationContext,
//                                "Semuanya Tidak boleh Kosong",
//                                Toast.LENGTH_SHORT
//                            ).show()
                            MotionToast.darkToast(
                                this,"Notification Janji Temu Dokter!",
                                "Inputan tidak boleh kosong",
                                MotionToastStyle.INFO,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                        } else {
                            createJanjiTemu()
                            createPdf(edRS, edDR, viewTanggal, keluhan)
                        }
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }else {
            getJanjiTemuById(id)
            btnSave.setOnClickListener {
                val edRS = edRS!!.text.toString()
                val edDR = edDR!!.text.toString()
                val viewTanggal = viewTanggal!!.text.toString()
                val keluhan = keluhan!!.text.toString()
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (edRS.isEmpty() && edDR.isEmpty() && viewTanggal.isEmpty() && keluhan.isEmpty()) {
//                            Toast.makeText(
//                                applicationContext,
//                                "Semuanya Tidak boleh Kosong",
//                                Toast.LENGTH_SHORT
//                            ).show()
                            MotionToast.darkToast(
                                this,"Notification Janji Temu Dokter!",
                                "Inputan tidak boleh kosong",
                                MotionToastStyle.INFO,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                        } else {
                            updateJanjiTemu(id)
                            createPdf(edRS, edDR, viewTanggal, keluhan)
                        }
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }

    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(
        FileNotFoundException::class
    )
    private fun createPdf(edRS: String, edDR: String, viewTanggal: String, keluhan: String) {
        //ini berguna untuk akses Writing ke Storage HP kalian dalam mode Download.
        //harus diketik jangan COPAS!!!!
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString()
        val file = File(pdfPath, "Rekapan Janji Temu.pdf")
        FileOutputStream(file)

        //inisaliasi pembuatan PDF
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f, 5f, 5f, 5f)
        @SuppressLint("UseCompatLoadingForDrawables") val d = getDrawable(R.drawable.img)

        //penambahan gambar pada Gambar atas
        val bitmap = (d as BitmapDrawable?)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapData = stream.toByteArray()
        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        val namapengguna = Paragraph("Identitas Pengguna").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                        Berikut adalah Data Janji Temu Anda 
                        """.trimIndent()
        ).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

        //proses pembuatan table
        val width = floatArrayOf(100f, 100f)
        val table = Table(width)
        //pengisian table dengan data-data
        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Nama Rumah Sakit")))
        table.addCell(Cell().add(Paragraph(edRS)))
        table.addCell(Cell().add(Paragraph("Nama Dokter")))
        table.addCell(Cell().add(Paragraph(edDR)))
        table.addCell(Cell().add(Paragraph("Tanggal Janji Temu")))
        table.addCell(Cell().add(Paragraph(viewTanggal)))
        table.addCell(Cell().add(Paragraph("Keluhan")))
        table.addCell(Cell().add(Paragraph(keluhan)))

        //pembuatan QR CODE secara generate dengan bantuan IText7
        val barcodeQRCode = BarcodeQRCode(
            """
                                        $edRS
                                        $edDR
                                        $viewTanggal
                                        $keluhan
                                        """.trimIndent()
        )
        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage =
            Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(HorizontalAlignment.CENTER)

        document.add(image)
        document.add(namapengguna)
        document.add(group)
        document.add(table)
        document.add(qrCodeImage)


        document.close()
//        Toast.makeText(this, "Pdf Created", Toast.LENGTH_LONG).show()
        MotionToast.darkToast(
            this,"Notification Janji Temu Dokter!",
            "File Pdf Rekapan Janji Temu Berhasil Dibuat! ",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
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

//                Toast.makeText(this@UpdateJanjiTemu, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                MotionToast.darkToast(
                    this,"Notification Janji Temu Dokter!",
                    "Data Berhasil Diambil",
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
                        this,"Notification Janji Temu Dokter!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: Exception){
//                    Toast.makeText(this@UpdateJanjiTemu, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Janji Temu Dokter!",
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
//                    Toast.makeText(this@UpdateJanjiTemu, "Data berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Janji Temu Dokter!",
                        "Data Janji Temu Berhasil Ditambahkan",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

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
//                    Toast.makeText(
//                        this@UpdateJanjiTemu,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    MotionToast.darkToast(
                        this,"Notification Janji Temu Dokter!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: Exception){
//                    Toast.makeText(this@UpdateJanjiTemu, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Janji Temu Dokter!",
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

    private fun updateJanjiTemu(id: Int){
        setLoading(true)

        val janjiTemuModels = JanjiTemuModels(
            id,
            edRS!!.text.toString(),
            viewTanggal!!.text.toString(),
            edDR!!.text.toString(),
            keluhan!!.text.toString()
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.PUT, JanjiTemuApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                var janjiTemuModels = gson.fromJson(response, JanjiTemuModels::class.java)

                if(janjiTemuModels != null)
//                    Toast.makeText(this@UpdateJanjiTemu, "Data berhasil Diupdate", Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Janji Temu Dokter!",
                        "Data Janji Temu Berhasil Diubah",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

                sendNotification3()
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
                        this,"Notification Janji Temu Dokter!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: Exception){
//                    Toast.makeText(this@UpdateJanjiTemu, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Janji Temu Dokter!",
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