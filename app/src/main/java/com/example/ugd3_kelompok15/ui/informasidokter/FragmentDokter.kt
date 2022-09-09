package com.example.ugd3_kelompok15.ui.informasidokter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.entity.Dokter

class FragmentDokter : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dokter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVDokterAdapter = RVDokterAdapter(Dokter.listOfDokter)
        val rvDokter : RecyclerView = view.findViewById(R.id.fg_dokter)

        rvDokter.layoutManager = layoutManager
        rvDokter.setHasFixedSize(true)
        rvDokter.adapter = adapter
    }
}