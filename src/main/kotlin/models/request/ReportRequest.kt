package com.creospace.models.request

import kotlinx.serialization.SerialName

data class ReportRequest(
    val id: Int? = null,
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
