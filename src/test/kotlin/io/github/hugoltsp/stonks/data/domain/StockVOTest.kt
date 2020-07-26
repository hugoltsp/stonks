package io.github.hugoltsp.stonks.data.domain

import io.github.hugoltsp.stonks.infra.extensions.nowAtDefaultTimeZone
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal.TEN

internal class StockVOTest {

    @Test
    fun shouldReturnMinutesElapsedSinceLastUpdate() {

        // GIVEN
        val stock = StockVO(
            id = 1L,
            name = "ABC",
            change = TEN,
            lastUpdatedAt = nowAtDefaultTimeZone().minusHours(1),
            price = TEN,
            changePercent = TEN
        )

        // WHEN
        val timeElapsedSinceLastUpdate = stock.minutesElapsedSinceLastUpdate()

        // THEN
        assertEquals(60, timeElapsedSinceLastUpdate)
    }

}