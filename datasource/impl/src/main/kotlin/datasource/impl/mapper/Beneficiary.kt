package datasource.impl.mapper

import datasource.impl.entities.BeneficiaryEntity
import models.beneficiary.BeneficiaryModel

internal fun BeneficiaryEntity.toModel(): BeneficiaryModel = BeneficiaryModel(
    name = this.name,
    iban = this.iban,
    swift = this.swift,
    bankName = this.bankName,
    bankAddress = this.bankAddress,
    userId = this.user.id.value,
    id = this.id.value,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)