package io.github.hugoltsp.stonks.usecase

import com.nhaarman.mockitokotlin2.*
import io.github.hugoltsp.stonks.data.domain.StockVO
import io.github.hugoltsp.stonks.data.domain.SubscriberVO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class AddStockToSubscriberTest {

    val stockService: StockService = mock()
    val subscriberService: SubscriberService = mock()

    lateinit var addStockToSubscriber: AddStockToSubscriber

    @BeforeEach
    fun setup() {
        addStockToSubscriber = AddStockToSubscriber(stockService, subscriberService)
    }

    @Test
    fun `subscribes existing user and associate him to an existing stock`() {

        val identifier = "ITUB3"
        val stocks = listOf(identifier)
        val telegramId = 123L
        val subscriberVO = SubscriberVO(telegramId, 1)
        val stockVO = StockVO(
            123L,
            identifier,
            BigDecimal("100"),
            BigDecimal("1"),
            BigDecimal("10")
        )

        whenever(subscriberService.findSubscriberByTelegramId(telegramId)).thenReturn(subscriberVO)
        whenever(stockService.findByName(identifier)).thenReturn(stockVO)

        addStockToSubscriber.add(telegramId, stocks)

        verify(stockService, never()).persist(identifier)
        verify(stockService).findByName(any())
        verify(subscriberService).addStock(subscriberVO.id, stockVO.id)
        verify(subscriberService, never()).save(telegramId)
    }

    @Test
    fun `creates new user and associate him to an existing stock`() {

        val identifier = "ITUB3"
        val stocks = listOf(identifier)
        val telegramId = 123L
        val subscriberVO = SubscriberVO(telegramId, 1)
        val stockVO = StockVO(
            123L,
            identifier,
            BigDecimal("100"),
            BigDecimal("1"),
            BigDecimal("10")
        )

        whenever(subscriberService.findSubscriberByTelegramId(telegramId)).thenReturn(null)
        whenever(subscriberService.save(telegramId)).thenReturn(subscriberVO)
        whenever(stockService.findByName(identifier)).thenReturn(stockVO)

        addStockToSubscriber.add(telegramId, stocks)

        verify(stockService, never()).persist(identifier)
        verify(stockService).findByName(any())
        verify(subscriberService).addStock(subscriberVO.id, stockVO.id)
    }

    @Test
    fun `persists non existent stock`() {

        val stocks = listOf("ITUB3", "ITSA4", "AAA123")
        val telegramId = 123L
        val subscriberVO = SubscriberVO(telegramId, 1)
        val stockVO = StockVO(
            123L,
            "ITUB3",
            BigDecimal("100"),
            BigDecimal("1"),
            BigDecimal("10")
        )

        whenever(subscriberService.findSubscriberByTelegramId(telegramId)).thenReturn(subscriberVO)
        whenever(stockService.findByName("ITUB3")).thenReturn(stockVO)
        whenever(stockService.findByName("ITSA4")).thenReturn(null)
        whenever(stockService.findByName("AAA123")).thenReturn(null)

        addStockToSubscriber.add(telegramId, stocks)

        verify(subscriberService, never()).save(telegramId)
        verify(stockService, times(3)).findByName(any())
        verify(stockService).persist("ITSA4")
        verify(stockService).persist("AAA123")
        verify(stockService, never()).persist("ITUB3")
        verify(subscriberService).addStock(subscriberVO.id, stockVO.id)
    }

}