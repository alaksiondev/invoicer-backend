package repository.api.repository

import entities.BeneficiaryEntity
import entities.BeneficiaryTable
import models.beneficiary.BeneficiaryModel
import models.beneficiary.CreateBeneficiaryModel
import models.beneficiary.PartialUpdateBeneficiaryModel
import models.beneficiary.UserBeneficiaries
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import repository.api.mapper.toModel
import utils.date.impl.DateProvider
import java.util.*

interface BeneficiaryRepository {
    suspend fun create(
        userId: UUID,
        model: CreateBeneficiaryModel
    ): String

    suspend fun delete(
        userId: UUID,
        beneficiaryId: UUID
    )

    suspend fun getById(
        beneficiaryId: UUID
    ): BeneficiaryModel?

    suspend fun getBySwift(
        userId: UUID,
        swift: String
    ): BeneficiaryModel?

    suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): UserBeneficiaries

    suspend fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: PartialUpdateBeneficiaryModel
    ): BeneficiaryModel
}

internal class BeneficiaryRepositoryImpl(
    private val dateProvider: DateProvider
) : BeneficiaryRepository {

    override suspend fun create(userId: UUID, model: CreateBeneficiaryModel): String {
        return newSuspendedTransaction {
            BeneficiaryTable.insertAndGetId { table ->
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

    override suspend fun delete(userId: UUID, beneficiaryId: UUID) {
        return newSuspendedTransaction {
            BeneficiaryTable.update(
                where = {
                    BeneficiaryTable.user.eq(userId).and(BeneficiaryTable.id eq beneficiaryId)
                }
            ) {
                it[isDeleted] = true
            }
        }
    }

    override suspend fun getById(beneficiaryId: UUID): BeneficiaryModel? {
        return newSuspendedTransaction {
            BeneficiaryEntity.find {
                (BeneficiaryTable.id eq beneficiaryId) and (BeneficiaryTable.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getBySwift(userId: UUID, swift: String): BeneficiaryModel? {
        return newSuspendedTransaction {
            BeneficiaryEntity.find {
                (BeneficiaryTable.user eq userId).and(BeneficiaryTable.swift eq swift) and (BeneficiaryTable.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): UserBeneficiaries {
        return newSuspendedTransaction {
            val query = BeneficiaryTable
                .selectAll()
                .where {
                    BeneficiaryTable.user eq userId and (BeneficiaryTable.isDeleted eq false)
                }
                .limit(n = limit, offset = page * limit)

            val count = query.count()
            val currentOffset = page * limit

            val nextPage = if (count > currentOffset) {
                (count - currentOffset) / limit
            } else {
                null
            }

            val result = BeneficiaryEntity.wrapRows(query)
                .toList()
                .map { it.toModel() }

            UserBeneficiaries(
                items = result,
                nextPage = nextPage,
                itemCount = count
            )
        }
    }

    override suspend fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: PartialUpdateBeneficiaryModel
    ): BeneficiaryModel {
        return newSuspendedTransaction {
            BeneficiaryTable.updateReturning(
                where = {
                    BeneficiaryTable.user eq userId
                    BeneficiaryTable.id eq beneficiaryId
                }
            ) { table ->
                model.name?.let { table[name] = it }
                model.iban?.let { table[iban] = it }
                model.swift?.let { table[swift] = it }
                model.bankName?.let { table[bankName] = it }
                model.bankAddress?.let { table[bankAddress] = it }
                table[updatedAt] = dateProvider.currentInstant()
            }.map {
                BeneficiaryEntity.wrapRow(it)
            }.first().toModel()
        }
    }
}