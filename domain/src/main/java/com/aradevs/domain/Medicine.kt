package com.aradevs.domain

import java.util.*

data class Medicine(
    val id: Int,
    val name: String,
    val doctorName: String,
    val startDate: Date,
    val repeatInterval: String,
    val color: String,
)
