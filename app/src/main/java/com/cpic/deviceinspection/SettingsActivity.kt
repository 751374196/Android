package com.cpic.deviceinspection

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cpic.deviceinspection.utils.PreferenceManager

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var radioGroup: RadioGroup
    private lateinit var etHost: EditText
    private lateinit var etPort: EditText
    private lateinit var etDatabase: EditText
    private lateinit var etUser: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSave: Button
    private lateinit var btnTest: Button
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        radioGroup = findViewById(R.id.radio_group)
        etHost = findViewById(R.id.et_host)
        etPort = findViewById(R.id.et_port)
        etDatabase = findViewById(R.id.et_database)
        etUser = findViewById(R.id.et_user)
        etPassword = findViewById(R.id.et_password)
        btnSave = findViewById(R.id.btn_save)
        btnTest = findViewById(R.id.btn_test)
        preferenceManager = PreferenceManager(this)

        loadConfig()

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_sql_server -> loadSqlServerConfig()
                R.id.radio_mysql -> loadMysqlConfig()
            }
        }

        btnSave.setOnClickListener {
            saveConfig()
        }

        btnTest.setOnClickListener {
            testConnection()
        }
    }

    private fun loadConfig() {
        val dbType = preferenceManager.getDatabaseType()
        if (dbType == "sql_server") {
            radioGroup.check(R.id.radio_sql_server)
            loadSqlServerConfig()
        } else {
            radioGroup.check(R.id.radio_mysql)
            loadMysqlConfig()
        }
    }

    private fun loadSqlServerConfig() {
        etHost.setText(preferenceManager.getSqlServerHost())
        etPort.setText(preferenceManager.getSqlServerPort())
        etDatabase.setText(preferenceManager.getSqlServerDatabase())
        etUser.setText(preferenceManager.getSqlServerUser())
        etPassword.setText(preferenceManager.getSqlServerPassword())
    }

    private fun loadMysqlConfig() {
        etHost.setText(preferenceManager.getMysqlHost())
        etPort.setText(preferenceManager.getMysqlPort())
        etDatabase.setText(preferenceManager.getMysqlDatabase())
        etUser.setText(preferenceManager.getMysqlUser())
        etPassword.setText(preferenceManager.getMysqlPassword())
    }

    private fun saveConfig() {
        val dbType = if (radioGroup.checkedRadioButtonId == R.id.radio_sql_server) "sql_server" else "mysql"
        preferenceManager.setDatabaseType(dbType)

        if (dbType == "sql_server") {
            preferenceManager.setSqlServerConfig(
                etHost.text.toString(),
                etPort.text.toString(),
                etDatabase.text.toString(),
                etUser.text.toString(),
                etPassword.text.toString()
            )
        } else {
            preferenceManager.setMysqlConfig(
                etHost.text.toString(),
                etPort.text.toString(),
                etDatabase.text.toString(),
                etUser.text.toString(),
                etPassword.text.toString()
            )
        }

        Toast.makeText(this, "配置已保存", Toast.LENGTH_SHORT).show()
    }

    private fun testConnection() {
        val dbType = if (radioGroup.checkedRadioButtonId == R.id.radio_sql_server) "sql_server" else "mysql"
        val host = etHost.text.toString()
        val port = etPort.text.toString()
        val database = etDatabase.text.toString()
        val user = etUser.text.toString()
        val password = etPassword.text.toString()

        val success = DatabaseManager.testConnection(dbType, host, port, database, user, password)
        if (success) {
            Toast.makeText(this, "连接成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "连接失败", Toast.LENGTH_SHORT).show()
        }
    }
}
