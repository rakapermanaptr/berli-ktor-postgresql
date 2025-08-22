package com.creospace.models.repository

import com.creospace.models.domain.Account
import com.creospace.models.domain.Event
import com.creospace.models.domain.Report
import com.creospace.models.request.ReportRequest

interface BerliRepository {
    suspend fun getAllReport(): List<Report>
    suspend fun postReport(report: Report)
    suspend fun getReport(id: Int): Report?
    suspend fun getAllEvent(): List<Event>
    suspend fun getEvent(id: Int): Event?
    suspend fun postRegisterAccount(account: Account)
    suspend fun getAccountLogin(username: String, password: String): Account?
}