package com.example.musicapp.utils

const val ZERO = 0
const val MILI_PER_SEC = 1000
const val SEC_PER_MINUTE = 60

class SimpleTimeFormat private constructor() {
    companion object {
        fun format(i: Int): String? {
            val sum = i / MILI_PER_SEC

            val m = sum / SEC_PER_MINUTE
            val s = sum - m * SEC_PER_MINUTE

            val mm = if (m < 10) {
                "$ZERO$m"
            } else {
                m.toString()
            }

            val ss = if (s < 10) {
                "$ZERO$s"
            } else {
                s.toString()
            }

            return "$mm:$ss"
        }
    }
}
