package repository.api.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import repository.api.repository.BeneficiaryRepositoryImpl
import repository.api.repository.IntermediaryRepositoryImpl
import repository.api.repository.InvoiceRepositoryImpl
import services.api.repository.BeneficiaryRepository
import services.api.repository.IntermediaryRepository
import services.api.repository.InvoiceRepository

val repositoryModule = DI.Module("invocer-repository") {

    bindProvider<BeneficiaryRepository> {
        BeneficiaryRepositoryImpl(
            dateProvider = instance()
        )
    }

    bindProvider<IntermediaryRepository> {
        IntermediaryRepositoryImpl(
            dateProvider = instance()
        )
    }

    bindProvider<InvoiceRepository> {
        InvoiceRepositoryImpl(
            dateProvider = instance()
        )
    }
}