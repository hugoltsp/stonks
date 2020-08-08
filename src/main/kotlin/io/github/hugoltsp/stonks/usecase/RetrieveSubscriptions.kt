package io.github.hugoltsp.stonks.usecase

class RetrieveSubscriptions(private val service: SubscriberService = SubscriberService()) {

    fun retrieveAll() = service.findSubscriptions()

}