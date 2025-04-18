package services.impl.pdf.pdfwriter.itext.components

import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import kotlinx.datetime.Instant
import services.impl.pdf.pdfwriter.itext.components.PdfStyle.formatDate

internal fun invoicePdfFooter(
    userEmail: String,
    createdAt: Instant,
    updatedAt: Instant,
    regularFont: PdfFont,
): Table {
    val footerTable = Table(1).apply {
        width = UnitValue.createPercentValue(100f)
    }

    footerTable.addCell(
        Cell()
            .add(
                Paragraph("Created by: (${userEmail})")
                    .setFont(regularFont)
                    .setFontSize(PdfStyle.FontSize.Small)
            )
            .add(
                Paragraph("Created at: ${formatDate(createdAt)} - Last updated at: ${formatDate(updatedAt)}")
                    .setFont(regularFont)
                    .setFontSize(PdfStyle.FontSize.Small)
            )
            .setTextAlignment(TextAlignment.CENTER)
            .setBorder(null)
    )

    return footerTable
}