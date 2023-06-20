package com.devs.imgur.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDDMMYYhhmmaa(): String{
    val sdf= SimpleDateFormat("DD/MM/YY hh:mm aa", Locale.getDefault())
    return sdf.format(Date(this))
}