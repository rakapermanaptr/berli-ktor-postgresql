package com.creospace.models.domain

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Int,
    val name: String,
    val details: String,
    val location: String,
    val date: String
)