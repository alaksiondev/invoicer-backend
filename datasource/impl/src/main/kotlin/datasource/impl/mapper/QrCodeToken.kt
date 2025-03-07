package datasource.impl.mapper

import entities.QrCodeTokenEntity
import models.qrcodetoken.QrCodeTokenModel
import models.qrcodetoken.QrCodeTokenStatusModel

internal fun QrCodeTokenEntity.toModel(): QrCodeTokenModel = QrCodeTokenModel(
    id = id.toString(),
    agent = agent,
    base64Content = base64Content,
    status = when (status) {
        "generated" -> QrCodeTokenStatusModel.GENERATED
        "consumed" -> QrCodeTokenStatusModel.CONSUMED
        "expired" -> QrCodeTokenStatusModel.EXPIRED
        else -> throw IllegalArgumentException("Invalid status")
    },
    createdAt = createdAt,
    updatedAt = updatedAt,
    expiresAt = expiresAt
)
