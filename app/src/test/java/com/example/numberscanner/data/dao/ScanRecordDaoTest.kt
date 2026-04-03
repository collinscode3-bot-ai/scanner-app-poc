package com.example.numberscanner.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.numberscanner.data.database.AppDatabase
import com.example.numberscanner.data.model.ScanRecord
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ScanRecordDaoTest {
    private lateinit var scanRecordDao: ScanRecordDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        scanRecordDao = db.scanRecordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeRecordAndReadInList() = runBlocking {
        val record = ScanRecord(serialNumber = "12345", classification = "Test", notes = "Notes")
        scanRecordDao.insertRecord(record)
        val allRecords = scanRecordDao.getAllRecords().first()
        assertEquals(allRecords[0].serialNumber, record.serialNumber)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllRecords() = runBlocking {
        val record = ScanRecord(serialNumber = "12345", classification = "Test", notes = "Notes")
        scanRecordDao.insertRecord(record)
        scanRecordDao.deleteAll()
        val allRecords = scanRecordDao.getAllRecords().first()
        assertTrue(allRecords.isEmpty())
    }
}
