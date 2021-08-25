package br.com.zupacademy.nicolecatarina.pixkey.registry

import br.com.zupacademy.nicolecatarina.pixkey.AccountType
import br.com.zupacademy.nicolecatarina.pixkey.KeyType
import br.com.zupacademy.nicolecatarina.pixkey.PixKey
import br.com.zupacademy.nicolecatarina.validation.ValidPixKey
import br.com.zupacademy.nicolecatarina.validation.ValidUUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
class NewPixKey(
        @field:ValidUUID
        @field:NotBlank
        val idCustomer: String,
        @field:NotNull
        val keyType: KeyType,
        @field:Size(max = 77)
        val key: String,
        @field:NotNull
        val accountType: AccountType
) {

    fun toModel(agencia: String, conta: String, ispb: String, keyValue: String): PixKey {
        return PixKey(
                idCustomer = idCustomer,
                pixKeyType = keyType,
                pixKeyValue = keyValue,
                accountType = accountType,
                agency = agencia,
                accountNumber = conta,
                ispb = ispb
        )
    }
}