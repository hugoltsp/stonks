package io.github.hugoltsp.stonks.data.orm

import org.jetbrains.exposed.dao.id.LongIdTable

object Stocks : LongIdTable("stock") {

    val name = Stocks.varchar("name", 255)
    val price = Stocks.decimal("price", 12, 3)
    val change = Stocks.decimal("change", 12, 3)
    val changePercent = Stocks.decimal("change_percent", 12, 3)

}