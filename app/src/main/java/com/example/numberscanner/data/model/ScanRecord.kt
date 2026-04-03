package com.example.numberscanner.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_records")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val serialNumber: String,
    val classification: String,
    val notes: String,
    val timestamp: Long = System.currentTimeMillis()
)
