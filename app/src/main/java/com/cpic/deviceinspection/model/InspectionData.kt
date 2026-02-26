package com.cpic.deviceinspection.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "inspection_data")
data class InspectionData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val deviceId: String,
    val deviceName: String,
    val productionLine: String,
    val inspectionType: String,
    val inspectionValue: Double,
    val unit: String,
    val remark: String,
    val inspectionTime: Date = Date(),
    val uploaded: Boolean = false,
    val uploadTime: Date? = null
)
