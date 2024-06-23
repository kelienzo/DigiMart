package com.digimart.utils

import android.content.Context
import android.widget.Toast
import com.digimart.ui.theme.Color_Green
import com.digimart.ui.theme.Color_Magenta
import com.digimart.ui.theme.Color_Red
import com.digimart.ui.theme.Color_Yellow
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun String.formatAmount(showNaira: Boolean = false): String {
    val numberFormatter = NumberFormat.getInstance().apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    val formattedAmount = numberFormatter.format(
        this.replace(
            "[,-]".toRegex(),
            ""
        ).toDouble()
    )
    return if (showNaira) "₦$formattedAmount" else formattedAmount
}

val String.formatNumber: String
    get() {
        val numberFormatter = NumberFormat.getInstance()
        return numberFormatter.format(
            this.replace(
                "[,-]".toRegex(),
                ""
            ).toDouble()
        )
    }

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun String.getStatusColor() = when (this) {
    SaleStatus.SOLD.name -> Color_Green
    SaleStatus.PENDING.name -> Color_Magenta
    else -> Color_Red
}

fun getDate(longDate: Long): String {
    return SimpleDateFormat(
        "E dd MMM, yyyy HH:mm",
        Locale.getDefault()
    ).format(longDate)
}