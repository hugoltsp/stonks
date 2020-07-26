package io.github.hugoltsp.stonks.usecase.domain

import io.github.hugoltsp.stonks.data.domain.StockVO
import io.github.hugoltsp.stonks.infra.domain.Settings
import java.math.BigDecimal
import java.text.NumberFormat

data class StockOutput(
    val name: String,
    val price: BigDecimal,
    val change: BigDecimal,
    val changePercent: BigDecimal
) {

    override fun toString(): String {

        val formattedPrice = NumberFormat.getCurrencyInstance(Settings.locale).format(price)
        val formattedChange = NumberFormat.getNumberInstance(Settings.locale).format(change)
        val formattedChangePercent = NumberFormat.getNumberInstance(Settings.locale).format(changePercent)

        return "***${name.toUpperCase()}***: $formattedPrice $formattedChange (${formattedChangePercent}%)"
    }

    companion object {

        fun from(stock: StockVO) = StockOutput(stock.name, stock.price, stock.change, stock.changePercent)

    }

}