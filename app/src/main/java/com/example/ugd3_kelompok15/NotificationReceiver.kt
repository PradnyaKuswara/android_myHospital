package com.example.ugd3_kelompok15

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val msg = intent.getStringExtra("toastMessage")
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}