package services.impl.login

import foundation.authentication.fakes.FakeAuthTokenManager
import utils.exceptions.http.HttpCode
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import models.login.RefreshTokenModel
import repository.api.fakes.FakeRefreshTokenRepository
import services.fakes.refreshtoken.FakeStoreRefreshTokenService
import services.fakes.user.FakeGetUserByIdService
import utils.exceptions.http.HttpError
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class RefreshLoginServiceImplTest {

    private lateinit var service: RefreshLoginServiceImpl
    private lateinit var tokenManager: FakeAuthTokenManager
    private lateinit var getUserByIdService: FakeGetUserByIdService
    private lateinit var refreshTokenRepository: FakeRefreshTokenRepository
    private lateinit var storeRefreshTokenService: FakeStoreRefreshTokenService

    @BeforeTest
    fun setUp() {
        tokenManager = FakeAuthTokenManager()
        getUserByIdService = FakeGetUserByIdService()
        refreshTokenRepository = FakeRefreshTokenRepository()
        storeRefreshTokenService = FakeStoreRefreshTokenService()
        service = RefreshLoginServiceImpl(
            tokenManager = tokenManager,
            getUserByIdService = getUserByIdService,
            refreshTokenRepository = refreshTokenRepository,
            storeRefreshTokenService = storeRefreshTokenService
        )
    }

    @Test
    fun `when refresh token is invalid then should throw unauthorized error`() = runTest {
        // Given
        tokenManager.verify = null

        val result = assertFailsWith<HttpError> {
            service.refreshLogin("1234")
        }

        assertEquals(
            expected = HttpCode.UnAuthorized,
            actual = result.statusCode
        )
    }

    @Test
    fun `when refresh token is not found then should throw unauthorized error`() = runTest {
        // Given
        refreshTokenRepository.userToken = { null }

        val result = assertFailsWith<HttpError> {
            service.refreshLogin("1234")
        }

        assertEquals(
            expected = HttpCode.UnAuthorized,
            actual = result.statusCode
        )
    }

    @Test
    fun `when refresh token disabled found then should throw unauthorized error`() = runTest {
        // Given
        refreshTokenRepository.userToken = {
            refreshTokenModel.copy(
                enabled = false
            )
        }

        val result = assertFailsWith<HttpError> {
            service.refreshLogin("1234")
        }

        assertEquals(
            expected = HttpCode.Forbidden,
            actual = result.statusCode
        )
    }

    @Test
    fun `when refresh token succeeds then should invalidate current token`() = runTest {
        // Given
        refreshTokenRepository.userToken = { refreshTokenModel }

        service.refreshLogin("1234")

        val (userId, token) = refreshTokenRepository.invalidateCallStack.first()

        assertEquals(
            expected = FakeGetUserByIdService.DEFAULT_RESPONSE.id,
            actual = userId
        )

        assertEquals(
            expected = token,
            actual = "1234"
        )
    }

    @Test
    fun `when refresh token succeeds then should store new refresh token`() = runTest {
        // Given
        refreshTokenRepository.userToken = { refreshTokenModel }
        tokenManager.refreshToken = "9876"

        service.refreshLogin("1234")

        val (token, userId) = storeRefreshTokenService.callHistory.first()

        assertEquals(
            expected = "9876",
            actual = token
        )

        assertEquals(
            expected = FakeGetUserByIdService.DEFAULT_RESPONSE.id,
            actual = userId
        )
    }

    @Test
    fun `when refresh token succeeds then should return genereated tokens`() = runTest {
        // Given
        refreshTokenRepository.userToken = { refreshTokenModel }
        tokenManager.refreshToken = "8888"
        tokenManager.token = "9999"

        val result = service.refreshLogin("1234")

        assertEquals(
            expected = "9999",
            actual = result.accessToken
        )

        assertEquals(
            expected = "8888",
            actual = result.refreshToken
        )
    }

    companion object {
        val refreshTokenModel = RefreshTokenModel(
            userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            token = "sampleToken",
            createdAt = Instant.parse("2023-10-05T00:00:00Z"),
            updatedAt = Instant.parse("2023-10-06T00:00:00Z"),
            enabled = true
        )
    }
}