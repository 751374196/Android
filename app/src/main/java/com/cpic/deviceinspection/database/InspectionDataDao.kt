package com.cpic.deviceinspection.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cpic.deviceinspection.model.InspectionData
import java.util.Date

@Dao
interface InspectionDataDao {
    
    @Insert
    suspend fun insert(data: InspectionData): Long

    @Update
    suspend fun update(data: InspectionData)

    @Delete
    suspend fun delete(data: InspectionData)

    @Query("DELETE FROM inspection_data WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM inspection_data")
    suspend fun deleteAll()

    @Query("SELECT * FROM inspection_data ORDER BY inspection_time DESC")
    suspend fun getAllData(): List<InspectionData>

    @Query("SELECT * FROM inspection_data WHERE uploaded = 0 ORDER BY inspection_time DESC")
    suspend fun getUnuploadedData(): List<InspectionData>

    @Query("SELECT * FROM inspection_data WHERE device_id = :deviceId ORDER BY inspection_time DESC")
    suspend fun getDataByDeviceId(deviceId: String): List<InspectionData>

    @Query("SELECT * FROM inspection_data WHERE inspection_time BETWEEN :startDate AND :endDate ORDER BY inspection_time DESC")
    suspend fun getDataByDateRange(startDate: Date, endDate: Date): List<InspectionData>

    @Query("SELECT * FROM inspection_data WHERE production_line = :productionLine ORDER BY inspection_time DESC")
    suspend fun getDataByProductionLine(productionLine: String): List<InspectionData>

    @Query("SELECT * FROM inspection_data WHERE inspection_type = :inspectionType ORDER BY inspection_time DESC")
    suspend fun getDataByInspectionType(inspectionType: String): List<InspectionData>
}
