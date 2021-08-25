package br.com.zupacademy.nicolecatarina.client.bcb

data class OwnerRequest(
        val type: PersonType,
        val name: String,
        val taxIdNumber: String
)
