package com.creospace.models.entity

import com.creospace.models.domain.Event
import org.jetbrains.exposed.dao.DaoEntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.IntIdTable

fun eventDaoToModel(dao: EventDAO) = Event(
    id = dao.id.value,
    name = dao.name,
    details = dao.details,
    location = dao.location,
    date = dao.date
)

object EventTable : IntIdTable("event") {
    val name = varchar("name", 50)
    val details = varchar("details", 50)
    val location = varchar("location", 50)
    val date = varchar("date", 50)
}

class EventDAO(id: DaoEntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EventDAO>(EventTable)

    var name by EventTable.name
    var details by EventTable.details
    var location by EventTable.location
    var date by EventTable.date
}