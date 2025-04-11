package models.fixtures

import kotlinx.datetime.Instant
import models.qrcodetoken.QrCodeTokenModel
import models.qrcodetoken.QrCodeTokenStatusModel
import java.util.*

val qrCodeTokenModelFixture = QrCodeTokenModel(
    id = UUID.fromString("a6200473-d471-4c62-8c55-aa714adab00c"),
    agent = "Mozilla/5.0",
    base64Content = "dGVzdCBjb250ZW50",
    ipAddress = "192.168.1.1",
    rawContent = "test content",
    status = QrCodeTokenStatusModel.GENERATED,
    createdAt = Instant.parse("2023-01-01T00:00:00Z"),
    updatedAt = Instant.parse("2023-01-01T00:00:00Z"),
    expiresAt = Instant.parse("2023-01-31T00:00:00Z")
)