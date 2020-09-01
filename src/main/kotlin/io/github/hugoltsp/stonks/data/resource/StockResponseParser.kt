package io.github.hugoltsp.stonks.data.resource

import io.github.hugoltsp.stonks.data.domain.StockResponse
import org.jsoup.Jsoup
import java.math.BigDecimal
import java.math.RoundingMode

object StockResponseParser {

    fun parse(response: String, stockIdentifier: String): StockResponse {
        val document = Jsoup.parse(response)
        val elementsByTag = document.getElementsByTag("span")
        val filter = elementsByTag.findLast { "Preço das ações" == it.text() || "Stock Price" == it.text() }
        val element = filter?.parent()?.parent()?.lastElementSibling()
        val text = element?.text()!!
        val rawText = text.dropLast(text.length - text.indexOf(stockIdentifier))
            .trim()
            .split(" ")

        return StockResponse(
            stockIdentifier,
            parseValue(rawText[0]),
            parseChange(rawText[1]),
            parsePercent(rawText[2])
        )
    }

    private fun parseChange(value: String) = if (value.contains("-")) {
        parseValue(value.removePrefix("-")).negate()
    } else {
        parseValue(value.removePrefix("+"))
    }

    private fun parsePercent(value: String) =
        parseValue(
            value.removePrefix("(")
                .removeSuffix(")")
                .replace("%", "")
        )

    private fun parseValue(value: String) = BigDecimal(value.replace(",", "."))
        .setScale(2, RoundingMode.HALF_UP)

}