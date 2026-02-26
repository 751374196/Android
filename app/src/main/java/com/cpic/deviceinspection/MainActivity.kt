package com.cpic.deviceinspection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var btnScanQr: Button
    private lateinit var btnCapture: Button
    private lateinit var btnDataEntry: Button
    private lateinit var btnUpload: Button
    private lateinit var btnHistory: Button
    private lateinit var btnSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initListeners()
    }

    private fun initViews() {
        btnScanQr = findViewById(R.id.btn_scan_qr)
        btnCapture = findViewById(R.id.btn_capture)
        btnDataEntry = findViewById(R.id.btn_data_entry)
        btnUpload = findViewById(R.id.btn_upload)
        btnHistory = findViewById(R.id.btn_history)
        btnSettings = findViewById(R.id.btn_settings)
    }

    private fun initListeners() {
        btnScanQr.setOnClickListener {
            startActivity(Intent(this, ScanQrCodeActivity::class.java))
        }

        btnCapture.setOnClickListener {
            startActivity(Intent(this, CaptureImageActivity::class.java))
        }

        btnDataEntry.setOnClickListener {
            startActivity(Intent(this, DataEntryActivity::class.java))
        }

        btnUpload.setOnClickListener {
            // 实现数据上传逻辑
            UploadManager.uploadAllData(this)
        }

        btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
