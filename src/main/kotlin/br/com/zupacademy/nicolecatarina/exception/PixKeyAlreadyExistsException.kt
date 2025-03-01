package br.com.zupacademy.nicolecatarina.exception

import io.grpc.Status

class PixKeyAlreadyExistsException(override val message: String) : RuntimeException(), BusinessException {
    override val status: Status = Status.ALREADY_EXISTS
}