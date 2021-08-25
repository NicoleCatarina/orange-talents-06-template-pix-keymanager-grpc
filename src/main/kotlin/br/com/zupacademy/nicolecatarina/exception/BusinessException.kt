package br.com.zupacademy.nicolecatarina.exception

import io.grpc.Status

interface BusinessException {

    val status: Status
    val message: String
}