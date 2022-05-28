package org.archguard.aaac.api.messaging

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KClass

@Serializable(ArchdocMessageTypeSerializer::class)
enum class MessageType(val contentClass: KClass<out MessageContent>) {
    NONE(NoneContent::class),
    ERROR(ErrorContent::class),
    ARCHGUARD_GRAPH(ArchGuardGraph::class),
    ARCHGUARD_EVOLUTION(ArchguardEvolution::class);

    val type: String get() = name.lowercase()
}

@Serializable
sealed class MessageContent

@Serializable
abstract class MessageReplyContent(val status: DocStatus) : MessageContent()

@Serializable
class NoneContent : MessageReplyContent(DocStatus.ABORT)

@Serializable
class ErrorContent(val exception: String = "", val message: String = "") : MessageContent()

@Serializable
class ArchGuardGraph(val graphType: String = "") : MessageContent()

@Serializable
class ArchguardEvolution(val actionType: String = "") : MessageContent()

@Serializable
enum class DocStatus {
    @SerialName("ok")
    OK,

    @SerialName("error")
    ERROR,

    @SerialName("abort")
    ABORT;
}

object ArchdocMessageTypeSerializer : KSerializer<MessageType> {
    private val cache: MutableMap<String, MessageType> = hashMapOf()

    private fun getMessageType(type: String): MessageType {
        return cache.computeIfAbsent(type) { newType ->
            MessageType.values().firstOrNull { it.type == newType }
                ?: throw SerializationException("Unknown message type: $newType")
        }
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        MessageType::class.qualifiedName!!, PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): MessageType {
        return getMessageType(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: MessageType) {
        encoder.encodeString(value.type)
    }
}
