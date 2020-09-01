package io.github.hugoltsp.stonks.data.domain

import java.math.BigDecimal

data class StockResponse(
    val name: String,
    val price: BigDecimal,
    val change: BigDecimal,
    val changePercent: BigDecimal
)