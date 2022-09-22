package com.example.ugd3_kelompok15.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.ugd3_kelompok15.R
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

        val btnLogout : Button = view.findViewById(R.id.btnLogout)
        val btnJanjiTemu: Button = view.findViewById(R.id.btn_janji_temu)



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
}