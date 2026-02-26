package com.cpic.deviceinspection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cpic.deviceinspection.database.InspectionRepository
import com.cpic.deviceinspection.model.InspectionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InspectionDataAdapter
    private lateinit var repository: InspectionRepository
    private lateinit var btnClear: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.recycler_view)
        btnClear = findViewById(R.id.btn_clear)
        repository = InspectionRepository(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InspectionDataAdapter(emptyList()) { data ->
            deleteData(data)
        }
        recyclerView.adapter = adapter

        loadData()

        btnClear.setOnClickListener {
            clearAllData()
        }
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.Main).launch {
            val dataList = repository.getAllData()
            adapter.updateData(dataList)
        }
    }

    private fun deleteData(data: InspectionData) {
        CoroutineScope(Dispatchers.Main).launch {
            repository.delete(data)
            Toast.makeText(this@HistoryActivity, "删除成功", Toast.LENGTH_SHORT).show()
            loadData()
        }
    }

    private fun clearAllData() {
        CoroutineScope(Dispatchers.Main).launch {
            repository.deleteAll()
            Toast.makeText(this@HistoryActivity, "数据已清空", Toast.LENGTH_SHORT).show()
            loadData()
        }
    }

    inner class InspectionDataAdapter(
        private var dataList: List<InspectionData>,
        private val onDelete: (InspectionData) -> Unit
    ) : RecyclerView.Adapter<InspectionDataAdapter.ViewHolder>() {

        fun updateData(newData: List<InspectionData>) {
            dataList = newData
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_inspection_data, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = dataList[position]
            holder.bind(data)
        }

        override fun getItemCount(): Int = dataList.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvDeviceName: TextView = itemView.findViewById(R.id.tv_device_name)
            private val tvDeviceId: TextView = itemView.findViewById(R.id.tv_device_id)
            private val tvInspectionType: TextView = itemView.findViewById(R.id.tv_inspection_type)
            private val tvValue: TextView = itemView.findViewById(R.id.tv_value)
            private val tvTime: TextView = itemView.findViewById(R.id.tv_time)
            private val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
            private val btnDelete: Button = itemView.findViewById(R.id.btn_delete)

            fun bind(data: InspectionData) {
                tvDeviceName.text = data.deviceName
                tvDeviceId.text = data.deviceId
                tvInspectionType.text = data.inspectionType
                tvValue.text = "${data.inspectionValue} ${data.unit}"
                tvTime.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(data.inspectionTime)
                tvStatus.text = if (data.uploaded) "已上传" else "未上传"
                tvStatus.setTextColor(if (data.uploaded) resources.getColor(R.color.green) else resources.getColor(R.color.red))

                btnDelete.setOnClickListener {
                    onDelete(data)
                }
            }
        }
    }
}
