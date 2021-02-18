package kz.eztech.stylyts.presentation.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.DAY_OF_YEAR
import java.util.Calendar.YEAR


object DateFormatterHelper {
    sealed class AdditionalTime{
        data class Seconds(val value:Int):AdditionalTime()
        data class Minutes(val value:Int):AdditionalTime()
        data class  Hour(val value:Int):AdditionalTime()
        data class  Day(val value:Int):AdditionalTime()
        data class  Month(val value:Int):AdditionalTime()
        data class  Year(val value:Int):AdditionalTime()
    }
    const val FORMAT_DATE_D_MMMM_yyyy = "d MMMM yyyy"
    const val FORMAT_DATE_HH_mm_D_MMMM_yyyy = "HH:mm d MMMM yyyy"
    const val FORMAT_DATE_D_MMMM_yyyy_HH_mm = "d MMMM yyyy / HH:mm "
    const val FORMAT_DATE_DD_MMMM = "dd MMMM"
    const val FORMAT_DATE_HH_mm = "HH:mm"
    const val FORMAT_DATE_HH_mm_ss = "HH:mm:ss"
    const val FORMAT_DATE_dd_MM_yyyy_dots = "dd.MM.yyyy"
    const val FORMAT_DATE_dd_MM_yyyy_dash = "dd MM yyyy"
    const val FORMAT_DATE_HH_mm_dd_MM_yyyy_dots_slash = "HH:mm / dd.MM.yyyy"
    const val FORMAT_DATE_HH_mm_dd_MM_yyyy_dash = "HH:mm dd MM yyyy"
    
    const val DATE_FORMAT_PATTERN =  "yyyy-MM-dd'T'HH:mm:ss"
    
    @JvmStatic fun formatISO_8601(date: Date?, timePatter: String ): String {
        return try{
            SimpleDateFormat(timePatter,Locale("ru")).format(date) ?: "Нет данных"
        }catch (e:Exception){
            "Нет данных"
        }
    }
    
    @JvmStatic fun formatISO_8601(time: String?, timePatter: String ): String {
        return try{
            val isoFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)
            val date = isoFormat.parse(time)
            SimpleDateFormat(timePatter,Locale("ru")).format(date) ?: "Нет данных"
        }catch (e:Exception){
            "Нет данных"
        }
    }
    @JvmStatic fun formatISO_8601_with_additional(time: String, timePatter: String,additional: AdditionalTime): String {
        return try{
            val isoFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)
            val date = isoFormat.parse(time)
            val cal = Calendar.getInstance()
            cal.time = date
            when(additional){
                is AdditionalTime.Seconds -> {
                    cal.add(Calendar.SECOND, additional.value)
                }
                is AdditionalTime.Minutes -> {
                    cal.add(Calendar.MINUTE, additional.value)
                }
                is AdditionalTime.Hour -> {
                    cal.add(Calendar.HOUR_OF_DAY, additional.value)
                }
                is AdditionalTime.Day -> {
                    cal.add(Calendar.DAY_OF_YEAR, additional.value)
                }
                is AdditionalTime.Month -> {
                    cal.add(Calendar.MONTH, additional.value)
                }
                is AdditionalTime.Year -> {
                    cal.add(Calendar.YEAR, additional.value)
                }
            }
            
            SimpleDateFormat(timePatter,Locale("ru")).format(cal.time) ?: "Нет данных"
        }catch (e:Exception){
            "Нет данных"
        }
    }
    @JvmStatic fun isSameDate(firstDateS:String,secondDateS:String):Boolean{
        return try{
            val firstIsoFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)
            val secondIsoFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)
    
            val firstDateD = firstIsoFormat.parse(firstDateS)
            val firstDate = SimpleDateFormat(DATE_FORMAT_PATTERN).format(firstDateD)
    
            val secondDateD = secondIsoFormat.parse(secondDateS)
            val secondDate = SimpleDateFormat(DATE_FORMAT_PATTERN).format(secondDateD)
    
            return firstDate == secondDate
        }catch (e:Exception){
            false
        }
    }
    @JvmStatic fun isCurrentDay(time: String?):Boolean{
        return try{
            val currentCalendar = Calendar.getInstance()
            
            val isoFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)
            val date = isoFormat.parse(time)
            val targetCalendar = Calendar.getInstance()
            targetCalendar.time = date
            
            return currentCalendar[DAY_OF_YEAR] == targetCalendar[DAY_OF_YEAR]
                    && currentCalendar[YEAR] == targetCalendar[YEAR]
        }catch (e:Exception){
            false
        }
    }
    
    @SuppressLint("SimpleDateFormat")
    fun formatCohortDate(): String {
        val df = SimpleDateFormat("yyyyMMdd")
        return df.format(Calendar.getInstance().time)
    }
    
    fun formatCohortDay(): String {
        val df = SimpleDateFormat("DD")
        return df.format(Calendar.getInstance().time)
    }
    
    
}