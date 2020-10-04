package com.manoloscorp.livinother.utils

import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

open class FormatValues {

    companion object{

        fun formatMoneyText(value: Double): String {
            val ptBr = Locale("pt", "BR")
            return NumberFormat.getCurrencyInstance(ptBr).format(value)
        }

        fun formatTextoMes(mes: Int): String {
            val local = Locale("pt", "BR")
            val monthDate = SimpleDateFormat("MMM", local)
            val sdf = SimpleDateFormat("MM", local)

            val date = sdf.parse(mes.toString());
            val mes = monthDate.format(date);
            return mes.toUpperCase()
        }

        fun formatBirthdayDate(date: String): String? {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            var convertedDate: Date? = null
            var formattedDate: String? = null
            try {
                convertedDate = sdf.parse(date)
                formattedDate = SimpleDateFormat("yyyy-MM-dd").format(convertedDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return formattedDate
        }
    }


}