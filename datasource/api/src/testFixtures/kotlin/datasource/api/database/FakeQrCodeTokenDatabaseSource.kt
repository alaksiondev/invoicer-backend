package datasource.api.database

import models.fixtures.qrCodeTokenModelFixture
import models.qrcodetoken.QrCodeTokenModel
import java.util.*

class FakeQrCodeTokenDatabaseSource : QrCodeTokenDatabaseSource {

    var createQrCodeTokenResponse: suspend () -> QrCodeTokenModel = { qrCodeTokenModelFixture }
    var getQrCodeTokenByContentIdResponse: suspend () -> QrCodeTokenModel? = { qrCodeTokenModelFixture }
    var getQrCodeTokenByUUIDResponse: suspend () -> QrCodeTokenModel? = { qrCodeTokenModelFixture }
    var consumeQrCodeTokenResponse: suspend () -> QrCodeTokenModel? = { qrCodeTokenModelFixture }
    var expireQrCodeTokenResponse: suspend () -> Unit = {}

    override suspend fun createQrCodeToken(
        ipAddress: String,
        agent: String,
        base64Content: String,
        content: String
    ): QrCodeTokenModel {
        return createQrCodeTokenResponse()
    }

    override suspend fun getQrCodeTokenByContentId(contentId: String): QrCodeTokenModel? {
        return getQrCodeTokenByContentIdResponse()
    }

    override suspend fun getQrCodeTokenByUUID(tokenId: UUID): QrCodeTokenModel? {
        return getQrCodeTokenByUUIDResponse()
    }

    override suspend fun consumeQrCodeToken(tokenId: UUID): QrCodeTokenModel? {
        return consumeQrCodeTokenResponse()
    }

    override suspend fun expireQrCodeToken(tokenId: UUID) {
        return expireQrCodeTokenResponse()
    }
}