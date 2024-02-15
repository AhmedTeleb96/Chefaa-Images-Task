package com.teleb.chefaaimagestask.domain.utils

import java.math.BigInteger
import java.security.MessageDigest

fun String.EncryptMd5(): String =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16).padStart(32, '0')