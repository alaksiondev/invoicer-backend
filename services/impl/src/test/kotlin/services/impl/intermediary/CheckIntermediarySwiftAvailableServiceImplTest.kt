package services.impl.intermediary

import kotlinx.coroutines.test.runTest
import repository.test.repository.FakeBeneficiaryRepository
import repository.test.repository.FakeIntermediaryRepository
import services.api.services.intermediary.CheckIntermediarySwiftAvailableService
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CheckIntermediarySwiftAvailableServiceImplTest {

    private lateinit var service: CheckIntermediarySwiftAvailableServiceImpl
    private lateinit var repository: FakeIntermediaryRepository

    @BeforeTest
    fun setUp() {
        repository = FakeIntermediaryRepository()

        service = CheckIntermediarySwiftAvailableServiceImpl(
            repository = repository
        )
    }

    @Test
    fun `given null response then should return false`() = runTest {
        repository.getBySwiftResponse = { null }

        val result = service.execute(swift = "value", userId = "d593ba02-c2bb-4be8-bd97-e71c02d229d3")

        assertFalse { result }
    }

    @Test
    fun `given non null response then should return true`() = runTest {
        repository.getBySwiftResponse = { IntermediaryTestData.intermediary }

        val result = service.execute(swift = "value", userId = "d593ba02-c2bb-4be8-bd97-e71c02d229d3")

        assertTrue { result }
    }

}