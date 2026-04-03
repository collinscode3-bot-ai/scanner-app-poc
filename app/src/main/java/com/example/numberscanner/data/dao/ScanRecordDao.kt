package com.example.numberscanner.data.dao

import androidx.room.*
import com.example.numberscanner.data.model.ScanRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanRecordDao {
    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<ScanRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: ScanRecord): Long

    @Delete
    suspend fun deleteRecord(record: ScanRecord)

    @Query("DELETE FROM scan_records")
    suspend fun deleteAll()
}
