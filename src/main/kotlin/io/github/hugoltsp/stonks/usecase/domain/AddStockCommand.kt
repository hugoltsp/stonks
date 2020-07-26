package io.github.hugoltsp.stonks.usecase.domain

data class AddStockCommand(
    val telegramUserId: Long,
    val identifier: String
) {

    init {
        require(identifier.isNotBlank()) { "Stock identifier should not be blank." }
    }

}