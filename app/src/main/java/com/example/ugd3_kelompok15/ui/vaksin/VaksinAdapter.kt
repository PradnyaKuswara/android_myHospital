package com.example.ugd3_kelompok15.ui.vaksin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.ugd3_kelompok15.models.VaksinModels
import com.example.ugd3_kelompok15.ui.janjitemu.JanjiTemuActivity
import com.example.ugd3_kelompok15.ui.janjitemu.JanjiTemuAdapter
import com.example.ugd3_kelompok15.ui.janjitemu.UpdateJanjiTemu
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class VaksinAdapter(private var vaksins: List<VaksinModels>, context: Context): RecyclerView.Adapter<VaksinAdapter.ViewHolder>(), Filterable {
    private var filteredVaksin: MutableList<VaksinModels>
    private val context: Context

    init {
        filteredVaksin = ArrayList(vaksins)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaksinAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_vaksin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: VaksinAdapter.ViewHolder, position: Int) {
        val vaksin = filteredVaksin[position]
        holder.tvLokasi.text = vaksin.lokasi
        holder.tvNama.text = vaksin.nama + " - " + vaksin.umur.toString()
        holder.tvJadwal.text = vaksin.tanggal
        holder.tvJenis.text = vaksin.jenis

        holder.iconDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data vaksin ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){ _, _ ->
                    if (context is VaksinActivity){
                        context.deleteVaksin(vaksin.id)
                    }
                }
                .show()
        }
        holder.cvVaksin.setOnClickListener{
            val i = Intent(context, AddUpdateVaksin::class.java)
            i.putExtra("id", vaksin.id)
            if(context is VaksinActivity)
                context.startActivityForResult(i, VaksinActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered : MutableList<VaksinModels> = java.util.ArrayList()
                if (charSequenceString.isEmpty()){
                    filtered.addAll(vaksins)
                }else{
                    for (vaksin in vaksins){
                        if (vaksin.lokasi.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(vaksin)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredVaksin.clear()
                filteredVaksin.addAll((filterResults.values as List<VaksinModels>))
                notifyDataSetChanged()
            }
        }
    }


    override fun getItemCount(): Int{
        return filteredVaksin.size
    }

    fun setVaksinList(vaksins: Array<VaksinModels>){
        this.vaksins = vaksins.toList()
        filteredVaksin = vaksins.toMutableList()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvLokasi: TextView
        var tvNama: TextView
        var tvJadwal: TextView
        var tvJenis: TextView
        var iconDelete: ImageButton
        var cvVaksin: CardView

        init {
           tvLokasi = view.findViewById(R.id.text_lokasi)
           tvNama = view.findViewById(R.id.text_nama)
           tvJadwal = view.findViewById(R.id.text_jadwal_vaksin)
           tvJenis = view.findViewById(R.id.text_jenis)
           iconDelete = view.findViewById(R.id.icon_delete_vaksin)
            cvVaksin = view.findViewById(R.id.cv_vaksin)
        }
    }

}