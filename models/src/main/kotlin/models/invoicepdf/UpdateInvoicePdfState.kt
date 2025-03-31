package models.invoicepdf

sealed interface UpdateInvoicePdfState {
    data class Success(
        val path: String
    ) : UpdateInvoicePdfState

    data object Failed : UpdateInvoicePdfState
}