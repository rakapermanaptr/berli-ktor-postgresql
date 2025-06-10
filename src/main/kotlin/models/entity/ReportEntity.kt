package com.creospace.models.entity

import com.creospace.models.domain.Report
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

fun reportDaoToModel(dao: ReportDAO) = Report(
    id = dao.id.value,
    username = dao.username,
    userId = dao.userId,
    complaint = dao.complaint,
    complaintDetails = dao.complaintDetail,
    image = dao.image,
    location = dao.location,
    locationDetails = dao.locationDetail,
    status = dao.status,
    likeNumber = dao.likeNumber,
    dateCreated = dao.dateCreated
)

object ReportTable : IntIdTable("report") {
    val username = varchar("username", 50)
    val userId = integer("userId")
    val complaint = varchar("complaint", 100)
    val complaintDetail = varchar("complaintDetail", 300)
    val image = varchar("image", 500)
    val location = varchar("location", 300)
    val locationDetail = varchar("locationDetail", 100)
    val status = varchar("status", 20)
    val likeNumber = integer("likeNumber")
    val dateCreated = varchar("dateCreated", 100)
}

class ReportDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<ReportDAO>(ReportTable)

    var username by ReportTable.username
    var userId by ReportTable.userId
    var complaint by ReportTable.complaint
    var complaintDetail by ReportTable.complaintDetail
    var image by ReportTable.image
    var location by ReportTable.location
    var locationDetail by ReportTable.locationDetail
    var status by ReportTable.status
    var likeNumber by ReportTable.likeNumber
    var dateCreated by ReportTable.dateCreated
}