package com.example.ugd3_kelompok15.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.ugd3_kelompok15.HomeActivity
import com.example.ugd3_kelompok15.LoginActivity
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.databinding.FragmentProfileBinding
import com.example.ugd3_kelompok15.room.UserDB
import com.example.ugd3_kelompok15.ui.home.FragmentHome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentProfile() : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

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
        setData()

        binding.btnUpdate.setOnClickListener {
            transitionFragment(FragmentEditProfil())
        }


       binding.btnDeleteAcc.setOnClickListener(View.OnClickListener {
            getActivity()?.let { it1 ->
                AlertDialog.Builder(it1).apply {
                    setTitle("Tolong Konfirmasi")
                    setMessage("Apakah anda yakin ingin menghapus akun?")

                    setPositiveButton("Iya") { _, _ ->
                        val intent = Intent(this@FragmentProfile.context, LoginActivity::class.java)
                        deleteAcc()
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

        binding.homeHospital.setOnClickListener {
            val intent = Intent(this@FragmentProfile.context, CameraActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setData() {
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val db by lazy { UserDB(activity as HomeActivity) }
        val userDao = db.userDao()

        val user = userDao.getUser(sharedPreferences.getInt("id", 0))
        binding.viewNamaLengkap.setText(user.namaLengkap)
        binding.viewUsername.setText(user.username)
        binding.viewEmail.setText(user.Email)
        binding.viewNomorTelepon.setText(user.nomorTelepon)
    }

    private fun deleteAcc() {
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val db by lazy { UserDB(activity as HomeActivity) }
        val userDao = db.userDao()

        val user = userDao.getUser(sharedPreferences.getInt("id", 0))

        userDao.deleteUser(user)
    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.layout_fragment, fragment)
            .addToBackStack(null).commit()
        transition.hide(FragmentProfile())
    }

}