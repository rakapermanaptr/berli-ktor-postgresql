package com.creospace.models.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: Int? = null,
    @SerialName("full_name")
    val fullName: String,
    val username: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    val password: String,
    @SerialName("date_created")
    val dateCreated: String? = null,
)
