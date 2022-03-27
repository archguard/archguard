package com.thoughtworks.archguard.system_info.infrastracture

import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Component
class AESCrypt {
    companion object {
        const val key = "thoughtworks.com"

        fun encrypt(msg: String): String {
            if (msg.isEmpty()) {
                return msg
            }
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            val encrypt = cipher.doFinal(msg.toByteArray())
            return Base64.getEncoder().encodeToString(encrypt)
        }

        fun decrypt(msg: String): String {
            if (msg.isEmpty()) {
                return msg
            }
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val decrypt = cipher.doFinal(Base64.getDecoder().decode(msg))
            return String(decrypt)
        }
    }

}
