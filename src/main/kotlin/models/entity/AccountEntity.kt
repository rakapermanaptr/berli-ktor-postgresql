package com.creospace.models.entity

import com.creospace.models.domain.Account
import com.creospace.models.domain.Report
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

fun accountDaoToModel(dao: AccountDAO) = Account(
    id = dao.id.value,
    fullName = dao.fullName,
    username = dao.username,
    phoneNumber = dao.phoneNumber,
    password = dao.password,
    dateCreated = dao.dateCreated
)

object AccountTable : IntIdTable("account") {
    val fullName = varchar("full_name", 100)
    val username = varchar("username", 50)
    val phoneNumber = varchar("phone_number", 12)
    val password = varchar("password", 50)
    val dateCreated = varchar("date_created", 100)
}

class AccountDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<AccountDAO>(AccountTable)

    var fullName by AccountTable.fullName
    var username by AccountTable.username
    var phoneNumber by AccountTable.phoneNumber
    var password by AccountTable.password
    var dateCreated by AccountTable.dateCreated
}