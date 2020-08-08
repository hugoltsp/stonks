package io.github.hugoltsp.stonks.usecase

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RetrieveSubscriptionsTest {

    val subscriberService = mock<SubscriberService>()
    lateinit var retrieveSubscriptions: RetrieveSubscriptions

    @BeforeEach
    fun setup() {
        retrieveSubscriptions = RetrieveSubscriptions(subscriberService)
    }

    @Test
    fun retrieveAll() {
        retrieveSubscriptions.retrieveAll()
        verify(subscriberService).findSubscriptions()
    }

}