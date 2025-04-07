package io.github.alaksion.invoicer.foundation.messaging.kafka

import foundation.secrets.SecretKeys
import foundation.secrets.SecretsProvider
import io.github.alaksion.invoicer.foundation.messaging.MessageConsumer
import io.github.alaksion.invoicer.foundation.messaging.MessageTopic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

internal class KafkaConsumer(
    private val secrets: SecretsProvider,
    private val coroutineScope: CoroutineScope
) : MessageConsumer {

    private val _messageStream = MutableSharedFlow<String>()
    override val messageStream: SharedFlow<String> = _messageStream

    private var messagingJob: Job? = null

    private val consumer by lazy {
        val properties = Properties()
        properties["bootstrap.servers"] = secrets.getSecret(SecretKeys.KAFKA_BOOTSTRAP)
        properties["key.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        properties["value.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        properties["group.id"] = "invoicer-service-group"
        properties["enable.auto.commit"] = "false"
        KafkaConsumer<String, String>(properties).apply {
            subscribe(MessageTopic.entries.map { it.topicId })
        }
    }

    override suspend fun startConsuming() {
        messagingJob = coroutineScope.launch {
            while (true) {
                val records = consumer.poll(1.seconds.toJavaDuration())
                for (record in records) {
                    _messageStream.emit(record.value())
                    println("Received message: ${record.value()} from topic: ${record.topic()}")
                    consumer.commitSync()
                }
            }
        }
    }

    override fun close() {
        messagingJob?.cancel()
        consumer.close()
    }
}