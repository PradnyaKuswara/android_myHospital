package com.example.ugd3_kelompok15.ui.janjitemu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd3_kelompok15.R
//import com.example.ugd3_kelompok15.room.JanjiTemu
import kotlinx.android.synthetic.main.activity_janji_temu.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok15.api.JanjiTemuApi
import com.example.ugd3_kelompok15.models.JanjiTemuModels
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class JanjiTemuActivity : AppCompatActivity() {

    private var srJanjiTemu: SwipeRefreshLayout? = null
    private var adapter: JanjiTemuAdapter? = null
    private var svJanjiTemu: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_janji_temu)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srJanjiTemu = findViewById(R.id.sr_janjitemu)
        svJanjiTemu = findViewById(R.id.sv_janjitemu)

        srJanjiTemu?.setOnRefreshListener ( SwipeRefreshLayout.OnRefreshListener{ allJanji()} )
        svJanjiTemu?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener{
            val i = Intent(this@JanjiTemuActivity, EditJanjiTemu::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvJanji = findViewById<RecyclerView>(R.id.list_janji_temu)
        adapter = JanjiTemuAdapter(ArrayList(), this)
        rvJanji.layoutManager = LinearLayoutManager(this)
        rvJanji.adapter = adapter
        allJanji()
    }

    private fun allJanji(){
        srJanjiTemu!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, JanjiTemuApi.GET_ALL, Response.Listener { response ->
//                val gson = Gson()
//                var janjiTemu : Array<JanjiTemu> = gson.fromJson(response, Array<JanjiTemu>::class.java)
                var jo = JSONObject(response.toString())
                var janjiTemu = arrayListOf<JanjiTemuModels>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var janji = JanjiTemuModels(
                        jo.getJSONArray("data").getJSONObject(i).getInt("id"),
                        jo.getJSONArray("data").getJSONObject(i).getString("rumahSakit"),
                        jo.getJSONArray("data").getJSONObject(i).getString("tanggal"),
                        jo.getJSONArray("data").getJSONObject(i).getString("dokter"),
                        jo.getJSONArray("data").getJSONObject(i).getString("keluhan")
                    )
                    janjiTemu.add(janji)
                }
                var data: Array<JanjiTemuModels> = janjiTemu.toTypedArray()

                adapter!!.setJanjiTemuList(data)
                adapter!!.filter.filter(svJanjiTemu!!.query)
                srJanjiTemu!!.isRefreshing = false

                if (!data.isEmpty())
                    Toast.makeText(this@JanjiTemuActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()

            }, Response.ErrorListener { error ->
                srJanjiTemu!!.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@JanjiTemuActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Toast.makeText(this@JanjiTemuActivity, e.message, Toast.LENGTH_SHORT).show()
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

    fun deleteJanjiTemu(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, JanjiTemuApi.DELETE_URL + id, Response.Listener { response ->
                setLoading(false)

                val gson = Gson()
                var mahasiswa = gson.fromJson(response, JanjiTemuModels::class.java)
                if (mahasiswa != null)
                    Toast.makeText(this@JanjiTemuActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                allJanji()
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@JanjiTemuActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: java.lang.Exception){
                    Toast.makeText(this@JanjiTemuActivity, e.message, Toast.LENGTH_SHORT).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allJanji()
    }

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



//    val db by lazy {JanjiTemuDB(this)}
//    private var  janjiAdapter: JanjiTemuAdapter? = null
//    private lateinit var binding: ActivityJanjiTemuBinding
//    private var layoutLoading: LinearLayout? = null
//    private var queue: RequestQueue? = null
//
//    companion object{
//        const val LAUNCH_ADD_ACTIVITY = 123
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_janji_temu)
//        binding = ActivityJanjiTemuBinding.inflate(layoutInflater)
//        val view = binding.root
//
//        supportActionBar?.hide()
//        setContentView(view)
//
////        setupListener()
//        setupRecyclerView()
//
//        queue = Volley.newRequestQueue(this)
//        layoutLoading = findViewById(R.id.layout_loading)
//    }
//
//    private fun setupRecyclerView() {
////        janjiAdapter = JanjiTemuAdapter(arrayListOf(), object :
////            JanjiTemuAdapter.OnAdapterListener {
////            override fun onClick(janjitemu: JanjiTemu) {
////                intentEdit(janjitemu.id, Constant.TYPE_READ)
////            }
////
////            override fun onUpdate(janjitemu: JanjiTemu) {
////                intentEdit(janjitemu.id, Constant.TYPE_UPDATE)
////            }
////
////            override fun onDelete(janjitemu: JanjiTemu) {
////                deleteDialog(janjitemu)
////            }
////        })
////        list_janji_temu.apply {
////            layoutManager = LinearLayoutManager(applicationContext)
////            adapter = janjiAdapter
////        }
//        val rvJanji = findViewById<RecyclerView>(R.id.list_janji_temu)
//        janjiAdapter = JanjiTemuAdapter(ArrayList(), this)
//        rvJanji.layoutManager = LinearLayoutManager(this)
//        rvJanji.adapter = janjiAdapter
////        allMahasiswa()
//    }
//
////        private fun deleteDialog(janjitemu: JanjiTemu){
////            val alertDialog = AlertDialog.Builder(this)
////            alertDialog.apply {
////                setTitle("Confirmation")
////                setMessage("Are You Sure to delete this data From${janjitemu.tanggal}?")
////                setNegativeButton("Cancel", DialogInterface.OnClickListener
////                { dialogInterface, i ->
////                    dialogInterface.dismiss()
////                })
////                setPositiveButton("Delete", DialogInterface.OnClickListener
////                { dialogInterface, i ->
////                    dialogInterface.dismiss()
////                    CoroutineScope(Dispatchers.IO).launch {
////                        db.janjiTemuDao().deleteJanjiTemu(janjitemu)
////                        loadData()
////                    }
////                })
////            }
////            alertDialog.show()
////        }
//
//        private fun setLoading(isLoading: Boolean){
//            if(isLoading){
//                window.setFlags(
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//                )
//                layoutLoading!!.visibility = View.VISIBLE
//            } else {
//                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//                layoutLoading!!.visibility = View.INVISIBLE
//            }
//        }

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