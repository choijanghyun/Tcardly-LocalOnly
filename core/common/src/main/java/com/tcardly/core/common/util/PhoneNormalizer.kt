package com.tcardly.core.common.util

object PhoneNormalizer {
    fun normalize(phone: String): String {
        val digits = phone.replace(Regex("[^0-9+]"), "")
        return when {
            digits.startsWith("+82") -> "0${digits.substring(3)}"
            digits.startsWith("82") -> "0${digits.substring(2)}"
            else -> digits
        }
    }

    fun format(phone: String): String {
        val normalized = normalize(phone)
        return when {
            normalized.length == 11 && normalized.startsWith("010") ->
                "${normalized.substring(0, 3)}-${normalized.substring(3, 7)}-${normalized.substring(7)}"
            normalized.length == 10 ->
                "${normalized.substring(0, 3)}-${normalized.substring(3, 6)}-${normalized.substring(6)}"
            else -> normalized
        }
    }

    fun isValid(phone: String): Boolean {
        val normalized = normalize(phone)
        return normalized.matches(Regex("^01[016789]\\d{7,8}$"))
    }
}
