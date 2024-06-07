package com.github.aakumykov.ktor_server

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val dateTime: String = SimpleDateFormat("hhч mmм ssс, dd.MM.yy", Locale.getDefault()).format(Date())