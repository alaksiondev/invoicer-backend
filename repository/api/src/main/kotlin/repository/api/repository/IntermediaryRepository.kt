package repository.api.repository

import entities.IntermediaryEntity
import entities.IntermediaryTable
import entities.IntermediaryTable.user
import models.intermediary.CreateIntermediaryModel
import models.intermediary.IntermediaryModel
import models.intermediary.PartialUpdateIntermediaryModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import repository.api.mapper.toModel
import utils.date.impl.DateProvider
import java.util.*

interface IntermediaryRepository {
    suspend fun create(
        userId: UUID,
        model: CreateIntermediaryModel
    ): String

    suspend fun delete(
        userId: UUID,
        intermediaryId: UUID
    )

    suspend fun getById(
        intermediaryId: UUID
    ): IntermediaryModel?

    suspend fun getBySwift(
        userId: UUID,
        swift: String
    ): IntermediaryModel?

    suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): List<IntermediaryModel>

    suspend fun update(
        userId: UUID,
        intermediaryId: UUID,
        model: PartialUpdateIntermediaryModel
    ): IntermediaryModel
}

internal class IntermediaryRepositoryImpl(
    private val dateProvider: DateProvider
) : IntermediaryRepository {

    override suspend fun create(userId: UUID, model: CreateIntermediaryModel): String {
        return newSuspendedTransaction {
            IntermediaryTable.insertAndGetId { table ->
                table[name] = model.name
                table[iban] = model.iban
                table[swift] = model.swift
                table[bankName] = model.bankName
                table[bankAddress] = model.bankAddress
                table[user] = userId
                table[createdAt] = dateProvider.currentInstant()
                table[updatedAt] = dateProvider.currentInstant()
            }.value.toString()
        }
    }

    override suspend fun delete(userId: UUID, intermediaryId: UUID) {
        return newSuspendedTransaction {
            IntermediaryTable.update(
                where = {
                    user.eq(userId).and(IntermediaryTable.id eq intermediaryId)
                }
            ) {
                it[isDeleted] = true
            }
        }
    }

    override suspend fun getById(intermediaryId: UUID): IntermediaryModel? {
        return newSuspendedTransaction {
            IntermediaryEntity.find {
                (IntermediaryTable.id eq intermediaryId) and (IntermediaryTable.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getBySwift(userId: UUID, swift: String): IntermediaryModel? {
        return newSuspendedTransaction {
            IntermediaryEntity.find {
                (user eq userId).and(IntermediaryTable.swift eq swift) and (IntermediaryTable.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int
    ): List<IntermediaryModel> {
        return newSuspendedTransaction {
            val query = IntermediaryTable
                .selectAll()
                .where {
                    user eq userId and (IntermediaryTable.isDeleted eq false)
                }
                .limit(n = limit, offset = page * limit)

            IntermediaryEntity.wrapRows(query)
                .toList()
                .map { it.toModel() }
        }
    }

    override suspend fun update(
        userId: UUID,
        intermediaryId: UUID,
        model: PartialUpdateIntermediaryModel
    ): IntermediaryModel {
        return newSuspendedTransaction {
            IntermediaryTable.updateReturning(
                where = {
                    user eq userId
                    IntermediaryTable.id eq intermediaryId
                }
            ) { table ->
                model.name?.let { table[name] = it }
                model.iban?.let { table[iban] = it }
                model.swift?.let { table[swift] = it }
                model.bankName?.let { table[bankName] = it }
                model.bankAddress?.let { table[bankAddress] = it }
                table[updatedAt] = dateProvider.currentInstant()
            }.map {
                IntermediaryEntity.wrapRow(it)
            }.first().toModel()
        }
    }
}