package models.fixtures

import kotlinx.datetime.Instant
import models.invoicepdf.InvoicePdfModel
import models.invoicepdf.InvoicePdfStatus
import java.util.*

val invoicePdfModelFixture = InvoicePdfModel(
    invoiceId = UUID.fromString("12345678-1234-5678-1234-567812345678"),
    id = UUID.fromString("87654321-4321-8765-4321-567843215678"),
    path = "path/to/file",
    createdAt = Instant.parse("2023-01-01T00:00:00Z"),
    updatedAt = Instant.parse("2023-01-02T00:00:00Z"),
    status = InvoicePdfStatus.Success
)