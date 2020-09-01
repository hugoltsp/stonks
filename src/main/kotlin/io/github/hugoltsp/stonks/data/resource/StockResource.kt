package io.github.hugoltsp.stonks.data.resource

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.github.hugoltsp.stonks.data.domain.StockResponse
import io.github.hugoltsp.stonks.infra.extensions.getLogger

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

                    return StockResponseParser.parse(result.get(), stockIdentifier.toUpperCase())

                } catch (e: Exception) {
                    logger.error("Failed while searching for: [$stockIdentifier], Response [${result.get()}]", e)
                    throw e
                }

            }

        }

    }

}