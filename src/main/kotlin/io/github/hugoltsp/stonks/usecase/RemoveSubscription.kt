package io.github.hugoltsp.stonks.usecase

import io.github.hugoltsp.stonks.infra.extensions.getLogger

class RemoveSubscription(
    private val subscriberService: SubscriberService = SubscriberService
) {

    fun unsubscribe(telegramChatId: Long) {
        logger.info("Removing user [{}] subscription.", telegramChatId)
        subscriberService.removeSubscriptionByTelegramChatId(telegramChatId)
    }

    private companion object{
        val logger = getLogger()
    }

}