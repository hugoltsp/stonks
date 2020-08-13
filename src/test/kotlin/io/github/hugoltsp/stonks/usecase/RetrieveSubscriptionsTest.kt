package io.github.hugoltsp.stonks.usecase

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test

internal class RetrieveSubscriptionsTest {

    @Test
    fun retrieveAll() {
        val subscriberService = mock<SubscriberService>()
        val retrieveSubscriptions = RetrieveSubscriptions(subscriberService)

        retrieveSubscriptions.retrieveAll()
        verify(subscriberService).findSubscriptions()
    }

}