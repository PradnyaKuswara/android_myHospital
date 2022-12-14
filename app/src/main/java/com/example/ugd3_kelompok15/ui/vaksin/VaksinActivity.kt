package com.example.ugd3_kelompok15.ui.vaksin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.api.JanjiTemuApi
import com.example.ugd3_kelompok15.api.VaksinApi
import com.example.ugd3_kelompok15.models.JanjiTemuModels
import com.example.ugd3_kelompok15.models.VaksinModels
import com.example.ugd3_kelompok15.ui.janjitemu.JanjiTemuActivity
import com.example.ugd3_kelompok15.ui.janjitemu.UpdateJanjiTemu
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class VaksinActivity : AppCompatActivity() {

    private var srVaksin: SwipeRefreshLayout? = null
    private var adapter: VaksinAdapter? = null
    private var svVaksin: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaksin)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srVaksin = findViewById(R.id.sr_janjitemuvaksin)
        svVaksin = findViewById(R.id.sv_janjiVaksin)

        srVaksin?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener{ allVaksin()})
        svVaksin?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })
        val btnAdd = findViewById<Button>(R.id.btnAddVaksin)
        btnAdd.setOnClickListener{
            val i = Intent(this@VaksinActivity, AddUpdateVaksin::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }
        val rvVaksin = findViewById<RecyclerView>(R.id.list_janji_vaksin)
        adapter = VaksinAdapter(ArrayList(),this)
        rvVaksin.layoutManager = LinearLayoutManager(this)
        rvVaksin.adapter = adapter
        allVaksin()
    }

    private fun allVaksin() {
        srVaksin!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, VaksinApi.GET_ALL, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var vaksinal = arrayListOf<VaksinModels>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var vaksin = VaksinModels(
                        jo.getJSONArray("data").getJSONObject(i).getInt("id"),
                        jo.getJSONArray("data").getJSONObject(i).getString("nama"),
                        jo.getJSONArray("data").getJSONObject(i).getInt("umur"),
                        jo.getJSONArray("data").getJSONObject(i).getString("lokasi"),
                        jo.getJSONArray("data").getJSONObject(i).getString("jenis"),
                        jo.getJSONArray("data").getJSONObject(i).getString("tanggal"),
                    )
                    vaksinal.add(vaksin)
                }
                var data: Array<VaksinModels> = vaksinal.toTypedArray()

                adapter!!.setVaksinList(data)
                adapter!!.filter.filter(svVaksin!!.query)
                srVaksin!!.isRefreshing = false

                if (!data.isEmpty())
//                    Toast.makeText(this@JanjiTemuActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification History Vaksin!",
                        "Data Vaksin berhasil ditampilkan!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

            }, Response.ErrorListener { error ->
                srVaksin!!.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(this@JanjiTemuActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification History Vaksin!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                } catch (e: Exception){
//                    Toast.makeText(this@JanjiTemuActivity, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification History Vaksin!",
                        e.message.toString(),
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
    fun deleteVaksin(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, VaksinApi.DELETE_URL + id, Response.Listener { response ->
                setLoading(false)

                val gson = Gson()
                var vaksin = gson.fromJson(response, VaksinModels::class.java)
                if (vaksin != null)
//                    Toast.makeText(this@JanjiTemuActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification History Vaksin!",
                        "Data Vaksin berhasil dihapus!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                allVaksin()
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(this@JanjiTemuActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification History Vaksin!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                } catch (e: java.lang.Exception){
//                    Toast.makeText(this@JanjiTemuActivity, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification History Vaksin!",
                        e.message.toString(),
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allVaksin()
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
}