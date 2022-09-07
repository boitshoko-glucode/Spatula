package com.boitshoko.spatula.utils

import java.text.SimpleDateFormat
import java.util.*

class Constants {
    companion object {
        const val API_KEY = "b29878f198114c7dba7af9db2ab185e2"
        const val BASE_URL = "https://api.spoonacular.com"
        const val SEARCH_TIME_DELAY = 500L
        const val QUERY_PAGE_SIZE = 20
    }
}


object DateTimeUtils {
    private val yyyy_MM_dd_HH_mm: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm",  Locale.ENGLISH)
    private val HHmm: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    private val MM_dd_HHmm: SimpleDateFormat = SimpleDateFormat("MM-dd HH:mm",  Locale.ENGLISH)

    fun date2DayTime(oldTime: Date?): String? {
        val newTime = Date()
        try {
            val cal: Calendar = Calendar.getInstance()
            cal.time = newTime
            val oldCal: Calendar = Calendar.getInstance()
            if (oldTime != null) {
                oldCal.time = oldTime
            }
            val oldYear: Int = oldCal.get(Calendar.YEAR)
            val year: Int = cal.get(Calendar.YEAR)
            val oldDay: Int = oldCal.get(Calendar.DAY_OF_YEAR)
            val day: Int = cal.get(Calendar.DAY_OF_YEAR)
            if (oldTime != null){
                if (oldYear == year) {
                    return when (oldDay - day) {
                        -1 -> {
                            "yesterday " + oldTime.let { HHmm.format(it) }
                        }
                        0 -> {
                            "today " + oldTime.let { HHmm.format(it) }
                        }
                        1 -> {
                            "tomorrow " + oldTime.let { HHmm.format(it) }
                        }
                        else -> {
                            MM_dd_HHmm.format(oldTime)
                        }
                    }
                }
            }

        } catch (e: Exception) {
        }
        return oldTime?.let { yyyy_MM_dd_HH_mm.format(it) }
    }
}