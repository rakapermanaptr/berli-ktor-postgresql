package com.creospace.models.domain

import kotlinx.serialization.Serializable

@Serializable
data class Report(
    val id: Int? = null,
    val username: String,
    val userId: Int,
    val complaint: String,
    val complaintDetails: String,
    val image: String,
    val location: String,
    val locationDetails: String,
    val status: String,
    val likeNumber: Int = 0,
    val dateCreated: String
)