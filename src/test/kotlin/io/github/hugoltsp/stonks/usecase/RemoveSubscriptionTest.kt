package io.github.hugoltsp.stonks.usecase

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class RemoveSubscriptionTest {

    @Test
    fun `deletes subscription`() {
        val subscriberService: SubscriberService = mock()
        val argumentcCaptor = argumentCaptor<Long>()
        val removeSubscription = RemoveSubscription(subscriberService)

        val telegramChatId = 1L

        removeSubscription.unsubscribe(telegramChatId)
        verify(subscriberService).removeSubscriptionByTelegramChatId(argumentcCaptor.capture())
        assertEquals(telegramChatId, argumentcCaptor.firstValue)
    }

}