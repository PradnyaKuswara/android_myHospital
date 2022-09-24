package com.example.ugd3_kelompok15.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.ugd3_kelompok15.HomeActivity
import com.example.ugd3_kelompok15.LoginActivity
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.room.UserDB
import com.example.ugd3_kelompok15.ui.janjitemu.JanjiTemuActivity
import kotlin.system.exitProcess

class FragmentHome : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db by lazy { UserDB(activity as HomeActivity) }
        val userDao = db.userDao()
        val btnLogout : Button = view.findViewById(R.id.btnLogout)
        val btnJanjiTemu: Button = view.findViewById(R.id.btn_janji_temu)
        val textNama: TextView = view.findViewById(R.id.textHome)

        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        val user = userDao.getUser(sharedPreferences.getInt("id", 0))

        textNama.setText(user.namaLengkap)


        btnJanjiTemu.setOnClickListener(View.OnClickListener {
           val movejanji = Intent(this@FragmentHome.context, JanjiTemuActivity::class.java)
            startActivity(movejanji)

        })

        btnLogout.setOnClickListener(View.OnClickListener {
            getActivity()?.let { it1 ->
                AlertDialog.Builder(it1).apply {
                    setTitle("Tolong Konfirmasi")
                    setMessage("Apakah anda yakin ingin keluar?")

                    setPositiveButton("Iya") { _, _ ->
                        exitProcess(0)
                    }

                    setNegativeButton("Tidak"){_, _ ->

                    }
                    setCancelable(true)
                }.create().show()
            }
        })
    }
    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.layout_fragment2, fragment)
            .addToBackStack(null).commit()
        transition.hide(FragmentHome())
    }
}