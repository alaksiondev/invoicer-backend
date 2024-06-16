package invoicer.alaksiondev.com.models

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceModel(
    val id: String,
    val externalId: String,
    val senderCompanyName: String,
    val senderCompanyAddress: String,
    val recipientCompanyAddress: String,
    val recipientCompanyName: String,
    val issueDate: String,
    val dueDate: String,
    val beneficiaryName: String,
    val beneficiaryIban: String,
    val beneficiarySwift: String,
    val beneficiaryBankName: String,
    val beneficiaryBankAddress: String,
    val intermediaryIban: String?,
    val intermediarySwift: String?,
    val intermediaryBankName: String?,
    val intermediaryBankAddress: String?,
    val activities: List<InvoiceActivityModel>?
)
