package datasource.api.database

import datasource.api.model.invoice.CreateInvoiceData
import datasource.api.model.invoice.GetInvoicesFilterData
import models.InvoiceModel
import models.getinvoices.InvoiceListItemModel
import models.getinvoices.InvoiceListModel
import models.fixtures.invoiceModelFixture
import models.fixtures.invoiceListModelFixture
import models.fixtures.invoiceListItemModelFixture
import java.util.*

class FakeInvoiceDatabaseSource : InvoiceDatabaseSource {

    var createInvoiceResponse: suspend () -> String = { createInvoiceResponseDefault }
    var getInvoiceByUUIDResponse: suspend () -> InvoiceModel? = { invoiceModelFixture }
    var getInvoiceByExternalIdResponse: suspend () -> InvoiceModel? = { invoiceModelFixture }
    var getInvoicesResponse: suspend () -> InvoiceListModel = { invoiceListModelFixture }
    var deleteResponse: suspend () -> Unit = {}
    var getInvoicesByBeneficiaryIdResponse: suspend () -> List<InvoiceListItemModel> = { listOf(invoiceListItemModelFixture) }
    var getInvoicesByIntermediaryIdResponse: suspend () -> List<InvoiceListItemModel> = { listOf(invoiceListItemModelFixture) }

    override suspend fun createInvoice(data: CreateInvoiceData, userId: UUID): String {
        return createInvoiceResponse()
    }

    override suspend fun getInvoiceByUUID(id: UUID): InvoiceModel? {
        return getInvoiceByUUIDResponse()
    }

    override suspend fun getInvoiceByExternalId(externalId: String): InvoiceModel? {
        return getInvoiceByExternalIdResponse()
    }

    override suspend fun getInvoices(filters: GetInvoicesFilterData, page: Long, limit: Int, userId: UUID): InvoiceListModel {
        return getInvoicesResponse()
    }

    override suspend fun delete(id: UUID) {
        return deleteResponse()
    }

    override suspend fun getInvoicesByBeneficiaryId(beneficiaryId: UUID, userId: UUID): List<InvoiceListItemModel> {
        return getInvoicesByBeneficiaryIdResponse()
    }

    override suspend fun getInvoicesByIntermediaryId(intermediaryId: UUID, userId: UUID): List<InvoiceListItemModel> {
        return getInvoicesByIntermediaryIdResponse()
    }

    companion object {
        val createInvoiceResponseDefault = "2bd1667c-459c-44a4-a74e-f4724c1ef8ab"
    }
}