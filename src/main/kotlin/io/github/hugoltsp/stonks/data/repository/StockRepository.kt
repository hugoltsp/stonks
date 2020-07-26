package io.github.hugoltsp.stonks.data.repository

import io.github.hugoltsp.stonks.data.domain.NewStockCommand
import io.github.hugoltsp.stonks.data.domain.StockVO
import io.github.hugoltsp.stonks.data.orm.Stocks
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object StockRepository {

    fun findByName(name: String) = StockDAO.find { Stocks.name eq name }.firstOrNull()
        ?.toVo()

    fun save(newStockCommand: NewStockCommand): StockVO {

        val stock = StockDAO.find { Stocks.name eq newStockCommand.name }.firstOrNull()?.apply {
            change = newStockCommand.change
            price = newStockCommand.price
            pricePercent = newStockCommand.pricePercent
        } ?: StockDAO.new {
            name = newStockCommand.name
            price = newStockCommand.price
            change = newStockCommand.change
            pricePercent = newStockCommand.pricePercent
        }

        return stock.toVo()
    }


}

internal class StockDAO(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<StockDAO>(Stocks)

    var name by Stocks.name
    var price by Stocks.price
    var change by Stocks.change
    var pricePercent by Stocks.changePercent

    fun toVo() = StockVO(id.value, name, price, change, pricePercent)

}
