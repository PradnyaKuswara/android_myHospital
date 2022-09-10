package com.example.ugd3_kelompok15

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.ugd3_kelompok15.ui.home.FragmentHome
import com.example.ugd3_kelompok15.ui.informasidokter.FragmentDokter
import kotlin.system.exitProcess

class HomeActivity : AppCompatActivity() {
    private lateinit var layout_fragment : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setTitle("My Hospital")

        changeFragment(FragmentHome())
//        init()
//
    }
//    private fun init(){
//        layout_fragment = findViewById(R.id.layout_fragment)
//    }
    fun changeFragment(fragment: Fragment?) {
        if (fragment != null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment, fragment)
                .commit()
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Tolong Konfirmasi")
            setMessage("Apakah anda yakin ingin keluar?")

            setPositiveButton("Iya") { _, _ ->
                exitProcess(0)
            }

            setNegativeButton("Tidak"){_, _ ->
                Toast.makeText(this@HomeActivity, "Terima Kasih",
                    Toast.LENGTH_LONG).show()
            }

            setCancelable(true)
        }.create().show()
    }
}