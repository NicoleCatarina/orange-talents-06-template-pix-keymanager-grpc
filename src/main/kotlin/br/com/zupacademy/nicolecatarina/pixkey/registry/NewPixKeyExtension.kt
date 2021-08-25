package br.com.zupacademy.nicolecatarina.pixkey.registry

import br.com.zupacademy.nicolecatarina.RegistryRequest
import br.com.zupacademy.nicolecatarina.pixkey.AccountType
import br.com.zupacademy.nicolecatarina.pixkey.KeyType

fun RegistryRequest.toModel() : NewPixKey {
    return NewPixKey(
            idCustomer = idCustomer,
            keyType = KeyType.valueOf(keyType.name),
            accountType = AccountType.valueOf(accountType.name),
            key = keyValue
    )
}