package io.github.hugoltsp.stonks.data.domain

import java.math.BigDecimal

data class StockUpdateCommand(
    val name: String,
    val price: BigDecimal,
    val change: BigDecimal,
    val pricePercent: BigDecimal
) {

    init {
        require(name.isNotBlank()) { "Name cannot be blank." }
    }

}