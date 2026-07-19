package com.dsagent.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.formatTimestamp(): String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this))
