package com.example.ugd3_kelompok15.ui.janjitemu

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
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

class EditJanjiTemu : AppCompatActivity()  {

    private lateinit var binding: ActivityEditJanjiTemuBinding
    private lateinit var selectrs: String
    private lateinit var selectdr: String
    private lateinit var date: String


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
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.drOption.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectdr = dr.get(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_edit_janji_temu)
        binding = ActivityEditJanjiTemuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

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

    private fun updateLable(myCalender: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        date = sdf.format(myCalender.time).toString()
    }

    fun setupView() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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
            CoroutineScope(Dispatchers.IO).launch {
                run {
                    db.janjiTemuDao().addJanjiTemu(
                        JanjiTemu(0, selectrs, date, selectdr, binding.tietkeluhan.text.toString())
                    )
                    finish()
                }
            }
        }
        binding.btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                run {
                    db.janjiTemuDao().updateJanjiTemu(
                        JanjiTemu(janjiId, selectrs, date, selectdr, binding.tietkeluhan.text.toString())
                    )
                    finish()
                }
            }
        }
    }

    fun getData() {
        janjiId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val janjis = db.janjiTemuDao().getJanji(janjiId)[0]
            binding.tietkeluhan.setText(janjis.keluhan)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}