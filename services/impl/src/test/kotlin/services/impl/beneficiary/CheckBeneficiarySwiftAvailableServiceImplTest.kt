package services.impl.beneficiary

import kotlinx.coroutines.test.runTest
import models.fixtures.beneficiaryModelFixture
import repository.api.fakes.FakeBeneficiaryRepository
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CheckBeneficiarySwiftAvailableServiceImplTest {

    private lateinit var service: CheckBeneficiarySwiftAvailableServiceImpl
    private lateinit var repository: FakeBeneficiaryRepository

    @BeforeTest
    fun setUp() {
        repository = FakeBeneficiaryRepository()

        service = CheckBeneficiarySwiftAvailableServiceImpl(
            repository = repository
        )
    }

    @Test
    fun `given null response then should return false`() = runTest {
        repository.getBySwiftResponse = { null }

        val result = service.execute(swift = "value", userId = UUID.fromString("d593ba02-c2bb-4be8-bd97-e71c02d229d3"))

        assertFalse { result }
    }

    @Test
    fun `given non null response then should return true`() = runTest {
        repository.getBySwiftResponse = { beneficiaryModelFixture }

        val result = service.execute(swift = "value", userId = UUID.fromString("d593ba02-c2bb-4be8-bd97-e71c02d229d3"))

        assertTrue { result }
    }

}