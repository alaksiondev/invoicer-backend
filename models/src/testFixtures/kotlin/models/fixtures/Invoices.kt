package models.fixtures

import kotlinx.datetime.Instant
import models.InvoiceModel
import models.InvoiceModelActivityModel
import models.beneficiary.BeneficiaryModel
import models.getinvoices.InvoiceListItemModel
import models.getinvoices.InvoiceListModel
import models.intermediary.IntermediaryModel
import models.user.UserModel
import java.util.*

val invoiceModelActivityModelFixture = InvoiceModelActivityModel(
    id = UUID.fromString("5d4c54df-fcec-4155-baf7-652aa378071b"),
    name = "Consulting Service",
    unitPrice = 10000L,
    quantity = 2
)

val invoiceModelFixture = InvoiceModel(
    id = UUID.fromString("37f3ef47-5651-49b9-890e-2bc5943bfae4"),
    externalId = "INV-123456",
    senderCompanyName = "Sender Company Ltd.",
    senderCompanyAddress = "123 Sender St, Sender City, SC 12345",
    recipientCompanyAddress = "456 Recipient Ave, Recipient City, RC 67890",
    recipientCompanyName = "Recipient Company Ltd.",
    issueDate = Instant.parse("2023-01-01T00:00:00Z"),
    dueDate = Instant.parse("2023-01-31T00:00:00Z"),
    createdAt = Instant.parse("2023-01-01T00:00:00Z"),
    updatedAt = Instant.parse("2023-01-01T00:00:00Z"),
    activities = listOf(invoiceModelActivityModelFixture),
    user = UserModel(
        id = UUID.fromString("b23899e4-63b5-42a9-9016-4647d9ec2936"),
        email = "john.doe@example.com",
        password = "1234",
        verified = true,
        createdAt = Instant.parse("2023-01-01T00:00:00Z"),
        updatedAt = Instant.parse("2023-01-01T00:00:00Z")
    ),
    beneficiary = BeneficiaryModel(
        name = "John Doe",
        iban = "DE89370400440532013000",
        swift = "DEUTDEDBFRA",
        bankName = "Deutsche Bank",
        bankAddress = "Taunusanlage 12, 60325 Frankfurt am Main, Germany",
        userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        id = UUID.fromString("746ed8ef-f40f-430b-887e-d6b939c51e9e"),
        createdAt = Instant.parse("2000-06-19T00:00:00Z"),
        updatedAt = Instant.parse("2000-06-19T00:00:00Z")
    ),
    intermediary = IntermediaryModel(
        id = UUID.fromString("c1351982-7c81-4036-a96a-6b93f6931cc9"),
        name = "Intermediary Ltd.",
        iban = "DE89370400440532013000",
        swift = "DEUTDEDBFRA",
        bankName = "Deutsche Bank",
        bankAddress = "Taunusanlage 12, 60325 Frankfurt am Main, Germany",
        userId = UUID.fromString("b23899e4-63b5-42a9-9016-4647d9ec2936"),
        createdAt = Instant.parse("2023-01-01T00:00:00Z"),
        updatedAt = Instant.parse("2023-01-01T00:00:00Z")
    )
)

val invoiceListItemModelFixture = InvoiceListItemModel(
    id = UUID.fromString("5d4c54df-fcec-4155-baf7-652aa378071b"),
    externalId = "INV-123456",
    senderCompany = "Sender Company Ltd.",
    recipientCompany = "Recipient Company Ltd.",
    issueDate = Instant.parse("2023-01-01T00:00:00Z"),
    dueDate = Instant.parse("2023-01-31T00:00:00Z"),
    createdAt = Instant.parse("2023-01-01T00:00:00Z"),
    updatedAt = Instant.parse("2023-01-01T00:00:00Z"),
    totalAmount = 20000L
)

val invoiceListModelFixture = InvoiceListModel(
    items = listOf(
        InvoiceListItemModel(
            id = UUID.fromString("5d4c54df-fcec-4155-baf7-652aa378071b"),
            externalId = "INV-123456",
            senderCompany = "Sender Company Ltd.",
            recipientCompany = "Recipient Company Ltd.",
            issueDate = Instant.parse("2023-01-01T00:00:00Z"),
            dueDate = Instant.parse("2023-01-31T00:00:00Z"),
            createdAt = Instant.parse("2023-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-01T00:00:00Z"),
            totalAmount = 20000L
        )
    ),
    totalResults = 1L,
    nextPage = null
)