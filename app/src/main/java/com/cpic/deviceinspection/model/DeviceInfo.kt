package com.cpic.deviceinspection.model

data class DeviceInfo(
    val deviceId: String,
    val deviceName: String,
    val productionLine: String,
    val inspectionType: String,
    val unit: String
)
