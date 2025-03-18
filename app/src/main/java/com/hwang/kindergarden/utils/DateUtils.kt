package com.hwang.kindergarden.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {

    fun getCurrentDateString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date()) // 현재 날짜를 "yyyy-MM-dd" 형식의 문자열로 반환
    }

    fun dateStringToEpochMilli(dateString: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC") // UTC 기준으로 변환
        val date = dateFormat.parse(dateString) // String → Date 변환
        return date?.time ?: 0L // Date → Epoch Milliseconds 변환
    }
}