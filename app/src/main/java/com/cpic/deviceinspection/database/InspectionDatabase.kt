package com.cpic.deviceinspection.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cpic.deviceinspection.model.InspectionData

@Database(entities = [InspectionData::class], version = 1, exportSchema = false)
abstract class InspectionDatabase : RoomDatabase() {
    
    abstract fun inspectionDataDao(): InspectionDataDao

    companion object {
        private const val DATABASE_NAME = "inspection.db"

        @Volatile
        private var instance: InspectionDatabase? = null

        fun getInstance(context: Context): InspectionDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    InspectionDatabase::class.java,
                    DATABASE_NAME
                ).build().also {
                    instance = it
                }
            }
        }
    }
}
