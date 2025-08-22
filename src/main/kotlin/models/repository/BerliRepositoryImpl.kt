package com.creospace.models.repository

import com.creospace.models.domain.Account
import com.creospace.models.domain.Event
import com.creospace.models.domain.Report
import com.creospace.models.entity.AccountDAO
import com.creospace.models.entity.AccountTable
import com.creospace.models.entity.EventDAO
import com.creospace.models.entity.EventTable
import com.creospace.models.entity.ReportDAO
import com.creospace.models.entity.ReportTable
import com.creospace.models.entity.accountDaoToModel
import com.creospace.models.entity.eventDaoToModel
import com.creospace.models.entity.reportDaoToModel
import com.creospace.models.request.ReportRequest
import com.creospace.utils.suspendTransaction
import org.jetbrains.exposed.sql.and
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BerliRepositoryImpl: BerliRepository {

    override suspend fun getAllReport(): List<Report> = suspendTransaction {
        ReportDAO.all().map(::reportDaoToModel)
    }

    override suspend fun postReport(report: Report): Unit = suspendTransaction {
        ReportDAO.new {
            username = report.username
            userId = report.userId
            complaint = report.complaint
            complaintDetail = report.complaintDetails
            image = report.image
            location = report.location
            locationDetail = report.locationDetails
            status = report.status
            likeNumber = report.likeNumber
            dateCreated = report.dateCreated
        }
    }

    override suspend fun getReport(id: Int): Report? = suspendTransaction {
        ReportDAO
            .find { (ReportTable.id eq id) }
            .limit(1)
            .map(::reportDaoToModel)
            .firstOrNull()
    }

    override suspend fun getAllEvent(): List<Event> = suspendTransaction {
        EventDAO.all().map(::eventDaoToModel)
    }

    override suspend fun getEvent(id: Int): Event? = suspendTransaction {
        EventDAO
            .find { (EventTable.id eq id) }
            .limit(1)
            .map(::eventDaoToModel)
            .firstOrNull()
    }

    override suspend fun postRegisterAccount(account: Account): Unit = suspendTransaction  {
        AccountDAO.new {
            fullName = account.fullName
            username = account.username
            phoneNumber = account.phoneNumber
            password = account.password
            dateCreated = account.dateCreated ?: LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        }
    }

    override suspend fun getAccountLogin(
        username: String,
        password: String
    ): Account? = suspendTransaction {
        AccountDAO
            .find { (AccountTable.username eq username) and (AccountTable.password eq password) }
            .firstOrNull()
            ?.let(::accountDaoToModel)
    }
}