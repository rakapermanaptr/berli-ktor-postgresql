package com.creospace.models.entity

import com.creospace.models.domain.Report
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

fun reportDaoToModel(dao: ReportDAO) = Report(
    id = dao.id.value,
    report = dao.report,
    reportDetails = dao.reportDetails,
    reporter = dao.reporter,
    location = dao.location
)

object ReportTable : IntIdTable("report") {
    val report = varchar("report", 100)
    val reportDetails = varchar("reportDetails", 300)
    val reporter = varchar("reporter", 100)
    val location = varchar("location", 100)
}

class ReportDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<ReportDAO>(ReportTable)

    var report by ReportTable.report
    var reportDetails by ReportTable.reportDetails
    var reporter by ReportTable.reporter
    var location by ReportTable.location
}