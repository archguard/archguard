package org.archguard.scanner.architecture.biz

/**
 * The `ChannelType` class is an enum class that represents different types of communication channels.
 * Each channel type has a display name associated with it.
 *
 * The `ChannelType` class also provides a companion object with a single method `allValues()`.
 * This method returns a list of all the display names of the available channel types.
 */
enum class ChannelType(val displayName: String) {
    WEBSITE("Website"),
    MOBILE_APP("Mobile App"),
    PHONE("Phone"),
    EMAIL("Email"),
    SOCIAL_MEDIA("Social Media"),
    ONLINE_CHAT("Online Chat"),
    IN_PERSON_STORE("In-person Store"),
    SELF_SERVICE_KIOSK("Self-service Kiosk"),
    VOICE_ASSISTANT("Voice Assistant"),
    VIDEO_CONFERENCING("Video Conferencing"),
    SMS("SMS"),
    FAX("Fax"),
    MAIL("Mail"),
    INTERACTIVE_VOICE_RESPONSE("Interactive Voice Response"),
    VIRTUAL_REALITY("Virtual Reality"),
    AUGMENTED_REALITY("Augmented Reality");

    companion object {
        fun allValues(): List<String> {
            return values().map { it.displayName }
        }
    }
}
