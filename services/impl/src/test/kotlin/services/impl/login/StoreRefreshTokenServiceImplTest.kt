package services.impl.login

import kotlinx.coroutines.test.runTest
import repository.api.fakes.FakeRefreshTokenRepository
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StoreRefreshTokenServiceImplTest {
    private lateinit var service: StoreRefreshTokenServiceImpl
    private lateinit var repository: FakeRefreshTokenRepository

    @BeforeTest
    fun setUp() {
        repository = FakeRefreshTokenRepository()
        service = StoreRefreshTokenServiceImpl(repository)
    }

    @Test
    fun `when service is called should store token`() = runTest {
        service.storeRefreshToken(token = "1234", userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))

        assertEquals(
            expected = 1,
            actual = repository.storeCalls
        )
    }
}