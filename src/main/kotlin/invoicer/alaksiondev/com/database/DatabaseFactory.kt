import invoicer.alaksiondev.com.entities.InvoiceTable
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf


object DatabaseFactory {
    val database by lazy {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/invoicer-api",
            driver = "org.postgresql.Driver",
            user = "invoicer",
            password = "1234"
        )
    }
}

val Database.invoices get() = this.sequenceOf(InvoiceTable)