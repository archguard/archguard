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
enum class AaacMessageType(val contentClass: KClass<out AaacContent>) {
    NONE(NoneContent::class),
    ERROR(ErrorContent::class),
    ARCHGUARD_GRAPH(ArchGuardGraph::class),
    ARCHGUARD_EVOLUTION(ArchguardEvolution::class);

    val type: String get() = name.lowercase()
}

@Serializable
sealed class AaacContent

@Serializable
abstract class MessageReplyContent(val status: DocStatus) : AaacContent()

@Serializable
class NoneContent : MessageReplyContent(DocStatus.ABORT)

@Serializable
class ErrorContent(val exception: String = "", val message: String = "") : AaacContent()

@Serializable
class ArchGuardGraph(val isGraph: Boolean = true, val graphType: String = "") : AaacContent()

@Serializable
class ArchguardEvolution(val actionType: String = "") : AaacContent()

@Serializable
enum class DocStatus {
    @SerialName("ok")
    OK,

    @SerialName("error")
    ERROR,

    @SerialName("abort")
    ABORT;
}

object ArchdocMessageTypeSerializer : KSerializer<AaacMessageType> {
    private val cache: MutableMap<String, AaacMessageType> = hashMapOf()

    private fun getMessageType(type: String): AaacMessageType {
        return cache.computeIfAbsent(type) { newType ->
            AaacMessageType.values().firstOrNull { it.type == newType }
                ?: throw SerializationException("Unknown message type: $newType")
        }
    }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            AaacMessageType::class.qualifiedName!!,
            PrimitiveKind.STRING
        )

    override fun deserialize(decoder: Decoder): AaacMessageType {
        return getMessageType(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: AaacMessageType) {
        encoder.encodeString(value.type)
    }
}
