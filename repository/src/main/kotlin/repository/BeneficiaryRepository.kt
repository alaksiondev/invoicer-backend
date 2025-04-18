package repository

import datasource.api.database.BeneficiaryDatabaseSource
import datasource.api.model.beneficiary.CreateBeneficiaryData
import datasource.api.model.beneficiary.UpdateBeneficiaryData
import foundation.cache.CacheHandler
import models.beneficiary.BeneficiaryModel
import models.beneficiary.CreateBeneficiaryModel
import models.beneficiary.PartialUpdateBeneficiaryModel
import models.beneficiary.UserBeneficiaries
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
    private val databaseSource: BeneficiaryDatabaseSource,
    private val cacheHandler: CacheHandler
) : BeneficiaryRepository {

    override suspend fun create(userId: UUID, model: CreateBeneficiaryModel): String {
        return databaseSource.create(
            userId = userId,
            model = CreateBeneficiaryData(
                name = model.name,
                iban = model.iban,
                swift = model.swift,
                bankName = model.bankName,
                bankAddress = model.bankAddress
            )
        )
    }

    override suspend fun delete(userId: UUID, beneficiaryId: UUID) {
        databaseSource.delete(
            userId = userId,
            beneficiaryId = beneficiaryId
        ).also {
            cacheHandler.delete(beneficiaryId.toString())
        }
    }

    override suspend fun getById(beneficiaryId: UUID): BeneficiaryModel? {
        val cached = cacheHandler.get(
            key = beneficiaryId.toString(),
            serializer = BeneficiaryModel.serializer()
        )

        if (cached != null) return cached

        return databaseSource.getById(
            beneficiaryId = beneficiaryId
        )?.also {
            cacheHandler.set(
                key = it.id.toString(),
                value = it,
                serializer = BeneficiaryModel.serializer(),
                ttlSeconds = CACHE_TTL_SECONDS
            )
        }
    }

    override suspend fun getBySwift(userId: UUID, swift: String): BeneficiaryModel? {
        return databaseSource.getBySwift(
            userId = userId,
            swift = swift
        )
    }

    override suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): UserBeneficiaries {
        val response = databaseSource.getAll(
            userId = userId,
            page = page,
            limit = limit
        )

        return UserBeneficiaries(
            items = response.items,
            nextPage = response.nextPage,
            itemCount = response.itemCount
        )
    }

    override suspend fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: PartialUpdateBeneficiaryModel
    ): BeneficiaryModel {
        return databaseSource.update(
            userId = userId,
            beneficiaryId = beneficiaryId,
            model = UpdateBeneficiaryData(
                name = model.name,
                iban = model.iban,
                swift = model.swift,
                bankName = model.bankName,
                bankAddress = model.bankAddress
            )
        ).also { cacheHandler.delete(beneficiaryId.toString()) }
    }

    companion object {
        const val CACHE_TTL_SECONDS = 600L
    }
}