package com.example.ugd3_kelompok15.ui.janjitemu

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.models.JanjiTemuModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.adapter_janjitemu.view.*

class JanjiTemuAdapter(private var janjitemus: List<JanjiTemuModels>, context: Context): RecyclerView.Adapter<JanjiTemuAdapter.ViewHolder>(), Filterable {

    private var filteredJanji: MutableList<JanjiTemuModels>
    private val context: Context

    init {
        filteredJanji = ArrayList(janjitemus)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_janjitemu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JanjiTemuAdapter.ViewHolder, position: Int) {
        val janji = filteredJanji[position]
        holder.tvDokter.text = "Dokter: " + janji.dokter
        holder.tvJadwal.text = "Tanggal: " + janji.tanggal
        holder.tvRs.text = "Rumah Sakit: " + janji.rumahSakit
        holder.tvKeluhan.text = "Keluhan: " + janji.keluhan

        holder.iconDelete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data janji temu ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){ _, _ ->
                    if (context is JanjiTemuActivity){
                        context.deleteJanjiTemu(janji.id)
                    }
                }
                .show()
        }
        holder.cvJanjiTemu.setOnClickListener{
            val i = Intent(context, EditJanjiTemu::class.java)
            i.putExtra("id", janji.id)
            if(context is JanjiTemuActivity)
                context.startActivityForResult(i, JanjiTemuActivity.LAUNCH_ADD_ACTIVITY)
        }
    }
    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered : MutableList<JanjiTemuModels> = java.util.ArrayList()
                if (charSequenceString.isEmpty()){
                    filtered.addAll(janjitemus)
                }else{
                    for (janjiTemu in janjitemus){
                        if (janjiTemu.keluhan.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(janjiTemu)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredJanji.clear()
                filteredJanji.addAll((filterResults.values as List<JanjiTemuModels>))
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int{
        return filteredJanji.size
    }

    fun setJanjiTemuList(janjitemus: Array<JanjiTemuModels>){
        this.janjitemus = janjitemus.toList()
        filteredJanji = janjitemus.toMutableList()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvDokter: TextView
        var tvJadwal: TextView
        var tvRs: TextView
        var tvKeluhan: TextView
        var iconDelete: ImageButton
        var cvJanjiTemu: CardView

        init {
            tvDokter = view.findViewById(R.id.text_dokter)
            tvJadwal = view.findViewById(R.id.text_jadwal)
            tvRs = view.findViewById(R.id.text_rs)
            tvKeluhan = view.findViewById(R.id.text_keluhan)
            iconDelete = view.findViewById(R.id.icon_delete)
            cvJanjiTemu = view.findViewById(R.id.cv_janjitemu)
        }
    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setData(list: List<JanjiTemu>){
//        janjitemus.clear()
//        janjitemus.addAll(list)
//        notifyDataSetChanged()
//    }

//    interface OnAdapterListener {
//        fun onClick(janjitemu: JanjiTemu)
//        fun onUpdate(janjitemu: JanjiTemu)
//        fun onDelete(janjitemu: JanjiTemu)
//    }
}