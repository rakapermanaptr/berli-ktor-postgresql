package com.creospace.models.domain

import kotlinx.serialization.Serializable

@Serializable
data class Report(
    val id: Int? = null,
    val report: String,
    val reportDetails: String,
    val location: String,
    val reporter: String,
)