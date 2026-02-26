package com.cpic.deviceinspection.utils

import android.content.Context
import com.cpic.deviceinspection.database.InspectionRepository
import com.cpic.deviceinspection.model.InspectionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.util.Date

object DatabaseManager {
    
    fun testConnection(dbType: String, host: String, port: String, database: String, user: String, password: String): Boolean {
        return try {
            when (dbType) {
                "sql_server" -> testSqlServerConnection(host, port, database, user, password)
                "mysql" -> testMysqlConnection(host, port, database, user, password)
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun testSqlServerConnection(host: String, port: String, database: String, user: String, password: String): Boolean {
        val url = "jdbc:sqlserver://$host:$port;databaseName=$database"
        DriverManager.getConnection(url, user, password).use { connection ->
            return connection.isValid(5)
        }
    }

    private fun testMysqlConnection(host: String, port: String, database: String, user: String, password: String): Boolean {
        val url = "jdbc:mysql://$host:$port/$database"
        DriverManager.getConnection(url, user, password).use { connection ->
            return connection.isValid(5)
        }
    }

    fun uploadData(context: Context, data: InspectionData): Boolean {
        val preferenceManager = PreferenceManager(context)
        val dbType = preferenceManager.getDatabaseType()
        
        return try {
            when (dbType) {
                "sql_server" -> uploadToSqlServer(context, data)
                "mysql" -> uploadToMysql(context, data)
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun uploadToSqlServer(context: Context, data: InspectionData): Boolean {
        val preferenceManager = PreferenceManager(context)
        val host = preferenceManager.getSqlServerHost()
        val port = preferenceManager.getSqlServerPort()
        val database = preferenceManager.getSqlServerDatabase()
        val user = preferenceManager.getSqlServerUser()
        val password = preferenceManager.getSqlServerPassword()

        val url = "jdbc:sqlserver://$host:$port;databaseName=$database"
        DriverManager.getConnection(url, user, password).use { connection ->
            val sql = "INSERT INTO inspection_data (device_id, device_name, production_line, inspection_type, inspection_value, unit, remark, inspection_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, data.deviceId)
                statement.setString(2, data.deviceName)
                statement.setString(3, data.productionLine)
                statement.setString(4, data.inspectionType)
                statement.setDouble(5, data.inspectionValue)
                statement.setString(6, data.unit)
                statement.setString(7, data.remark)
                statement.setTimestamp(8, java.sql.Timestamp(data.inspectionTime.time))
                val rowsAffected = statement.executeUpdate()
                return rowsAffected > 0
            }
        }
    }

    private fun uploadToMysql(context: Context, data: InspectionData): Boolean {
        val preferenceManager = PreferenceManager(context)
        val host = preferenceManager.getMysqlHost()
        val port = preferenceManager.getMysqlPort()
        val database = preferenceManager.getMysqlDatabase()
        val user = preferenceManager.getMysqlUser()
        val password = preferenceManager.getMysqlPassword()

        val url = "jdbc:mysql://$host:$port/$database"
        DriverManager.getConnection(url, user, password).use { connection ->
            val sql = "INSERT INTO inspection_data (device_id, device_name, production_line, inspection_type, inspection_value, unit, remark, inspection_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, data.deviceId)
                statement.setString(2, data.deviceName)
                statement.setString(3, data.productionLine)
                statement.setString(4, data.inspectionType)
                statement.setDouble(5, data.inspectionValue)
                statement.setString(6, data.unit)
                statement.setString(7, data.remark)
                statement.setTimestamp(8, java.sql.Timestamp(data.inspectionTime.time))
                val rowsAffected = statement.executeUpdate()
                return rowsAffected > 0
            }
        }
    }
}
