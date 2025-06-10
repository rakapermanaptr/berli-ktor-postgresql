package com.creospace.models.repository

import com.creospace.models.domain.Event
import com.creospace.models.domain.Report
import com.creospace.models.entity.EventDAO
import com.creospace.models.entity.EventTable
import com.creospace.models.entity.ReportDAO
import com.creospace.models.entity.ReportTable
import com.creospace.models.entity.eventDaoToModel
import com.creospace.models.entity.reportDaoToModel
import com.creospace.utils.suspendTransaction

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
}