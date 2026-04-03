package com.example.numberscanner.ui.viewmodel

import com.example.numberscanner.data.dao.ScanRecordDao
import com.example.numberscanner.data.model.ScanRecord
import com.example.numberscanner.data.repository.ScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelsTest {
    private lateinit var repository: ScanRepository
    private lateinit var dao: ScanRecordDao
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dao = mock(ScanRecordDao::class.java)
        repository = ScanRepository(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ScannerViewModel saveRecord calls repository insert`() = runTest {
        val viewModel = ScannerViewModel(repository)
        viewModel.onNumberScanned("12345")
        viewModel.onClassificationChanged("Asset")
        viewModel.onNotesChanged("Some notes")

        viewModel.saveRecord()
        advanceUntilIdle()

        verify(dao).insertRecord(any(ScanRecord::class.java))
        assertEquals("", viewModel.uiState.value.scannedNumber)
        assertEquals(true, viewModel.uiState.value.saveSuccess)
    }
}

private fun <T> any(type: Class<T>): T = any()
