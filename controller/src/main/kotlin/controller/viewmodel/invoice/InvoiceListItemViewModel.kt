package controller.viewmodel.invoice

import kotlinx.serialization.Serializable
import models.getinvoices.InvoiceListModel

@Serializable
internal data class InvoiceListViewModel(
    val items: List<InvoiceListItemViewModel>,
    val totalItemCount: Long,
    val nextPage: Long?
)

@Serializable
internal data class InvoiceListItemViewModel(
    val id: String,
    val externalId: String,
    val senderCompany: String,
    val recipientCompany: String,
    val issueDate: String,
    val dueDate: String,
    val createdAt: String,
    val updatedAt: String,
    val totalAmount: Long,
)

internal fun InvoiceListModel.toViewModel(): InvoiceListViewModel {
    return InvoiceListViewModel(
        items = items.map {
            InvoiceListItemViewModel(
                id = it.id.toString(),
                externalId = it.externalId,
                senderCompany = it.senderCompany,
                recipientCompany = it.recipientCompany,
                issueDate = it.issueDate.toString(),
                dueDate = it.dueDate.toString(),
                createdAt = it.createdAt.toString(),
                updatedAt = it.updatedAt.toString(),
                totalAmount = it.totalAmount,
            )
        },
        totalItemCount = totalResults,
        nextPage = nextPage
    )
}
