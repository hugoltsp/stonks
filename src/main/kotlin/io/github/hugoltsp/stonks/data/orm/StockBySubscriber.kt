package io.github.hugoltsp.stonks.data.orm

import org.jetbrains.exposed.sql.Table

object StockBySubscriber : Table("subscriber_has_stock") {

    val stock = (long("stock_id").references(Stocks.id))
    val subscriber = (long("subscriber_id").references(Subscribers.id))

    override val primaryKey = PrimaryKey(stock, subscriber)

}