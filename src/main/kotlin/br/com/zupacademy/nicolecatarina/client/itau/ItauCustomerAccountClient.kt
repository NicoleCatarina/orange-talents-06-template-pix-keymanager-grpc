package br.com.zupacademy.nicolecatarina.client.itau

import br.com.zupacademy.nicolecatarina.pixkey.AccountType
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("\${itau.contas.url}")
interface ItauCustomerAccountClient {

    @Get("/clientes/{clienteId}/contas{?tipo}")
    fun findCustomerAccount(
            @PathVariable("clienteId") idCustomer: String,
            @QueryValue("tipo") type: AccountType
    ): HttpResponse<AccountResponse>
}