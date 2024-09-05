package io.github.alaksion.invoicer.server.domain.model.beneficiary

data class CreateBeneficiaryModel(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
)