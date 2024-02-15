package com.teleb.chefaaimagestask.domain.utils

import java.math.BigInteger
import java.security.MessageDigest

fun String.encryptMd5(): String =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16).padStart(32, '0')
fun String.toHttps(): String = if (startsWith("http://")) "https${substring(4)}"
else this