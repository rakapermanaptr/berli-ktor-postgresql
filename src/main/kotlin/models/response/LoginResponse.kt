package com.creospace.models.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val username: String,
    val fullName: String,
    val phoneNumber: String
)
