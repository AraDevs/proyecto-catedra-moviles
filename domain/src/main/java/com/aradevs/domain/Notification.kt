package com.aradevs.domain

import java.util.*

data class Notification(
    val id: Int,
    val medicineId: Int,
    val name: String,
    val startDate: Date,
    val repeatInterval: String,
    val current: Boolean,
    val endDate: Date?,
)
