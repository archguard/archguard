package com.thoughtworks.archguard.unit

import com.thoughtworks.archguard.project_info.infrastracture.AESCrypt
import org.junit.Test
import kotlin.test.assertEquals

class AESCryptTest {
    private val msg = "admin123456"
    private val encrypt = "WCA5RH/O9J4yxgU40Z+thg=="

    @Test
    fun should_encrypt_success() {
        val result = AESCrypt.encrypt(msg)
        assertEquals(result, encrypt)
    }

    @Test
    fun should_decrypt_success() {
        val result = AESCrypt.decrypt(encrypt)
        assertEquals(result, msg)
    }
}
