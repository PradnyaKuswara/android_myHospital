package com.example.ugd3_kelompok15.ui.lokasi

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ugd3_kelompok15.HomeActivity
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.databinding.FragmentLokasiRSBinding
import com.example.ugd3_kelompok15.ui.home.FragmentHome
import com.example.ugd3_kelompok15.ui.profile.FragmentProfile
import kotlinx.android.synthetic.main.fragment_lokasi_r_s.*
import org.json.JSONException
import org.json.JSONObject
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import java.io.IOException
import java.nio.charset.StandardCharsets

class FragmentLokasiRS : Fragment() {

    private var _binding: FragmentLokasiRSBinding? = null
    private val binding get() = _binding!!
    var modelMainList: MutableList<ModelMain> = ArrayList()
    lateinit var mapController: MapController
    lateinit var overlayItem: ArrayList<OverlayItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLokasiRSBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Configuration.getInstance().load(activity, PreferenceManager.getDefaultSharedPreferences(activity))

        val geoPoint = GeoPoint(-7.78165, 110.414497)
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.animateTo(geoPoint)
        binding.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        binding.mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

        mapController = mapView.controller as MapController
        mapController.setCenter(geoPoint)
        mapController.zoomTo(6)

        getLocationMarker()

    }
    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.layout_fragment, fragment)
            .addToBackStack(null).commit()
        transition.hide(FragmentProfile())
    }

    private fun getLocationMarker() {
        try {
            val stream = requireContext().assets.open("location_maps.json")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            val strContent = String(buffer, StandardCharsets.UTF_8)
            try {
                val jsonObject = JSONObject(strContent)
                val jsonArrayResult = jsonObject.getJSONArray("results")
                for (i in 0 until jsonArrayResult.length()) {
                    val jsonObjectResult = jsonArrayResult.getJSONObject(i)
                    val modelMain = ModelMain()
                    modelMain.strName = jsonObjectResult.getString("name")
                    modelMain.strVicinity = jsonObjectResult.getString("vicinity")

                    val jsonObjectGeo = jsonObjectResult.getJSONObject("geometry")
                    val jsonObjectLoc = jsonObjectGeo.getJSONObject("location")
                    modelMain.latLoc = jsonObjectLoc.getDouble("lat")
                    modelMain.longLoc = jsonObjectLoc.getDouble("lng")
                    modelMainList.add(modelMain)
                }
                initMarker(modelMainList)
            }catch (e: JSONException) {
                e.printStackTrace()
            }
        }catch (ignored: IOException) {
            Toast.makeText(
                activity,
                "Oops, Coba ulangi beberapa saat lagi.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initMarker(modelList: List<ModelMain>) {
        for (i in modelList.indices) {
            overlayItem = ArrayList()
            overlayItem.add(
                OverlayItem(
                    modelList[i].strName, modelList[i].strVicinity, GeoPoint(modelList[i].latLoc, modelList[i].longLoc
                    )
                )
            )
            val modelMain = ModelMain()
            modelMain.strName = modelList[i].strName
            modelMain.strVicinity = modelList[i].strVicinity

            val markerRs = Marker(mapView)
            markerRs.icon = resources.getDrawable(R.drawable.ic_baseline_location_on_24)
            markerRs.position = GeoPoint(modelList[i].latLoc,modelList[i].longLoc)
            markerRs.relatedObject = modelMain
            markerRs.infoWindow = InfoWindow(mapView)
            markerRs.setOnMarkerClickListener{item,  arg1 ->
                item.showInfoWindow()
                true
            }
            mapView.overlays.add(markerRs)
            mapView.invalidate()
        }
    }

    public override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(activity, PreferenceManager.getDefaultSharedPreferences(activity))
        if(mapView != null) {
            mapView.onResume()
        }
    }


    public override fun onPause() {
        super.onPause()
        Configuration.getInstance().load(activity, PreferenceManager.getDefaultSharedPreferences(activity))
        if(mapView != null) {
            mapView.onPause()
        }
    }
}