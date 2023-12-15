package com.my.poc.framework

import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Component
class EncryptionManager {
    @Throws(NoSuchAlgorithmException::class)
    fun encrypt(input: String): String {
        var output = ""
        val sb = StringBuffer()
        val md = MessageDigest.getInstance("SHA-512")
        md.update(input.toByteArray())
        val msgb = md.digest()
        for (i in msgb.indices) {
            val temp = msgb[i]
            var str = Integer.toHexString(temp.toInt() and 0xFF)
            while (str.length < 2) {
                str = "0$str"
            }
            str = str.substring(str.length - 2)
            sb.append(str)
        }
        output = sb.toString()
        return output
    }
}