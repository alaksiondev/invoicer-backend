package io.github.alaksion.invoicer.server.models

import kotlinx.datetime.LocalDate

data class GetInvoicesFilterModel(
    val minIssueDate: LocalDate?,
    val maxIssueDate: LocalDate?,
    val minDueDate: LocalDate?,
    val maxDueDate: LocalDate?,
    val senderCompanyName: String?,
    val recipientCompanyName: String?,
)
