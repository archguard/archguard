package org.archguard.architecture.layered

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
