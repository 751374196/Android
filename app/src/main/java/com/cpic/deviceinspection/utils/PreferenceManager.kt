package com.cpic.deviceinspection.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_config", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // 数据库类型
    fun getDatabaseType(): String {
        return sharedPreferences.getString("database_type", "sql_server") ?: "sql_server"
    }

    fun setDatabaseType(type: String) {
        editor.putString("database_type", type).apply()
    }

    // SQL Server 配置
    fun getSqlServerHost(): String {
        return sharedPreferences.getString("sql_server_host", "zjzx.cpicfiber.com") ?: "zjzx.cpicfiber.com"
    }

    fun getSqlServerPort(): String {
        return sharedPreferences.getString("sql_server_port", "1433") ?: "1433"
    }

    fun getSqlServerDatabase(): String {
        return sharedPreferences.getString("sql_server_database", "CPIC_QMI") ?: "CPIC_QMI"
    }

    fun getSqlServerUser(): String {
        return sharedPreferences.getString("sql_server_user", "sa") ?: "sa"
    }

    fun getSqlServerPassword(): String {
        return sharedPreferences.getString("sql_server_password", "Cpic1234$") ?: "Cpic1234$"
    }

    fun setSqlServerConfig(host: String, port: String, database: String, user: String, password: String) {
        editor.putString("sql_server_host", host)
        editor.putString("sql_server_port", port)
        editor.putString("sql_server_database", database)
        editor.putString("sql_server_user", user)
        editor.putString("sql_server_password", password)
        editor.apply()
    }

    // MySQL 配置
    fun getMysqlHost(): String {
        return sharedPreferences.getString("mysql_host", "10.12.0.130") ?: "10.12.0.130"
    }

    fun getMysqlPort(): String {
        return sharedPreferences.getString("mysql_port", "9030") ?: "9030"
    }

    fun getMysqlDatabase(): String {
        return sharedPreferences.getString("mysql_database", "cpic_doris") ?: "cpic_doris"
    }

    fun getMysqlUser(): String {
        return sharedPreferences.getString("mysql_user", "root") ?: "root"
    }

    fun getMysqlPassword(): String {
        return sharedPreferences.getString("mysql_password", "root") ?: "root"
    }

    fun setMysqlConfig(host: String, port: String, database: String, user: String, password: String) {
        editor.putString("mysql_host", host)
        editor.putString("mysql_port", port)
        editor.putString("mysql_database", database)
        editor.putString("mysql_user", user)
        editor.putString("mysql_password", password)
        editor.apply()
    }

    // 管理员账户
    fun getAdminUsername(): String {
        return sharedPreferences.getString("admin_username", "CPIC") ?: "CPIC"
    }

    fun getAdminPassword(): String {
        return sharedPreferences.getString("admin_password", "CPIC") ?: "CPIC"
    }
}
