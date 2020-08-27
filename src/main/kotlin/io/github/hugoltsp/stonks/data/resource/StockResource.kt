package io.github.hugoltsp.stonks.data.resource

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.github.hugoltsp.stonks.infra.extensions.getLogger
import org.jsoup.Jsoup
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.log

object StockResource {

    private const val GOOGLE_URL = "https://www.google.com.br/search"
    private const val USER_AGENT =
        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.7) Gecko/2009021910 Firefox/3.0.7"
    private val logger = getLogger()

    fun get(stockIdentifier: String): StockResponse? {

        logger.info("Searching for [{}] on google.", stockIdentifier)

        val (_, _, result) = GOOGLE_URL
            .httpGet(listOf("q" to stockIdentifier))
            .header(mapOf("User-Agent" to USER_AGENT))
            .responseString()

        when (result) {
            is Result.Failure -> {
                logger.error("Could not find $stockIdentifier", result.getException())
                return null
            }
            else -> {
                try {
                    val document = Jsoup.parse(result.get())
                    val elementsByTag = document.getElementsByTag("span")
                    val filter = elementsByTag.findLast { "Preço das ações" == it.text() }
                    val element = filter?.parent()?.parent()?.lastElementSibling()
                    val text = element?.text()!!
                    val rawText = text.dropLast(text.length - text.indexOf(stockIdentifier.toUpperCase()))
                        .trim()
                        .split(" ")

                    return StockResponse(
                        stockIdentifier.toUpperCase(),
                        parseValue(rawText[0]),
                        parseChange(rawText[1]),
                        parsePercent(rawText[2])
                    )
                } catch (e: Exception) {
                    logger.error("Failed while searching for: [$stockIdentifier]", e)
                    logger.error("HTML {$result.get()}")
                    throw e
                }
            }
        }

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

    data class StockResponse(
        val name: String,
        val price: BigDecimal,
        val change: BigDecimal,
        val changePercent: BigDecimal
    )

}