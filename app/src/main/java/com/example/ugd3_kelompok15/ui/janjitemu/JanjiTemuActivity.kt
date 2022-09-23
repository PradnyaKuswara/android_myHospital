package com.example.ugd3_kelompok15.ui.janjitemu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
            JanjiTemuAdapter.OnAdapterListener{
            override fun onClick(janjitemu: JanjiTemu) {
                intentEdit(janjitemu.id, Constant.TYPE_READ)
            }
            override fun onUpdate(janjitemu: JanjiTemu) {
                intentEdit(janjitemu.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(janjitemu: JanjiTemu) {
                TODO("Not yet implemented")
            }
        })
        list_janji_temu.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = janjiAdapter
        }
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
        binding.btnAdd.setOnClickListener(View.OnClickListener {
            intentEdit(0, Constant.TYPE_CREATE)
        })
    }

    fun intentEdit(janjiId: Int, intentType: Int) {
        startActivity(
            Intent(applicationContext, EditJanjiTemu::class.java)
                .putExtra("intent_id", janjiId)
                .putExtra("intent_type", intentType)

        )
    }
}