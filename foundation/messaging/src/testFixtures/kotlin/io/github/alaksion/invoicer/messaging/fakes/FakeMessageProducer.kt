package io.github.alaksion.invoicer.messaging.fakes

import io.github.alaksion.invoicer.foundation.messaging.MessageProducer
import io.github.alaksion.invoicer.foundation.messaging.MessageTopic

class FakeMessageProducer : MessageProducer {

    var calls = 0
        private set

    val messages = mutableListOf<Triple<MessageTopic, String, String>>()

    override suspend fun produceMessage(topic: MessageTopic, key: String, value: String) {
        calls++
        messages.add(Triple(topic, key, value))
    }
}