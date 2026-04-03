package com.example.numberscanner.data.repository

import com.example.numberscanner.data.dao.ScanRecordDao
import com.example.numberscanner.data.model.ScanRecord
import kotlinx.coroutines.flow.Flow

class ScanRepository(private val scanRecordDao: ScanRecordDao) {
    val allRecords: Flow<List<ScanRecord>> = scanRecordDao.getAllRecords()

    suspend fun insert(record: ScanRecord) {
        scanRecordDao.insertRecord(record)
    }

    suspend fun delete(record: ScanRecord) {
        scanRecordDao.deleteRecord(record)
    }

    suspend fun deleteAll() {
        scanRecordDao.deleteAll()
    }
}
