package com.example.ugd3_kelompok15.ui.janjitemu

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.databinding.ActivityJanjiTemuBinding
import com.example.ugd3_kelompok15.room.Constant
import com.example.ugd3_kelompok15.room.JanjiTemu
import com.example.ugd3_kelompok15.room.JanjiTemuDB
import kotlinx.android.synthetic.main.activity_janji_temu.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JanjiTemuActivity : AppCompatActivity() {

    val db by lazy {JanjiTemuDB(this)}
    lateinit var  janjiAdapter: JanjiTemuAdapter
    private lateinit var binding: ActivityJanjiTemuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_janji_temu)
        binding = ActivityJanjiTemuBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)


        setupListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        janjiAdapter = JanjiTemuAdapter(arrayListOf(), object :
            JanjiTemuAdapter.OnAdapterListener {
            override fun onClick(janjitemu: JanjiTemu) {
                intentEdit(janjitemu.id, Constant.TYPE_READ)
            }

            override fun onUpdate(janjitemu: JanjiTemu) {
                intentEdit(janjitemu.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(janjitemu: JanjiTemu) {
                deleteDialog(janjitemu)
            }
        })
        list_janji_temu.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = janjiAdapter
        }
    }

        private fun deleteDialog(janjitemu: JanjiTemu){
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.apply {
                setTitle("Confirmation")
                setMessage("Are You Sure to delete this data From${janjitemu.tanggal}?")
                setNegativeButton("Cancel", DialogInterface.OnClickListener
                { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
                setPositiveButton("Delete", DialogInterface.OnClickListener
                { dialogInterface, i ->
                    dialogInterface.dismiss()
                    CoroutineScope(Dispatchers.IO).launch {
                        db.janjiTemuDao().deleteJanjiTemu(janjitemu)
                        loadData()
                    }
                })
            }
            alertDialog.show()
        }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.janjiTemuDao().getJanjis()
            Log.d("EditJanjiTemu","dbResponse: $notes")
            withContext(Dispatchers.Main){
                janjiAdapter.setData(notes)
            }
        }
    }

    fun setupListener() {
        binding.btnAdd.setOnClickListener {
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(janjiId: Int, intentType: Int) {
        startActivity(
            Intent(applicationContext, EditJanjiTemu::class.java)
                .putExtra("intent_id", janjiId)
                .putExtra("intent_type", intentType)
        )
    }
}