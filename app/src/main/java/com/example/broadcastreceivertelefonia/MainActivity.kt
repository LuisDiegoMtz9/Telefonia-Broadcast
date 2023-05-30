package com.example.broadcastreceivertelefonia

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private lateinit var txtNumero: TextView
    private lateinit var txtMensaje: TextView
    private val PERMISSIONS_REQUEST_CODE = 100
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.SEND_SMS
    )
    private val incomingCallReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            val Obtenernumero = intent?.getStringExtra("incoming_number")
            if (Obtenernumero == txtNumero.text.toString()) {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(
                    txtNumero.text.toString(), null, txtMensaje.text.toString(), null, null
                )
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arePermissionsGranted()) {
            setContentView(R.layout.activity_main)
            txtNumero = findViewById(R.id.txtTelefono)
            txtMensaje = findViewById(R.id.txtMensaje)
            val filter = IntentFilter("com.example.myapp.NEW_INCOMING_CALL")
            registerReceiver(incomingCallReceiver, filter)
        } else {
            requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(incomingCallReceiver)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (areAllPermissionsGranted(grantResults)) {
                setContentView(R.layout.activity_main)
                txtNumero = findViewById(R.id.txtTelefono)
                txtMensaje = findViewById(R.id.txtMensaje)

                val filter = IntentFilter("com.example.myapp.NEW_INCOMING_CALL")
                registerReceiver(incomingCallReceiver, filter)
            } else {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE)

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun arePermissionsGranted(): Boolean {
        for (permission in PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(
                    this, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun areAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }


}

