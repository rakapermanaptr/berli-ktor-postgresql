package com.creospace.models.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Report(
    val id: Int,
    val username: String,
    @SerialName("user_id")
    val userId: Int,
    val complaint: String,
    @SerialName("complaint_details")
    val complaintDetails: String,
    val image: String,
    val location: String,
    @SerialName("location_details")
    val locationDetails: String,
    val status: String,
    @SerialName("like_number")
    val likeNumber: Int = 0,
    @SerialName("date_created")
    val dateCreated: String
)