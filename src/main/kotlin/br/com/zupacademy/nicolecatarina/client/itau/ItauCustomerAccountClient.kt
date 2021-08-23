package br.com.zupacademy.nicolecatarina.client.itau

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:9091/api/v1")
interface ItauCustomerAccountClient {

    @Get("/clientes/{clienteId}/contas{?tipo}")
    fun findCustomerAccount(
            @PathVariable("clienteId") idCustomer: String,
            @QueryValue("tipo") type: AccountType
    ): HttpResponse<AccountResponse>

}