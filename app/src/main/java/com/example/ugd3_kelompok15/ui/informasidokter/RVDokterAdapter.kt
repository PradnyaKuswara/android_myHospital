package com.example.ugd3_kelompok15.ui.informasidokter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.entity.Dokter

class RVDokterAdapter(private val data: Array<Dokter>) : RecyclerView.Adapter<RVDokterAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_dokter, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvNamaDokter.text = currentItem.nama
        holder.tvSpesialis.text = "${currentItem.spesialis} - ${currentItem.tahunMasuk}"
        holder.tvJadwal.text = currentItem.hariPraktek
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaDokter: TextView = itemView.findViewById(R.id.tv_namaDokter)
        val tvSpesialis: TextView = itemView.findViewById(R.id.tv_spesialis)
        val tvJadwal: TextView = itemView.findViewById(R.id.tv_jadwal)
    }
}