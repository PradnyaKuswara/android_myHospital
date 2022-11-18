package com.example.ugd3_kelompok15.ui.janjitemu

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject

class JanjiTemuActivity : AppCompatActivity() {

    val db by lazy {JanjiTemuDB(this)}
    private var  janjiAdapter: JanjiTemuAdapter? = null
    private lateinit var binding: ActivityJanjiTemuBinding
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_janji_temu)
        binding = ActivityJanjiTemuBinding.inflate(layoutInflater)
        val view = binding.root

        supportActionBar?.hide()
        setContentView(view)

//        setupListener()
        setupRecyclerView()

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
    }

    private fun setupRecyclerView() {
//        janjiAdapter = JanjiTemuAdapter(arrayListOf(), object :
//            JanjiTemuAdapter.OnAdapterListener {
//            override fun onClick(janjitemu: JanjiTemu) {
//                intentEdit(janjitemu.id, Constant.TYPE_READ)
//            }
//
//            override fun onUpdate(janjitemu: JanjiTemu) {
//                intentEdit(janjitemu.id, Constant.TYPE_UPDATE)
//            }
//
//            override fun onDelete(janjitemu: JanjiTemu) {
//                deleteDialog(janjitemu)
//            }
//        })
//        list_janji_temu.apply {
//            layoutManager = LinearLayoutManager(applicationContext)
//            adapter = janjiAdapter
//        }
        val rvJanji = findViewById<RecyclerView>(R.id.list_janji_temu)
        janjiAdapter = JanjiTemuAdapter(ArrayList(), this)
        rvJanji.layoutManager = LinearLayoutManager(this)
        rvJanji.adapter = janjiAdapter
//        allMahasiswa()
    }

//        private fun deleteDialog(janjitemu: JanjiTemu){
//            val alertDialog = AlertDialog.Builder(this)
//            alertDialog.apply {
//                setTitle("Confirmation")
//                setMessage("Are You Sure to delete this data From${janjitemu.tanggal}?")
//                setNegativeButton("Cancel", DialogInterface.OnClickListener
//                { dialogInterface, i ->
//                    dialogInterface.dismiss()
//                })
//                setPositiveButton("Delete", DialogInterface.OnClickListener
//                { dialogInterface, i ->
//                    dialogInterface.dismiss()
//                    CoroutineScope(Dispatchers.IO).launch {
//                        db.janjiTemuDao().deleteJanjiTemu(janjitemu)
//                        loadData()
//                    }
//                })
//            }
//            alertDialog.show()
//        }

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

//    override fun onStart() {
//        super.onStart()
//        loadData()
//    }

//    fun loadData() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val notes = db.janjiTemuDao().getJanjis()
//            Log.d("EditJanjiTemu","dbResponse: $notes")
//            withContext(Dispatchers.Main){
//                janjiAdapter.setData(notes)
//            }
//        }
//    }

//    fun setupListener() {
//        binding.btnAdd.setOnClickListener {
//            intentEdit(0, Constant.TYPE_CREATE)
//        }
//    }
//
//    fun intentEdit(janjiId: Int, intentType: Int) {
//        startActivity(
//            Intent(applicationContext, EditJanjiTemu::class.java)
//                .putExtra("intent_id", janjiId)
//                .putExtra("intent_type", intentType)
//        )
//    }
}