package com.example.ugd3_kelompok15.ui.lokasi

import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.ui.lokasi.ModelMain
import kotlinx.android.synthetic.main.layout_tooltip_rs.view.*
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class InfoWindow(mapView: MapView?) : InfoWindow(R.layout.layout_tooltip_rs, mapView){
    override fun onOpen(item: Any) {
        val markerRs = item as Marker
        val infoWindowData = markerRs.relatedObject as ModelMain

        val namaLokasi = mView.tv_nama_lokasi
        val alamat = mView.tv_alamat
        val closeBtn = mView.image_close

        namaLokasi.text = infoWindowData.strName
        alamat.text = infoWindowData.strVicinity
        closeBtn.setOnClickListener{
            markerRs.closeInfoWindow()
        }
    }

    override fun onClose() {
        //
    }
}