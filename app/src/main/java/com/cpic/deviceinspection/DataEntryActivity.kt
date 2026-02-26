package com.cpic.deviceinspection

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cpic.deviceinspection.database.InspectionRepository
import com.cpic.deviceinspection.model.DeviceInfo
import com.cpic.deviceinspection.model.InspectionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataEntryActivity : AppCompatActivity() {
    
    private lateinit var deviceInfo: DeviceInfo
    private lateinit var recognizedValue: Double
    private lateinit var etValue: EditText
    private lateinit var etRemark: EditText
    private lateinit var tvDeviceId: TextView
    private lateinit var tvDeviceName: TextView
    private lateinit var tvProductionLine: TextView
    private lateinit var tvInspectionType: TextView
    private lateinit var tvUnit: TextView
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var repository: InspectionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_entry)

        deviceInfo = intent.getSerializableExtra("deviceInfo") as? DeviceInfo ?: return
        recognizedValue = intent.getDoubleExtra("recognizedValue", 0.0)
        repository = InspectionRepository(this)

        initViews()
        initData()
        initListeners()
    }

    private fun initViews() {
        etValue = findViewById(R.id.et_value)
        etRemark = findViewById(R.id.et_remark)
        tvDeviceId = findViewById(R.id.tv_device_id)
        tvDeviceName = findViewById(R.id.tv_device_name)
        tvProductionLine = findViewById(R.id.tv_production_line)
        tvInspectionType = findViewById(R.id.tv_inspection_type)
        tvUnit = findViewById(R.id.tv_unit)
        btnSave = findViewById(R.id.btn_save)
        btnCancel = findViewById(R.id.btn_cancel)
    }

    private fun initData() {
        tvDeviceId.text = deviceInfo.deviceId
        tvDeviceName.text = deviceInfo.deviceName
        tvProductionLine.text = deviceInfo.productionLine
        tvInspectionType.text = deviceInfo.inspectionType
        tvUnit.text = deviceInfo.unit
        etValue.setText(recognizedValue.toString())
    }

    private fun initListeners() {
        btnSave.setOnClickListener {
            val valueText = etValue.text.toString()
            if (valueText.isEmpty()) {
                Toast.makeText(this, "请输入检测值", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val value = valueText.toDoubleOrNull() ?: 0.0
            val remark = etRemark.text.toString()

            saveData(value, remark)
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun saveData(value: Double, remark: String) {
        val inspectionData = InspectionData(
            deviceId = deviceInfo.deviceId,
            deviceName = deviceInfo.deviceName,
            productionLine = deviceInfo.productionLine,
            inspectionType = deviceInfo.inspectionType,
            inspectionValue = value,
            unit = deviceInfo.unit,
            remark = remark
        )

        CoroutineScope(Dispatchers.Main).launch {
            repository.insert(inspectionData)
            Toast.makeText(this@DataEntryActivity, "数据保存成功", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
