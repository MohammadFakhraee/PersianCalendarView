package com.maddev.persiancalendarview.utils

object PersianHelper {
    private val persianNumbers = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    private val englishNumbers = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    fun toPersianNumber(text: String): String {
        if (text.isEmpty()) return ""
        val out = StringBuilder()
        val length = text.length
        for (i in 0 until length) {
            when (val c = text[i]) {
                in '0'..'9' -> out.append(persianNumbers[c.toString().toInt()])
                '٫' -> out.append('،')
                else -> out.append(c)
            }
        }
        return out.toString()
    }

    fun toEnglishNumber(text: String): String {
        if (text.isEmpty()) return ""
        val out = java.lang.StringBuilder()
        val length = text.length
        for (i in 0 until length) {
            val c = text[i]
            var charPos: Int
            if (hasCharacter(c).also { charPos = it } != -1) out.append(englishNumbers[charPos])
            else if (c == '،') out.append('٫')
            else out.append(c)
        }
        return out.toString()
    }

    private fun hasCharacter(c: Char): Int {
        for (i in persianNumbers.indices) {
            if (c == persianNumbers[i]) {
                return i
            }
        }
        return -1
    }
}