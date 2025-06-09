package com.creospace.utils

import models.domain.BaseResponse

fun <T> successResponse(data: T, message: String = "Success") =
    BaseResponse(success = true, message = message, data = data)

fun errorResponse(message: String) =
    BaseResponse<Unit>(success = false, message = message, data = null)
