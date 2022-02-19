package com.thoughtworks.archguard.scanner.infrastructure

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class AESCrypt() {
    companion object {
        const val key: String = "thoughtworks.com"
        fun encrypt(msg: String): String {
            if (msg.isEmpty()) {
                return msg
            }
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            val encrypt = cipher.doFinal(msg.toByteArray());
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
