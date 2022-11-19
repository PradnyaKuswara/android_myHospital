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
import com.example.ugd3_kelompok15.models.JanjiTemu
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.adapter_janjitemu.view.*

class JanjiTemuAdapter(private var janjitemus: List<JanjiTemu>, context: Context): RecyclerView.Adapter<JanjiTemuAdapter.JanjiViewHolder>(), Filterable {

    private var filteredJanjiTemuList: MutableList<JanjiTemu>
    private val context: Context
    init {
        filteredJanjiTemuList = ArrayList(janjitemus)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JanjiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_janjitemu, parent, false)
        return JanjiViewHolder(view)
    }

    override fun onBindViewHolder(holder: JanjiViewHolder, position: Int) {
        val janji = janjitemus[position]
        holder.view.text_dokter.text = janji.dokter
        holder.view.text_rs.text = janji.rumahSakit
        holder.view.text_jadwal.text = janji.tanggal
        holder.view.text_keluhan.text = janji.keluhan

        holder.view.icon_delete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data janji temu ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){ _, _ ->
                    if (context is JanjiTemuActivity) janji.id?.let { it1 ->
                        context.deleteJanjiTemu(
                            it1
                        )
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
                val filtered : MutableList<JanjiTemu> = java.util.ArrayList()
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
                filteredJanjiTemuList.clear()
                filteredJanjiTemuList.addAll((filterResults.values as List<JanjiTemu>))
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int{
        return filteredJanjiTemuList.size
    }

    fun setJanjiTemuList(janjitemus: Array<JanjiTemu>){
        this.janjitemus = janjitemus.toList()
        filteredJanjiTemuList = janjitemus.toMutableList()
    }

    inner class JanjiViewHolder( val view: View) :
        RecyclerView.ViewHolder(view){
        var tvDokter: TextView
        var tvJadwal: TextView
        var tvRs: TextView
        var tvKeluhan: TextView
        var iconDelete: ImageButton
        var cvJanjiTemu: CardView

        init {
            tvDokter = itemView.findViewById(R.id.text_dokter)
            tvJadwal = itemView.findViewById(R.id.text_jadwal)
            tvRs = itemView.findViewById(R.id.text_rs)
            tvKeluhan = itemView.findViewById(R.id.text_keluhan)
            iconDelete = itemView.findViewById(R.id.icon_delete)
            cvJanjiTemu = itemView.findViewById(R.id.cv_janjitemu)
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