package com.example.ugd3_kelompok15.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok15.HomeActivity
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.api.UserProfilApi
import com.example.ugd3_kelompok15.databinding.FragmentUpdateProfilBinding
import com.example.ugd3_kelompok15.models.UserProfil
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Exception
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws


class FragmentUpdateProfil : Fragment() {
    private var _binding: FragmentUpdateProfilBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
    private var layoutloading: LinearLayout? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateProfilBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        queue = Volley.newRequestQueue(activity)
        layoutloading = view.findViewById(R.id.layout_loading)
        sharedPreferences = (activity as HomeActivity).getSharedPreferences("login", Context.MODE_PRIVATE)

        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        var id = sharedPreferences.getInt("id",0)
        var pass = sharedPreferences.getString("pass",null)

        var check = true

        binding.btnUpdate.setOnClickListener {
            if(binding.tietnamalengkap.text.toString().isEmpty()) {
                binding.tietnamalengkap.setError("Kosong")
                check =false
            }
            if (binding.tietEmail.text.toString().isEmpty()){
                binding.tietEmail.setError("Kosong")
                check =false
            }
            if(binding.tietNoTelp.text.toString().isEmpty()) {
                binding.tietNoTelp.setError("Kosong")
                check =false
            }
            if(binding.tietUsername.text.toString().isEmpty()) {
                binding.tietUsername.setError("Kosong")
                check =false
            }
            if(!check) {
                check = true
                return@setOnClickListener
            }else {
                //updateData()
                if (pass != null) {
                    updateUser(id,pass)
                }
                transitionFragment(FragmentProfile())
            }

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.layout_fragment, fragment)
            .addToBackStack(null).commit()
        transition.hide(FragmentUpdateProfil())
    }

    private fun updateUser(id: Int, pass: String) {
        setLoading(true)

        val user= UserProfil(
            id,
            binding.tietnamalengkap.text.toString(),
            binding.tietUsername.text.toString(),
            binding.tietEmail.text.toString(),
            binding.tietNoTelp.text.toString(),
            pass
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.PUT, UserProfilApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                var user = gson.fromJson(response, UserProfil::class.java)

                if(user != null) {
                    var resJO = JSONObject(response.toString())
                    val  userobj = resJO.getJSONObject("data")

                    sharedPreferences.edit()
                        .putInt("id",userobj.getInt("id"))
                        .putString("nama",userobj.getString("namaLengkap"))
                        .putString("pass",userobj.getString("password"))
                        .apply()
//                    Toast.makeText(activity, "User Berhasil Diupdate", Toast.LENGTH_SHORT).show()

                    MotionToast.darkColorToast(
                        context as Activity,"Notification Profil!",
                        "Data user berhasil diperbarui!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }
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
//                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
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

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(user)
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
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutloading!!.visibility = View.VISIBLE
        }else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutloading!!.visibility = View.INVISIBLE
        }
    }

}