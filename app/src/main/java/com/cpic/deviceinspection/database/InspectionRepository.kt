package com.cpic.deviceinspection.database

import android.content.Context
import com.cpic.deviceinspection.model.InspectionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class InspectionRepository(context: Context) {
    
    private val dao = InspectionDatabase.getInstance(context).inspectionDataDao()

    suspend fun insert(data: InspectionData): Long {
        return withContext(Dispatchers.IO) {
            dao.insert(data)
        }
    }

    suspend fun update(data: InspectionData) {
        withContext(Dispatchers.IO) {
            dao.update(data)
        }
    }

    suspend fun delete(data: InspectionData) {
        withContext(Dispatchers.IO) {
            dao.delete(data)
        }
    }

    suspend fun deleteById(id: Long) {
        withContext(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }

    suspend fun getAllData(): List<InspectionData> {
        return withContext(Dispatchers.IO) {
            dao.getAllData()
        }
    }

    suspend fun getUnuploadedData(): List<InspectionData> {
        return withContext(Dispatchers.IO) {
            dao.getUnuploadedData()
        }
    }

    suspend fun getDataByDeviceId(deviceId: String): List<InspectionData> {
        return withContext(Dispatchers.IO) {
            dao.getDataByDeviceId(deviceId)
        }
    }

    suspend fun getDataByDateRange(startDate: Date, endDate: Date): List<InspectionData> {
        return withContext(Dispatchers.IO) {
            dao.getDataByDateRange(startDate, endDate)
        }
    }

    suspend fun getDataByProductionLine(productionLine: String): List<InspectionData> {
        return withContext(Dispatchers.IO) {
            dao.getDataByProductionLine(productionLine)
        }
    }

    suspend fun getDataByInspectionType(inspectionType: String): List<InspectionData> {
        return withContext(Dispatchers.IO) {
            dao.getDataByInspectionType(inspectionType)
        }
    }
}
