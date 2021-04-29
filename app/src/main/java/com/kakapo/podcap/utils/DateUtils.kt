package com.kakapo.podcap.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun jsonDateToShortDate(jsonDate: String?): String{
        return if (jsonDate == null){
            "-"
        }else{
            val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = inFormat.parse(jsonDate)
            val outputFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
            outputFormat.format(date!!)
        }
    }
}