package com.example.ugd3_kelompok15

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlin.system.exitProcess

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setTitle("My Hospital")

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