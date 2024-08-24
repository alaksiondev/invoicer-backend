package io.github.alaksion.invoicer.server.view.viewmodel.intermediary

import io.github.alaksion.invoicer.server.domain.model.intermediary.IntermediaryModel
import kotlinx.serialization.Serializable

@Serializable
data class UserIntermediaryViewModel(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
    val id: String
)

internal fun IntermediaryModel.toViewModel() = UserIntermediaryViewModel(
    name = name,
    iban = iban,
    swift = swift,
    bankName = bankName,
    bankAddress = bankAddress,
    id = this.id
)

@Serializable
data class UserIntermediariesViewModel(
    val intermediaries: List<UserIntermediaryViewModel>
)
