package com.example.ugd3_kelompok15.ui.janjitemu

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.room.JanjiTemu
import kotlinx.android.synthetic.main.adapter_janjitemu.view.*

class JanjiTemuAdapter(private val janjitemus: ArrayList<JanjiTemu>, private val listener: OnAdapterListener): RecyclerView.Adapter<JanjiTemuAdapter.JanjiViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JanjiTemuAdapter.JanjiViewHolder {
        return JanjiViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_janjitemu, parent, false))
    }

    override fun onBindViewHolder(holder: JanjiTemuAdapter.JanjiViewHolder, position: Int) {
        val janji = janjitemus[position]
        holder.view.text_dokter.text = janji.dokter
        holder.view.text_rs.text = janji.rumahSakit
        holder.view.text_jadwal.text = janji.tanggal
        holder.view.text_keluhan.text = janji.keluhan
        holder.view.layoutrv.setOnClickListener {
            listener.onClick(janji)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(janji)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(janji)
        }
    }

    override fun getItemCount()= janjitemus.size

    inner class JanjiViewHolder( val view: View) :
        RecyclerView.ViewHolder(view)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<JanjiTemu>){
        janjitemus.clear()
        janjitemus.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(janjitemu: JanjiTemu)
        fun onUpdate(janjitemu: JanjiTemu)
        fun onDelete(janjitemu: JanjiTemu)
    }
}