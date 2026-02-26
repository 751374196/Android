package com.cpic.deviceinspection.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.cpic.deviceinspection.database.InspectionRepository
import com.cpic.deviceinspection.model.InspectionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object UploadManager {
    
    fun uploadAllData(context: Context) {
        if (!isNetworkConnected(context)) {
            Toast.makeText(context, "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show()
            return
        }

        val repository = InspectionRepository(context)

        CoroutineScope(Dispatchers.Main).launch {
            val unuploadedData = repository.getUnuploadedData()
            if (unuploadedData.isEmpty()) {
                Toast.makeText(context, "没有待上传的数据", Toast.LENGTH_SHORT).show()
                return@launch
            }

            var successCount = 0
            var failedCount = 0

            for (data in unuploadedData) {
                val success = DatabaseManager.uploadData(context, data)
                if (success) {
                    val updatedData = data.copy(uploaded = true, uploadTime = java.util.Date())
                    repository.update(updatedData)
                    successCount++
                } else {
                    failedCount++
                }
            }

            val message = "上传完成：成功 $successCount 条，失败 $failedCount 条"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
