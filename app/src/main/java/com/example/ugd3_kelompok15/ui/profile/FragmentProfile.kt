package com.example.ugd3_kelompok15.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok15.HomeActivity
import com.example.ugd3_kelompok15.LoginActivity
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.api.UserProfilApi
import com.example.ugd3_kelompok15.databinding.FragmentProfileBinding
import com.example.ugd3_kelompok15.models.UserProfil
import com.example.ugd3_kelompok15.room.UserDB
import com.example.ugd3_kelompok15.ui.home.FragmentHome
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets


class FragmentProfile() : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
    private var layoutloading: LinearLayout? = null

    val db by lazy { UserDB(requireActivity()) }
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setData()

        queue = Volley.newRequestQueue(activity)
        layoutloading = view.findViewById(R.id.layout_loading)
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        var id = sharedPreferences.getInt("id",0)

        //Read User Profil
        getUsersByid(id)

        binding.btnUpdate.setOnClickListener {
            transitionFragment(FragmentUpdateProfil())
        }

       binding.btnDeleteAcc.setOnClickListener(View.OnClickListener {
            getActivity()?.let { it1 ->
                AlertDialog.Builder(it1).apply {
                    setTitle("Tolong Konfirmasi")
                    setMessage("Apakah anda yakin ingin menghapus akun?")

                    setPositiveButton("Iya") { _, _ ->
                        val intent = Intent(this@FragmentProfile.context, LoginActivity::class.java)
                        //Delete User Profil
                        deleteUser(id)
                        intent.putExtra("finish", true)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }

                    setNegativeButton("Tidak"){_, _ ->

                    }
                    setCancelable(true)
                }.create().show()
            }
        })


        //camera
        binding.homeHospital.setOnClickListener {
            val intent = Intent(this@FragmentProfile.context, CameraActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    private fun setData() {
//        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
//
//        val db by lazy { UserDB(activity as HomeActivity) }
//        val userDao = db.userDao()
//
//        val user = userDao.getUser(sharedPreferences.getInt("id", 0))
//        binding.viewNamaLengkap.setText(user.namaLengkap)
//        binding.viewUsername.setText(user.username)
//        binding.viewEmail.setText(user.Email)
//        binding.viewNomorTelepon.setText(user.nomorTelepon)
//    }

//    private fun deleteAcc() {
//        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
//
//        val db by lazy { UserDB(activity as HomeActivity) }
//        val userDao = db.userDao()
//
//        val user = userDao.getUser(sharedPreferences.getInt("id", 0))
//
//        userDao.deleteUser(user)
//    }

    private fun getUsersByid(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, UserProfilApi.GET_BY_ID_URL + id, Response.Listener { response ->
                // val gson = Gson()
                // val mahasiswa = gson.fromJson(response, Mahasiswa::class.java)

                var joUser = JSONObject(response.toString())
                val userdata = joUser.getJSONObject("data")

                binding.viewNamaLengkap.setText(userdata.getString("namaLengkap"))
                binding.viewUsername.setText(userdata.getString("username"))
                binding.viewEmail.setText(userdata.getString("Email"))
                binding.viewNomorTelepon.setText(userdata.getString("nomorTelepon"))

//                Toast.makeText(activity, "Data User berhasil diambil!", Toast.LENGTH_SHORT).show()
                MotionToast.darkColorToast(
                    context as Activity,"Notification Profil!",
                    "Data User Berhasil Ditampilkan!!",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        activity,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    MotionToast.darkColorToast(
                        context as Activity,"Notification Profil!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: Exception) {
                    MotionToast.darkColorToast(
                        context as Activity,"Notification Profil!",
                        e.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    fun deleteUser(id: Int){
        setLoading(true)

        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, UserProfilApi.DELETE_URL + id, Response.Listener{ response ->
                setLoading(false)

                val gson = Gson()
                var user  = gson.fromJson(response, UserProfil:: class.java)


                if(user != null)
//                    Toast.makeText(activity, "Berhasil Hapus Akun", Toast.LENGTH_SHORT).show()
                    MotionToast.darkColorToast(
                        context as Activity,"Notification Profil!",
                        "Berhasil delete akun",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
            }, Response.ErrorListener{ error ->
                setLoading(false)

                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//
                    MotionToast.darkColorToast(
                        context as Activity,"Notification Profil!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch(e: Exception){
                    MotionToast.darkColorToast(
                        context as Activity,"Notification Profil!",
                        e.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>{
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun setLoading(isLoading: Boolean) {
        if(isLoading) {
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutloading!!.visibility = View.VISIBLE
        }else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutloading!!.visibility = View.INVISIBLE
        }
    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.layout_fragment, fragment)
            .addToBackStack(null).commit()
        transition.hide(FragmentProfile())
    }

}