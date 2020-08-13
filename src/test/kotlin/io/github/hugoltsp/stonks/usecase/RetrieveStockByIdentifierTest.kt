package io.github.hugoltsp.stonks.usecase

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.hugoltsp.stonks.data.domain.StockVO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal.TEN

internal class RetrieveStockByIdentifierTest {

    @Test
    fun `returns valid Stock`() {

        val stockService: StockService = mock()
        val argumentCaptor = argumentCaptor<String>()
        val retrieveStockByIdentifier = RetrieveStockByIdentifier(stockService)

        val stockIdentifier = "ITUB3"
        whenever(stockService.persist(stockIdentifier)).thenReturn(
            StockVO(1, stockIdentifier, TEN, TEN, TEN)
        )

        val stockOutput = retrieveStockByIdentifier.retrieve(stockIdentifier)
        verify(stockService).persist(argumentCaptor.capture())

        assertEquals(stockIdentifier, argumentCaptor.firstValue)
        assertNotNull(stockOutput)
        assertEquals(stockIdentifier, stockOutput?.name)
    }

    @Test
    fun `returns null when no Stock exists`() {
        val stockService: StockService = mock()
        val argumentCaptor = argumentCaptor<String>()
        val retrieveStockByIdentifier = RetrieveStockByIdentifier(stockService)

        val stockIdentifier = "ITUB3"
        whenever(stockService.persist(stockIdentifier)).thenReturn(null)

        val stockOutput = retrieveStockByIdentifier.retrieve(stockIdentifier)
        verify(stockService).persist(argumentCaptor.capture())

        assertEquals(stockIdentifier, argumentCaptor.firstValue)
        assertNull(stockOutput)
    }

}