package io.github.alaksion.invoicer.server.app.database

import org.postgresql.util.PGobject

class PostgreEnum<T : Enum<T>>(
    enumTypeName: String,
    enumValue: T?
) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}