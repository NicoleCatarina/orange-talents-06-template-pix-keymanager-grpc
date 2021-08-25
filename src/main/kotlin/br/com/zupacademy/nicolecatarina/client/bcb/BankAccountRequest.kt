package br.com.zupacademy.nicolecatarina.client.bcb

data class BankAccountRequest(
        val participant: String,
        val branch: String,
        val accountNumber: String,
        val accountType: BCBAccountType?
)
