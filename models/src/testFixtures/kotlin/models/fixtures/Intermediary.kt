package models.fixtures

import kotlinx.datetime.Instant
import models.intermediary.CreateIntermediaryModel
import models.intermediary.IntermediaryModel
import models.intermediary.PartialUpdateIntermediaryModel
import models.intermediary.UpdateIntermediaryModel
import java.util.*

val intermediaryModelFixture: IntermediaryModel = IntermediaryModel(
    name = "John Doe",
    iban = "DE89370400440532013000",
    swift = "DEUTDEDBFRA",
    bankName = "Deutsche Bank",
    bankAddress = "Taunusanlage 12, 60325 Frankfurt am Main, Germany",
    userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
    id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"),
    createdAt = Instant.parse("2000-06-19T00:00:00Z"),
    updatedAt = Instant.parse("2000-06-19T00:00:00Z"),
)

val createIntermediaryModelFixture: CreateIntermediaryModel = CreateIntermediaryModel(
    name = "John Doe",
    iban = "DE89370400440532013000",
    swift = "DEUTDEDBFRA",
    bankName = "Deutsche Bank",
    bankAddress = "Taunusanlage 12, 60325 Frankfurt am Main, Germany"
)

val updateIntermediaryModelFixture: UpdateIntermediaryModel = UpdateIntermediaryModel(
    name = "Jane Doe",
    iban = "FR7630006000011234567890189",
    swift = "AGRIFRPP",
    bankName = "Credit Agricole",
    bankAddress = "12 Place des Etats-Unis, 92127 Montrouge, France"
)

val partialUpdateIntermediaryModelFixture: PartialUpdateIntermediaryModel = PartialUpdateIntermediaryModel(
    name = "Jane Doe",
    iban = "FR7630006000011234567890189",
    swift = "AGRIFRPP",
    bankName = "Credit Agricole",
    bankAddress = "12 Place des Etats-Unis, 92127 Montrouge, France"
)

val userIntermediariesFixture: List<IntermediaryModel> = listOf(
    IntermediaryModel(
        name = "John Doe",
        iban = "DE89370400440532013000",
        swift = "DEUTDEDBFRA",
        bankName = "Deutsche Bank",
        bankAddress = "Taunusanlage 12, 60325 Frankfurt am Main, Germany",
        userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"),
        createdAt = Instant.parse("2000-06-19T00:00:00Z"),
        updatedAt = Instant.parse("2000-06-19T00:00:00Z"),
    ),
)