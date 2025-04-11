package services.impl.intermediary

import utils.exceptions.http.HttpCode
import kotlinx.coroutines.test.runTest
import models.fixtures.intermediaryModelFixture
import repository.api.fakes.FakeIntermediaryRepository
import utils.exceptions.http.HttpError
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetIntermediaryByIdServiceImplTest {

    private lateinit var service: GetIntermediaryByIdServiceImpl
    private lateinit var repository: FakeIntermediaryRepository

    @BeforeTest
    fun setUp() {
        repository = FakeIntermediaryRepository()
        service = GetIntermediaryByIdServiceImpl(
            repository = repository
        )
    }

    @Test
    fun `given no beneficiary found for given id then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getByIdResponse = { null }

            service.get(
                intermediaryId = UUID.fromString("b0a7e0bc-044a-42d1-9cc9-f0b63f7f3f36"),
                userId = UUID.fromString("8bd22165-975f-4a99-9f89-9447e57d45ad")
            )
        }

        assertEquals(
            expected = HttpCode.NotFound,
            actual = error.statusCode
        )
    }

    @Test
    fun `when beneficiaryId owner does not match userId parameter then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getByIdResponse = {
                intermediaryModelFixture.copy(
                    userId = UUID.fromString("cf6870a5-ae78-44bc-a360-211a30c2c264")
                )
            }

            service.get(
                intermediaryId = UUID.fromString("b0a7e0bc-044a-42d1-9cc9-f0b63f7f3f36"),
                userId = UUID.fromString("8bd22165-975f-4a99-9f89-9447e57d45ad")
            )
        }

        assertEquals(
            expected = HttpCode.Forbidden,
            actual = error.statusCode
        )
    }

    @Test
    fun `when beneficiaryId owner matches userId parameter then should return beneficiary`() = runTest {
        val beneficiary = intermediaryModelFixture.copy(
            userId = UUID.fromString("8bd22165-975f-4a99-9f89-9447e57d45ad")
        )

        repository.getByIdResponse = { beneficiary }

        val response = service.get(
            intermediaryId = UUID.fromString("b0a7e0bc-044a-42d1-9cc9-f0b63f7f3f36"),
            userId = UUID.fromString("8bd22165-975f-4a99-9f89-9447e57d45ad")
        )

        assertEquals(
            expected = beneficiary,
            actual = response
        )
    }
}